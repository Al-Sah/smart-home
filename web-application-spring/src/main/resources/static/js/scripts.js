
$(document).ready(function () {

    const ws = new WebSocket("ws://188.166.82.71:8083/ds");

    ws.onmessage = function (event) {
        let jsonMessage = JSON.parse(event.data);
        let data = jsonMessage.data;
        console.log(jsonMessage);

        switch (jsonMessage.action) {
            case "START":
                onStart(data);
                showDevicesInfo();
                //showHubsInfo();
                break;
            case "HUB_CONNECTED": break;
            case "DEVICE_CONNECTED":
                onDeviceConnected(data);
                showDevicesInfo();
                break;
            case "DEVICE_MESSAGE":
                onDeviceMessage(data);
                break;
            case "DEVICE_DISCONNECTED":
                onDeviceDisconnected(data)
                break;
            case "HUB_DISCONNECTED": break;
            default: break;
        }
    }

});

let deviceList = Array();
let hubList = Array();


/*
* Show all devices information to screen
*/
function showDevicesInfo() {
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

    // TODO check (if not present in the json -> replace with string 'undefined')
    let lastUpdate = new Date(item.state.lastUpdate).toLocaleString();
    // TODO check (if not present in the json -> replace with string 'undefined')
    let lastConnection = new Date(item.state.lastConnection).toLocaleString();
    // TODO add last disconnection

    let content;

    if(item.state === undefined){
        content =`<p>Status unknown</p>`;
    } else if(item.state.active){
        content = `
            <ul> ${generateDeviceComponentsHtml(item)} </ul>
            <div class="d-flex justify-content-between align-items-center">
                <small class="text-muted">${lastUpdate}</small>
            </div>`;
    } else {
        content = `
            <div class="d-flex justify-content-between align-items-center">
                <p>Device is not active. Last connection: ${lastConnection}</p>
                <small class="text-muted">${lastUpdate}</small>
            </div>`;
    }

    return `
        <div class="col-md-4" id="${item.metadata.id}">
            <div class="card mb-4 box-shadow">
                <img class="card-img-top" src="img/topImg.png" alt="Card image cap">
                <div class="card-body">
                    <p>Device name: ${item.metadata.name}</p>
                    ${content}
                </div>
            </div>
        </div>`;
}

function generateDeviceComponentsHtml(obj){
    let data = '';

    obj.metadata.components.forEach(function (component) {

        let mainProperty = component.mainProperty;

        let editSection = '';
        let buttons = ` <button class="btn btn-primary" type="button" data-toggle="collapse" data-target="#${component.id}-info" aria-expanded="false" aria-controls="${component.id}-info"> Info </button>`;
        let baseSection = `${mainProperty.description} <span id="${component.id}--span"> ${mainProperty.value} </span> ${mainProperty.unit}`;
        if(component.writableProperties !== undefined){
            buttons += `<button class="btn btn-primary" type="button" data-toggle="collapse" data-target="#${component.id}-edit" aria-expanded="false" aria-controls="${component.id}-edit"> Edit </button>`;
            editSection = printEditPropertySection(obj.state.owner, obj.metadata.id, component.id, component.writableProperties)
            baseSection = `${mainProperty.description} ${mainProperty.value} ${mainProperty.unit}`;
        }

       data+=` 
        <li id="${component.id}">
            ${baseSection}
            <p>
                ${buttons}
                <div class="collapse" id="${component.id}-info">
                    <div class="card card-body">
                        Const properties:
                        <div>
                            <p>name: ${mainProperty.name}</p>
                            <p>unit: ${mainProperty.unit}</p>
                            <p>description: ${mainProperty.description}</p>
                            <p>constraint:
                                <ul>
                                    <li>type:${mainProperty.constraint.type} </li>
                                    <li>min: ${mainProperty.constraint.min}</li>
                                    <li>max:${mainProperty.constraint.max} </li>
                                </ul>
                            </p>
                        </div>
                    </div>
                </div>
                ${editSection}
            </p>
       </li>`
    });

    return data;
}

function printEditPropertySection(hubId, deviceId, componentId, properties){
    let items;
    properties.forEach(function (property , key) {
        items+=`
            <label for="${componentId}-input${key}">Enter ${property.description}</label>
            <input type="number" class="form-control" id="${componentId}-input${key}" placeholder="${property.name}" min=${property.constraint.min} max=${property.constraint.max} step="0.1">
            <button class="btnChangeSens btn btn-primary" id="btn${componentId}-input${key}" type="button" onclick="sendRequestToChangeProperty(${hubId},'${deviceId}', '${componentId}', '${property.name}')">Submit</button>`;
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

/**
 * Show all hubs information to screen
 */
function showHubsInfo() {
/*    let data ="";
    hubList.forEach(function (item) {
        if(item.active){
            let lastUpdate = new Date(item.state.lastUpdate).toLocaleString();
            data+=`<div id=${item.id}><p>Hub name: ${item.id}</p><p>Last update: ${lastUpdate}</p></div>`
        }
    });
    $('#data').append(data);*/
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
    // TODO replace with filter ?
    deviceList.forEach(function (item) {
        if(json.state === item.id){
            // FIXME !! 'components' is an array (each component has main property)
            item.metadata.components.mainProperty.value = json.components.value;
            item.state.active = true;
            addStatus = false;
            // TODO add buttons .....
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
        console.log(deviceList)
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
        // TODO Create GUI element to show error ...
    }

    console.log(json.message)

    if(updateDeviceProperty(json.message)){
        // FIXME !!!!!!!!!!!!!
        console.log(json.message.component);
        //$(`li#${json.message.component} > span`).text(json.value);
        $(`li#${json.message.component} > span`).text(json.value);
    }
    // TODO update state ????? ('lastUpdate' property at least)
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
    // TODO update other 'state' parameters
    // TODO update UI ?
}


/**
 * Change settings of component
 */
function sendRequestToChangeProperty(hub, device, component, property){
    console.log("AAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
    console.log(component)
    console.log(property)
    console.log(device)
    // TODO pass new value ???

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
