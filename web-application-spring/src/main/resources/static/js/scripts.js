
$(document).ready(function () {

    const ws = new WebSocket("ws://localhost:8083/ds");

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
                data += renderSensor(item)
                break;
            default: break;
        }
    })
    data+="</div></div></div>";
    $("#data").html(data);
}

function renderSensor(item){

    let data = ""

    // TODO as a notification ?
    if(item.error !== undefined){
        data+=`<div id='${item.metadata.id}'><p>Error detected: ${item.error}</p></div>`;
    }

    // FIXME code duplication
    if(item.state === undefined){
        data+=`
            <div class="col-md-4" id="${item.metadata.id}">
                <div class="card mb-4 box-shadow">
                    <img class="card-img-top" src="../img/topImg.png" alt="Card image cap">
                    <div class="card-body">
                        <p>Device name: ${item.metadata.name}</p>
                        <p>Status unknown</p>
                    </div>
                </div>
            </div>`;
        return data;
    }

    // TODO check (if not present in the json -> replace with string 'undefined')
    let lastUpdate = new Date(item.state.lastUpdate).toLocaleString();
    // TODO check (if not present in the json -> replace with string 'undefined')
    let lastConnection = new Date(item.state.lastConnection).toLocaleString();
    // TODO add last disconnection

    // FIXME code duplication
    if(!item.state.active){
        data+=`
            <div class="col-md-4" id="${item.metadata.id}">
                <div class="card mb-4 box-shadow">
                    <img class="card-img-top" src="img/topImg.png" alt="Card image cap">
                    <div class="card-body">
                        <p>Device name: ${item.metadata.name}</p>
                        <div class="d-flex justify-content-between align-items-center">
                            <p>Device is not active. Last connection: ${lastConnection}</p>
                            <small class="text-muted">${lastUpdate}</small>
                        </div>
                    </div>
                </div>
            </div>`;
        return data;
    }

    // FIXME code duplication
    let components = renderDeviceComponents(item)
    data+=`
        <div class="col-md-4" id="${item.metadata.id}">
            <div class="card mb-4 box-shadow">
                <img class="card-img-top" src="img/topImg.png" alt="Card image cap">
                <div class="card-body">
                    <p>Device name: ${item.metadata.name}</p>
                    <ul> ${components} </ul>
                    <div class="d-flex justify-content-between align-items-center">
                        <small class="text-muted">${lastUpdate}</small>
                    </div>
                </div>
            </div>
        </div>`;

    return data;
}

function renderDeviceComponents(obj){
    let data = '';

    obj.metadata.components.forEach(function (component) {

        let mainProperty = component.mainProperty;
        let deviceId = obj.metadata.id;
        let componentId = component.id;

        if(component.writableProperties === undefined){
            // FIXME code duplication
            data+=`
                <li id="${component.id}"> 
                    ${mainProperty.description} <span> ${mainProperty.value} </span> ${mainProperty.unit}
                    <p>
                        <button class="btn btn-primary" type="button" data-toggle="collapse" data-target="#${component.id}-info" aria-expanded="false" aria-controls="${component.id}-info"> Info </button>
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
                                            <li>max: ${mainProperty.constraint.max} </li>
                                        </ul>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </p>
                </li>`;
            return data;
        }

        // FIXME code duplication
        data+=`
            <li id="${component.id}">
                ${mainProperty.description} ${mainProperty.value} ${mainProperty.unit}
                <p>
                    <button class="btn btn-primary" type="button" data-toggle="collapse" data-target="#${component.id}-info" aria-expanded="false" aria-controls="${component.id}-info">Info</button>
                    <button class="btn btn-primary" type="button" data-toggle="collapse" data-target="#${component.id}-edit" aria-expanded="false" aria-controls="${component.id}-edit">Edit</button>
                    
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
                    
                    <div class="collapse" id="${component.id}-edit">
                        <div class="card card-body">
                        <form id="${obj.state.owner}--${obj.id}--${component.id}">`;

                        component.writableProperties.forEach(function (property , key) {
                            data+=`
                                <label for="${component.id}-input${key}">Enter ${property.description}</label>
                                <input type="number" class="form-control" id="${component.id}-input${key}" placeholder="${property.name}" min=${property.constraint.min} max=${property.constraint.max} step="0.1">
                                <button class="btnChangeSens btn btn-primary" id="btn${component.id}-input${key}" type="button" onclick="sendRequestToChangeProperty('${deviceId}', '${componentId}', '${property.name}')">Submit</button>`;
                        });
        data += "</form></div></div></p></li>"
    });
    return data;
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
    // updating value
    filteredComponents[0].value = deviceMessage.value;
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
        // FIXME ???
        $(`#${json.message.component} > span`).text(json.value);
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
function sendRequestToChangeProperty(device, component, property){
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
