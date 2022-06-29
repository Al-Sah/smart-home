
$(document).ready(function () {

    const ws = new WebSocket("ws://188.166.82.71:8083/ds");

    ws.onmessage = function (event) {
        let jsonMessage = JSON.parse(event.data);
        let data = jsonMessage.data;
        //console.log('Device message:', jsonMessage.action);
        switch (jsonMessage.action) {
            case "START":
                onStart(data);
                //console.log('Devices:', deviceList)
                showDevicesInfo();
                showHubsInfo();break;
            case "HUB_CONNECTED": break;
            case "DEVICE_CONNECTED":
                onDeviceConnected(data);
                showDevicesInfo();break;
            case "DEVICE_MESSAGE":
                onDeviceMessage(data);break;
            case "DEVICE_DISCONNECTED":
                onDeviceDisconnected(data);break;
            case "HUB_DISCONNECTED": break;
            default: break;
        }
    }

});

let deviceList = Array();
let hubList = Array();


/**
* Show all devices information to screen
*/
function showDevicesInfo() {
    $('#data').html("");
    let data ="<div class=\"album py-5 bg-light\"><div class=\"container\"><div class=\"row\">";
    deviceList.forEach(function (item) {
        switch (item.metadata.type) {
            case "SENSOR":
                data += generateSensorHtml(item)
                break;
            default: break;
        }
    })
    data+="</div></div></div>";
    $("#data").html(data);
}

function generateSensorHtml(item){

    let state;

    if(!("state" in item)){
        state = `
            <div class="box-shadow">
                <p>Device state in undefined (inactive)</p>
            </div>`;
    } else {
        let lastUpdate = ("lastUpdate" in item.state) ? new Date(item.state.lastUpdate).toLocaleString() : "undefined";
        let lastConnection = ("lastConnection" in item.state) ? new Date(item.state.lastConnection).toLocaleString() : "undefined";
        state = `
            <div class="box-shadow">
                Device is ${item.state.active ? "active" : "inactive"}:
                <p>
                    <small class="text-muted">last connection: ${lastConnection} <br> last update: ${lastUpdate}</small>
                </p>
            </div>`;
    }

    let icon = "img/default.png";
    if(item.metadata.name.includes("thermometer")){
        icon = "img/thermometer.png";
    }

    return `
        <div class="col-md-4" id="${item.metadata.id}">
            <div class="card mb-4 box-shadow">
                <div class="card-body">
                    <div class="row media border border-light rounded">
                        <div class="col-md-auto">
                            <p class="text-primary"> Type: ${item.metadata.name} <br> <small class="text-secondary"> id: ${item.metadata.id}</small></p>
                            ${state}
                        </div>
                        <div class="col col-lg-2">
                            <img class="ml-3 rounded" src="${icon}" alt="icon (._.)" width="80" height="80">
                        </div>
                    </div>
                    ${generateDeviceComponentsHtml(item)}
                    ${generateDeviceHistoryHtml(item)}
                </div>
            </div>
        </div>
    `;
}

function generateDeviceComponentsHtml(obj){
    let data = '';

    obj.metadata.components.forEach(function (component) {

        let mainProperty = component.mainProperty;

        let editSection = '';
        let constSection ='';
        let trimmedValue = parseFloat(mainProperty.value).toFixed(1);
        let buttons ='';
        //let buttons = ` <button class="btn btn-primary" type="button" data-bs-toggle="collapse" data-bs-target="#${component.id}-info" aria-expanded="false" aria-controls="${component.id}-info"> Contanat props </button>`;
        let baseSection = `${mainProperty.description} <span id="${component.id}--span"> ${trimmedValue} </span> ${mainProperty.unit}`;
        if(component.writableProperties !== undefined){
            buttons += `<button class="btn btn-primary" type="button" data-bs-toggle="collapse" data-bs-target="#${component.id}-edit" aria-expanded="false" aria-controls="${component.id}-edit"> Writable props </button>`;
            editSection = printEditPropertySection(obj.state.owner, obj.metadata.id, component.id, component.writableProperties);
            //baseSection = `${mainProperty.description} <span id="${component.id}--span">${trimmedValue}</span> ${mainProperty.unit}`;
        }
        if(component.constProperties !== undefined){
            //console.log('Const Prop',component.constProperties);
            buttons+=` <button class="btn btn-primary" type="button" data-bs-toggle="collapse" data-bs-target="#${component.id}-const" aria-expanded="false" aria-controls="${component.id}-const"> Constant props </button>`;
            constSection = printConstPropertySection(obj.state.owner, obj.metadata.id, component.id, component.constProperties);
        }
        let changeSection = buttons+constSection+editSection;
        if(changeSection !== ''){
            changeSection = `<p>${changeSection}</p>`
        }
       data+=` 
        <li id="${component.id}">
            ${baseSection}
            ${changeSection}
       </li>`
    });

    return data;
}

function printConstPropertySection(hubId, deviceId, componentId, properties){
    let items = "";
    if(properties)
    properties.forEach(function (property , key) {
        items+=`<div>
            <p>name: ${property.name}</p>
            <p>unit: ${property.unit}</p>
            <p>description: ${property.description}</p>
            <p>value: ${property.value}</p>`;
        if(property.constraint !== undefined){
            items+=`<ul>
                <li>type:${property.constraint.type} </li>
                <li>min: ${property.constraint.min}</li>
                <li>max:${property.constraint.max} </li>
            </ul>`;
        }
        items+='</div>';
    })
    return `
        <div class="collapse" id="${componentId}-const">
            <div class="card card-body">
                <form id="${hubId}--${deviceId}--${componentId}">
                    ${items}
                </form>
            </div>
        </div>`;
}

function printEditPropertySection(hubId, deviceId, componentId, properties){
    let items = "";
    properties.forEach(function (property , key) {
        items+=`<div><label for="${componentId}-input${key}">Enter ${property.description}</label>
            <input type="number" class="form-control" id="${componentId}-input${key}" placeholder="${property.name}" min=${property.constraint.min} max=${property.constraint.max} step="0.1">
            <button class="btnChangeSens btn btn-primary" id="btn${componentId}-input${key}"  type="button" onclick="sendRequestToChangeProperty(this, '${hubId}','${deviceId}', '${componentId}', '${property.name}','${componentId}-input${key}')">Submit</button></div>`;
    });
    return `
        <div class="collapse" id="${componentId}-edit">
            <div class="card card-body">
                <form id="${hubId}--${deviceId}--${componentId}">
                    ${items}
                </form>
            </div>
        </div>`;
}

function generateDeviceHistoryHtml(obj){
    let data = "";
    data+=`<div> <a href="history.html?${obj.metadata.id}">Device history</a></div>`;
    return data;
}
/*function generateDeviceHistoryHtml(obj){
    let data ="";
    data+=`<div>
            <button class="btn btn-primary" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvas--${obj.metadata.id}" aria-controls="offcanvas--${obj.metadata.id}">
                Load history of device
            </button>
            <div class="offcanvas offcanvas-start" tabindex="-1" id="offcanvas--${obj.metadata.id}" aria-labelledby="offcanvas${obj.metadata.id}Label">
                 <div class="offcanvas-header">
                    <h5 class="offcanvas-title" id="offcanvas${obj.metadata.id}leLabel">${obj.metadata.name} history:</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
                </div>
                <div class="offcanvas-body">
                    <div>
                    Some text as placeholder. In real life you can have the elements you have chosen. Like, text, images, lists, etc.
                    </div>
                </div>
            </div>
    </div>`;
    return data;
}*/
/**
 * Show all hubs information to screen
 */
function showHubsInfo() {
    let data ="<div class=\"album py-5 bg-light\"><div class=\"container\"><div class=\"row\">";
    hubList.forEach(function (item) {
        data+=generateHubHtml(item)
    })
    data+="</div></div></div>";
    $("#data").append(data);
}

function generateHubHtml(item){
    let state;

    if(!("active" in item)){
        state = `
            <div class="box-shadow">
                <p>Device state in undefined (inactive)</p>
            </div>`;
    } else {
        let lastUpdate = ("lastUpdate" in item) ? new Date(item.lastUpdate).toLocaleString() : "undefined";
        let lastConnection = ("lastConnection" in item) ? new Date(item.lastConnection).toLocaleString() : "undefined";
        state = `
            <div class="box-shadow">
                Device is ${item.active ? "active" : "inactive"}:
                <p>
                    <small class="text-muted">last connection: ${lastConnection} <br> last update: ${lastUpdate}</small>
                </p>
            </div>`;
    }

    let icon = "img/default.png";
    return `
        <div class="col-md-4" id="${item.id}">
            <div class="card mb-4 box-shadow">
                <div class="card-body">
                    <div class="row media border border-light rounded">
                        <div class="col-md-auto">
                            <p class="text-primary"> Type: HUB <br> <small class="text-secondary"> id: ${item.id}</small></p>
                            ${state}
                        </div>
                        <div class="col col-lg-2">
                            <img class="ml-3 rounded" src="${icon}" alt="icon (._.)" width="80" height="80">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `;
}

/**
 * 'START' message handler
 */
function onStart(json) {
    json.devices.forEach(function (item) {
        deviceList.push(item);
    });

    json.hubsState.forEach(function (item) {
        hubList.push(item);
    });
}

/**
 * 'DEVICE_CONNECTED' message handler
 */
function onDeviceConnected(json) {

    let addStatus = true;
    deviceList.forEach(function (item) {
        if(json.state === item.id){
            item.metadata.components = json.metadata.components;
            item.state = json.metadata.state;
            addStatus = false;
        }
    });
    if (addStatus){
        deviceList.push(json);
    }
}

/**
 * Update device property in the array;
 *
 * @param deviceMessage
 * @return {boolean}
 *  <b>true</b> when property was updated and
 *  <b>false</b> when property was not updated (failure)
 */
function updateDeviceProperty(deviceMessage) {
    // searching device
    let filteredDevices = deviceList.filter(object => {
        return object.metadata.id === deviceMessage.device;
    })
    if(filteredDevices[0] === undefined){
        console.error("ERROR: devices filtering; searched device: " + deviceMessage.device);
        return false;
    }

    // searching device component
    let device = filteredDevices[0].metadata;
    let filteredComponents = device.components.filter(component => {
        return component.id === deviceMessage.component;
    })
    if(filteredComponents[0] === undefined){
        console.error("ERROR: components filtering; searched component: " + deviceMessage.component);
        return false;
    }


    //searching for property
    let component = filteredComponents[0];
    if(component.mainProperty.name === deviceMessage.property){
        component.mainProperty.value = deviceMessage.value;
        return true;
    }

    let filteredProperty = component.writableProperties.filter(property => {
        return property.name === deviceMessage.property;
    })
    if(filteredProperty[0] === undefined){
        console.error("ERROR: components filtering; searched component: " + deviceMessage.component);
        return false;
    }
    // updating value
    filteredProperty[0].value = deviceMessage.value;
    return true;
}

/**
 * 'DEVICE_MESSAGE' message handler
 */
function onDeviceMessage(json){
    if(json.error !== undefined){
        console.log(json.error);
        return;
    }

    if(updateDeviceProperty(json.message)){
        let trimmedValue = parseFloat(json.message.value).toFixed(1);
        let lastConnection = new Date(json.state.lastConnection).toLocaleString();
        let lastUpdate = new Date(json.state.lastUpdate).toLocaleString();
        let timeStamp = `last connection: ${lastConnection} <br> last update: ${lastUpdate}`;
        $(`#${json.message.component}--span`).text(trimmedValue);
        $(`#${json.message.device} > small`).text(timeStamp);
    }
}


/**
 * 'DEVICE_DISCONNECTED' message handler
 */
function onDeviceDisconnected(json) {

    let id = json.details.id;

    let filteredDevices = deviceList.filter(device => {
        return device.metadata === id;
    })
    if(filteredDevices[0] === undefined){
        console.error("ERROR: devices filtering; searched device: " + id);
        return false;
    }
    filteredDevices[0].state.active = false;
}

/**
 * Change settings of component
 */
function sendRequestToChangeProperty(btn, hub, device, component, property, input_id){
    let btnID = btn.getAttribute('id');
    let value = $(`#${input_id}`).val();
    let diff = 5;
    let expireIn = new Date(new Date().getTime() + diff*60000).getTime();
    let objRequest = {
        "hub": hub,
        "device": device,
        "component": component,
        "property": property,
        "value": value,
        "options": "",
        "expire": expireIn,
    }
    $.ajax({
        url:'http://localhost:8080/command',
        type: 'POST',
        cache: false,
        data: JSON.stringify(objRequest),
        dataType: 'json',
        beforeSend: function() {
            $(`#${btnID}`).prop('disabled',true);
        },
        success: function(data) {
            console.log(data);
            $(`#${btnID}`).prop('disabled',false);
        },
        error: function () {
            console.log('error');
            $(`#${btnID}`).prop('disabled',false);
        }
    });

/*    let parent_id = $(this).parent().attr('id');
    let IDs = parent_id.split("--");
    let ID_input = $(this).attr('id').replace('btn','');
    let diff = 5;
    let expireIn = new Date(new Date().getTime() + diff*60000).getTime();
    let objRequest = {
        "hub": IDs[0],
        "device": IDs[1],
        "component": IDs[2],
        "property": "deltaT",
        "value": $(ID_input).val(),
        "options": "",
        "expire": expireIn,
    }

    console.log(JSON.stringify(objRequest));*/
}

