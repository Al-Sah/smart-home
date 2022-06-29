
$(document).ready(function () {

    // setup form
    let deviceId = new URLSearchParams(window.location.search).get('id');
    let deviceType = "device";
    $('#object-id-input').val(deviceId);
    $('#object-type-input').val(deviceType);


    $('#get-history-button').on("click",function () {
        // FIXME
        let timeFrom = new Date($('#time-from-input').val()).getTime();
        let timeTo = new Date($('#time-to-input').val()).getTime();

        if((timeFrom && timeTo) !== undefined){
            getHistory($('#object-id-input').val(), $('#object-type-input').val(), timeFrom, timeTo);
        }
    })
    getDeviceStructure()
});


let state = {
    'querySet': "",
    'page' : 1,
    'rows': 10,
    'window': 5,
}

function pagination(querySet, page, rows) {

    let trimStart = (page - 1) * rows
    let trimEnd = trimStart + rows
    return {
        'querySet': querySet.slice(trimStart, trimEnd),
        'pages': Math.round(querySet.length / rows),
    }
}

function pageButtons(pages) {
    let wrapper = document.getElementById('pagination_wrapper')
    wrapper.innerHTML = ``;

    console.log('Pages:', pages)

    let maxLeft = (state.page - Math.floor(state.window / 2))
    let maxRight = (state.page + Math.floor(state.window / 2))

    if (maxLeft < 1) {
        maxLeft = 1
        maxRight = state.window
    }

    if (maxRight > pages) {
        maxLeft = pages - (state.window - 1)

        if (maxLeft < 1) {
            maxLeft = 1
        }
        maxRight = pages
    }



    for (let page = maxLeft; page <= maxRight; page++) {
        wrapper.innerHTML += `<button value=${page} class="page btn btn-sm btn-info">${page}</button>`
    }

    if (state.page !== 1) {
        wrapper.innerHTML = `<button value=${1} class="page btn btn-sm btn-info">&#171; First</button>` + wrapper.innerHTML
    }

    if (state.page !== pages) {
        wrapper.innerHTML += `<button value=${pages} class="page btn btn-sm btn-info">Last &#187;</button>`
    }

    $('.page').on('click', function() {
        $('#result_table').empty()
        state.page = Number($(this).val())
        buildTable()
    })
}

function buildTable() {
    let table = $('#result_table')
    table.empty();
    let data = pagination(state.querySet, state.page, state.rows)
    let myList = data.querySet;
    myList.forEach(function (item) {
        let lastDate = new Date(item.ts).toLocaleString()
        let row = `<tr>
                  <td>${item.component}</td>
                  <td>${item.hub}</td>
                  <td>${item.property}</td>
                  <td>${Math.ceil(item.value)}</td>
                  <td>${lastDate}</td>
                  </tr>`
        table.append(row)
    });
    pageButtons(data.pages)
}

function getHistory(id, type, TimeFrom, TimeTo) {
    $.ajax({
        url:'http://localhost:8080/history',
        type: 'GET',
        cache: false,
        data:{'type' : type, 'id' : id, 'from': TimeFrom, 'to': TimeTo},
        dataType: 'json',
        beforeSend: function() {
            $('#get-history-button').prop('disabled',true);
        },
        success: function(data) {
            state.querySet = data;
            $('.table').prop('hidden', false);
            buildTable()
            $('#get-history-button').prop('disabled',false);
        },
        error: function () {
            console.log('error');
            $('#get-history-button').prop('disabled',false);
        }
    });
}



function getDeviceStructure(){

    let deviceId = new URLSearchParams(window.location.search).get('id');
    if(deviceId === null){
        console.error("device id is undefined");
        return;
    }

    let device = $('#device-structure')[0];
    $.ajax({
        url:'http://localhost:8083/device',
        type: 'GET',
        cache: false,
        data: {'id' : deviceId},
        crossDomain: true,
        beforeSend: ()=> device.innerHTML = "<p> Loading ... </p>",
        success: (item)=> device.innerHTML = printDeviceStructure(item.metadata),
        error:  ()=> device.innerHTML = "<p> Failed to load structure </p>",
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