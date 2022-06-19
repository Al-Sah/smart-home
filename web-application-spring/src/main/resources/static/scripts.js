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
    * Change settings of property
    */
    $('.btnChangeProp').on('click', ".btnChangeProp", function(){
        var parent_id = $(this).parent().parent().attr('id');
        console.log(parent_id);
    })
    /*
    * Show all devices information to screen
    */
    function showDevicesInfo() {
        let data ="<div class=\"album py-5 bg-light\"><div class=\"container\"><div class=\"row\">";
        deviceList.forEach(function (item) {
            switch (item.metadata.type) {
                case "SENSOR":
                    if(item.error === undefined){
                        if(item.state === undefined || !item.state.active){
                            data+=`<div class = 'col-md-4' id='${item.metadata.id}'><div class="card mb-4 box-shadow"><img class="card-img-top"  src="img/topImg.png" alt="Card image cap"><div class="card-body"><p>Device name: ${item.metadata.name}</p><p>Status unknown</p></div></div></div>`;
                        }
                        else{
                            let lastUpdate = new Date(item.state.lastUpdate).toLocaleString();
                            data+=`<div class = 'col-md-4' id='${item.metadata.id}'><div class="card mb-4 box-shadow"><img class="card-img-top"   src="img/topImg.png" alt="Card image cap"><div class="card-body"><p>Device name: ${item.metadata.name}</p><ul>`;
                            item.metadata.components.forEach(function (item1) {
                                data+=`<li id="${item1.id}">${item1.mainProperty.description} ${item1.mainProperty.value} ${item1.mainProperty.unit} <button type="button" class="btn btn-sm btn-outline-secondary btnChangeProp">Edit</button></li>`;
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
    function showHubsInfo() {
        let data ="";
        hubList.forEach(function (item) {
            if(item.active){
                let lastUpdate = new Date(item.state.lastUpdate).toLocaleString();
                data+=`<div id=${item.id}><p>Hub name: ${item.id}</p><p>Last update: ${lastUpdate}</p></div>`
            }
        });
        $('#data').append(data);
    }
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
                item.metadata[0].components.mainProperty.value = answer.data.components.value;
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
            deviceList.forEach(function (item) {
                if(item.metadata.id == answer.message.device){
                    item.metadata.components.forEach(function (item1){
                        if(item1.id == answer.message.component){
                            item1.value = answer.message.value;
                        }
                    });
                }
            });
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
    * When receive a message from server
    * */
    ws.onmessage = function (event) {
        let answer = JSON.parse(event.data);
        switch (answer.action) {
            case "START":
                onStart(answer);
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
        showDevicesInfo();
        showHubsInfo();
    }
});
