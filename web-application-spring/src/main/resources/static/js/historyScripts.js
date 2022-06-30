
$(document).ready(function () {

    // setup form
    // TODO set default time
    let deviceId = new URLSearchParams(window.location.search).get('id');
    let deviceType = "device";
    $('#object-id-input').val(deviceId);
    $('#object-type-input').val(deviceType);

    $('#get-history-button').on("click",function () {
        // TODO check (can be null or 'NaN')
        let timeFrom = new Date($('#time-from-input').val()).getTime();
        let timeTo = new Date($('#time-to-input').val()).getTime();

        // TODO if > timeFrom > timeTo  ERROR !!
        if((timeFrom && timeTo) !== undefined){
            getHistory($('#object-id-input').val(), $('#object-type-input').val(), timeFrom, timeTo);
        }
    })
    getDeviceStructure()
});

function getHistory(id, type, TimeFrom, TimeTo) {

    let button = $('#get-history-button');
    let dataContainer = $("#data-container");
    let paginationContainer = $('#pagination');
    // TODO 'from' and 'to' are optional !!!
    $.ajax({
        url:'http://localhost:8080/history',
        type: 'GET',
        cache: false,
        data:{'type' : type, 'id' : id, 'from': TimeFrom, 'to': TimeTo},
        dataType: 'json',
        beforeSend: function() {
            dataContainer.html('');
            paginationContainer.html('');
            button.prop('disabled',true);
        },
        success: function (data) {
            handleHistoryMessages(data)
            button.prop('disabled',false);
        },
        error: function () {
            dataContainer.html(`<p> Failed to get messages </p>`);
            button.prop('disabled',false);
        }
    });
}

function handleHistoryMessages(itemsList){

    let dataContainer = $("#data-container");
    let paginationContainer = $('#pagination');

    if(itemsList == null || itemsList.length === 0){
        //TODO check (can be null or 'NaN')
        let from = new Date($('#time-from-input').val()).toLocaleDateString();
        let to = new Date($('#time-to-input').val()).toLocaleDateString();
        dataContainer.html(`
            <div>
                <p> No messages found</p>
                <p> Requested period: ${from} - ${to} </p>
            </div>
        `);
        paginationContainer.html('');
        return;
    }

    paginationContainer.pagination({
        dataSource: itemsList,
        pageSize: 10,
        callback: (items) => dataContainer.html(generateMessagesList(items))
    })
}

function generateMessagesList(data){
    let items = '';
    data.forEach( function (message) {
        items+=`
            <div class="list-group-item">
                <p  class="test-primary"> Message: ${new Date(message.ts).toLocaleDateString()} </p>
                <p> Value of property '${message.property}' changed to '${message.value}' </p>
                <small class="test-secondary"> ${message.device} | ${message.component} </small>
            </div>`
    })
    return `<ul class="list-group"> ${items} </ul>`
}


function getDeviceStructure(){

    let deviceId = new URLSearchParams(window.location.search).get('id');
    if(deviceId === null){
        console.error("device id is undefined");
        return;
    }

    let device = $('#device-structure');
    $.ajax({
        url:'http://localhost:8083/device',
        type: 'GET',
        cache: false,
        data: {'id' : deviceId},
        crossDomain: true,
        beforeSend: ()=> device.html("<p> Loading ... </p>"),
        success: (item)=> device.html(printDeviceStructure(item.metadata)),
        error:  ()=> device.html("<p> Failed to load structure </p>"),
    });
}

function printDeviceStructure(structure){

    return `
        <div class="list-group shadow-sm p-3 mb-5 bg-white rounded">
            <a href="#" class="list-group-item list-group-item-action border border-light rounded" onclick="updateFormInputs('${structure.id}','device')">
                <p class="text-primary text-center"> 
                     ${structure.name} <br> <small class="text-secondary"> ${structure.id}</small>
                </p>
                <div class="list-group">
                    ${generateComponentsList(structure.components)}
                </div>
            </a>
        </div>
    `;
}

function generateComponentsList(components){

    let res = ``;
    components.forEach(function (component) {
        let mainProperty = component.mainProperty;
        res+=`
        <a href="#" class="list-group-item list-group-item-info list-group-item-action mb-1" onclick="updateFormInputs('${component.id}','component')"> 
            <div class="mb-1">
                <p class="text-primary"> 
                    ${mainProperty.name};  unit: '${mainProperty.unit}' <br> 
                    <small> ${mainProperty.description} </small> 
                </p>
                ${generatePropertiesList(component.constProperties, 'constant')}
                ${generatePropertiesList(component.writableProperties, 'writable')}
            </div>
        </a>`;
    });
    return res;
}

function updateFormInputs(id, type){
    $('#object-id-input').val(id);
    $('#object-type-input').val(type);
}

function generatePropertiesList(properties, type){

    if(properties === null || properties.length === 0){
        return `<p>No ${type} properties specified</p>`;
    }

    let listItems = ``;
    properties.forEach(function (property) {
        listItems += `
            <li class="list-group-item list-group-item-secondary">
                <p>${property.name} '${property.unit}' <br> <small> ${property.description} </small></p>
            </li>`
    })
    return `<p> ${type} properties: </p><ul class="list-group">${listItems}</ul>`
}