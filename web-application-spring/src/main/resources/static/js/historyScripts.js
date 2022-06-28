
let state = {
    'querySet': "",
    'page' : 1,
    'rows': 10,
    'window': 5,
}

function showHistory() {
    $('.table').prop('hidden', false);
    buildTable()
}

function pagination(querySet, page, rows) {

    let trimStart = (page - 1) * rows
    let trimEnd = trimStart + rows

    let trimmedData = querySet.slice(trimStart, trimEnd)

    let pages = Math.round(querySet.length / rows);

    return {
        'querySet': trimmedData,
        'pages': pages,
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

    if (state.page != 1) {
        wrapper.innerHTML = `<button value=${1} class="page btn btn-sm btn-info">&#171; First</button>` + wrapper.innerHTML
    }

    if (state.page != pages) {
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
    myList.forEach(function (item, key) {
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
            $('#btn_getHistory').prop('disabled',true);
        },
        success: function(data) {
            state.querySet = data;
            showHistory();
            $('#btn_getHistory').prop('disabled',false);
        },
        error: function () {
            console.log('error');
            $('#btn_getHistory').prop('disabled',false);
        }
    });
}

$(document).ready(function () {

    let deviceId = new URLSearchParams(window.location.search).get('id');
    let deviceType = "device";

    $('#object-id-input').val(deviceId);
    $('#object-type-input').val(deviceType);
    $('#btn_getHistory').on("click",function () {
        let timeFrom = new Date($('#timeFrom_input').val()).getTime();
        let timeTo = new Date($('#timeTo_input').val()).getTime();
        if((timeFrom && timeTo) !== undefined){
            getHistory(deviceId, deviceType, timeFrom, timeTo);
        }
    })
    getDeviceStructure()
});


function getDeviceStructure(){

    let deviceId = new URLSearchParams(window.location.search).get('id');
    if(deviceId === null){
        console.error("device id is undefined");
        return;
    }

    let device = $('#deviceStructure')[0];
    $.ajax({
        url:'http://localhost:8083/device',
        type: 'GET',
        cache: false,
        data: {'id' : deviceId},
        crossDomain: true,
        beforeSend: ()=> device.innerHTML = "<p> Loading ... </p>",
        success: ()=> device.innerHTML = "<p> Loaded </p>",
        error:  ()=> device.innerHTML = "<p> Failed to load structure </p>",
    });
}