$(document).ready(function () {
    /*
* Connection to WebSocket
*/
    const ws = new WebSocket("ws://188.166.82.71:8083/ds");
    ws.onopen = function () {
    }
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
                    if(item.error === undefined){
                        if(item.state === undefined){
                            data+=`<div class = 'col-md-4' id='${item.metadata.id}'><div class="card mb-4 box-shadow"><img class="card-img-top"  src="../img/topImg.png" alt="Card image cap"><div class="card-body"><p>Device name: ${item.metadata.name}</p><p>Status unknown</p></div></div></div>`;break;
                        }
                        let lastUpdate = new Date(item.state.lastUpdate).toLocaleString();
                        let lastConnection = new Date(item.state.lastConnection).toLocaleString();
                        if(!item.state.active){
                            data+=`<div class = 'col-md-4' id='${item.metadata.id}'><div class="card mb-4 box-shadow"><img class="card-img-top"   src="img/topImg.png" alt="Card image cap"><div class="card-body"><p>Device name: ${item.metadata.name}</p>`;
                            data+=`<div class="d-flex justify-content-between align-items-center"><p>Device is not active. Last connection: ${lastConnection}</p><small class="text-muted">${lastUpdate}</small></div></div></div></div>`;
                        }
                        else{
                            data+=`<div class = 'col-md-4' id='${item.metadata.id}'><div class="card mb-4 box-shadow"><img class="card-img-top"   src="img/topImg.png" alt="Card image cap"><div class="card-body"><p>Device name: ${item.metadata.name}</p><ul>`;
                            item.metadata.components.forEach(function (item1, key) {
                                if(item1.writableProperties === undefined){
                                    data+=`<li id="${item1.id}">${item1.mainProperty.description} <span>${item1.mainProperty.value}</span> ${item1.mainProperty.unit}<p>
                                      <button class="btn btn-primary" type="button" data-toggle="collapse" data-target="#${item1.id}-info" aria-expanded="false" aria-controls="${item1.id}-info">
                                        Info
                                      </button>
                                      <div class="collapse" id="${item1.id}-info">
                                        <div class="card card-body">
                                        Const properties:
                                        <div>
                                        <p>name: ${item1.mainProperty.name}</p>
                                        <p>unit: ${item1.mainProperty.unit}</p>
                                        <p>description: ${item1.mainProperty.description}</p>
                                        <p>constraint:
                                            <ul>
                                            <li>type:${item1.mainProperty.constraint.type} </li>
                                            <li>min: ${item1.mainProperty.constraint.min}</li>
                                            <li>max:${item1.mainProperty.constraint.max} </li>
</ul>
                                        </p>
</div>
                                        </div>
                                    </div>
                                    </p>
                                    </li>`;
                                }
                                else {
                                    data+=`<li id="${item1.id}">${item1.mainProperty.description} ${item1.mainProperty.value} ${item1.mainProperty.unit}<p>
                                      <button class="btn btn-primary" type="button" data-toggle="collapse" data-target="#${item1.id}-info" aria-expanded="false" aria-controls="${item1.id}-info">
                                        Info
                                      </button>
                                      <button class="btn btn-primary" type="button" data-toggle="collapse" data-target="#${item1.id}-edit" aria-expanded="false" aria-controls="${item1.id}-edit">
                                        Edit
                                      </button>
                                      <div class="collapse" id="${item1.id}-info">
                                        <div class="card card-body">
                                            Const properties:
                                            <div>
                                        <p>name: ${item1.mainProperty.name}</p>
                                        <p>unit: ${item1.mainProperty.unit}</p>
                                        <p>description: ${item1.mainProperty.description}</p>
                                        <p>constraint:
                                            <ul>
                                            <li>type:${item1.mainProperty.constraint.type} </li>
                                            <li>min: ${item1.mainProperty.constraint.min}</li>
                                            <li>max:${item1.mainProperty.constraint.max} </li>
                                            </ul>
                                        </p>
                                        </div>
                                        </div>
                                        </div>
                                        <div class="collapse" id="${item1.id}-edit">
                                        <div class="card card-body">
                                         <form id="${item.state.owner}--${item.id}--${item1.id}">`;
                                    item1.writableProperties.forEach(function (item2,key) {
                                        data+=`<label for="${item1.id}-input${key}">Enter ${item2.description}</label>
                                           <input type="number" class="form-control" id="${item1.id}-input${key}" placeholder="${item2.name}" min=${item2.constraint.min} max=${item2.constraint.max} step="0.1">
                                           <button class="btnChangeSens btn btn-primary" id="btn${item1.id}-input${key}" type="button">Submit</button>
                                        </form>
                                        </div>
                                        </div>
                                    </p>
                                    </li>`;
                                    });
                                }
                            });
                            data+=`</ul><div class="d-flex justify-content-between align-items-center"><small class="text-muted">${lastUpdate}</small></div></div></div></div>`;
                        }
                    }else{
                        data+=`<div id='${item.metadata.id}'><p>Error detected: ${item.error}</p></div>`;
                    }
                    break;
                default: break;
            }
        })
        data+="</div></div></div>";
        $("#data").html(data);
    }
    /*
    * Show all hubs information to screen
    */
    /*function showHubsInfo() {
        let data ="";
        hubList.forEach(function (item) {
            if(item.active){
                let lastUpdate = new Date(item.state.lastUpdate).toLocaleString();
                data+=`<div id=${item.id}><p>Hub name: ${item.id}</p><p>Last update: ${lastUpdate}</p></div>`
            }
        });
        $('#data').append(data);
    }*/
    /*
    * On START function
    * */
    function onStart(answer) {
        answer.data.devices.forEach(function (item) {
            deviceList.push(item);

        });
        answer.data.hubsState.forEach(function (item) {
            hubList.push(item);
        });
    }
    /*
    * Device connected
    */
    function newDeviceConnected(answer) {
        let addStatus = true;
        deviceList.forEach(function (item) {
            if(answer.data.state == item.id){
                item.metadata.components.mainProperty.value = answer.data.components.value;
                item.state.active = true;
                addStatus = false;
            }
        });
        if (addStatus){
            deviceList.push(answer.data);
        }
    }
    /*
    *   Change device property
    */
    function changeDeviceProp(answer) {
        if(answer.error === undefined){
            let device_id = answer.message.component;
            deviceList.forEach(function (item) {
                if(item.metadata.id == device_id){
                    item.metadata.components.forEach(function (item1){
                        if(item1.id == answer.message.component){
                            item1.value = answer.message.value;
                        }
                    });
                }
            });
            $(`#${device_id} > span`).text(answer.message.value);
        }
    }
    /*
    *   Device disconnected
    */
    function disconnectDevice(answer) {
        deviceList.forEach(function (item) {
            if(item.metadata.id == answer.details.id){
                item.state.active = false;
            }
        });
    }
    /*
* Change settings of component
*/
    $('.btnChangeSens').on('click', ".btnChangeSens", function(){
        let parent_id = $(this).parent().attr('id');
        let IDs = parent_id.split("--");
        let ID_input = $(this).attr('id').replace('btn','');
        let value = $(ID_input).val();
        let timeNow = new Date();
        let diff = 5;
        let expireIn = new Date(timeNow.getTime() + diff*60000).getTime();
        let objRequest = {
            "hub": IDs[0],
            "device": IDs[1],
            "component": IDs[2],
            "property": "deltaT",
            "value": value,
            "option": "",
            "expire": expireIn,
        }

        let jsonStr = JSON.stringify(objRequest);
        console.log(jsonStr);
    })
    /*
    * When receive a message from server
    * */
    ws.onmessage = function (event) {
        let answer = JSON.parse(event.data);
        switch (answer.action) {
            case "START":
                onStart(answer);
                showDevicesInfo();
                //showHubsInfo();
                break;
            case "HUB_CONNECTED": break;
            case "DEVICE_CONNECTED":
                newDeviceConnected(answer);
                break;
            case "DEVICE_MESSAGE":
                changeDeviceProp(answer.data);
                break;
            case "DEVICE_DISCONNECTED":
                disconnectDevice(answer.data)
                break;
            case "HUB_DISCONNECTED": break;
            default: break;
        }
    }
});
