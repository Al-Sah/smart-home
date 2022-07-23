/**
 * GET id history search
 * */
function getDeviceID() {
    return window.location.search.replace("?","");
}
function showHistory(data) {
    console.log(data);
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
            console.log(typeof (data));
            $('#btn_getHistory').prop('disabled',false);
            return data;
        },
        error: function () {
            console.log('error');
            $('#btn_getHistory').prop('disabled',false);
            return false;
        }
    });
}
$(document).ready(function () {
    let deviceId = getDeviceID();
    let deviceType = "device";
    let timeTo = $('#timeTo_input').val();
    $('#object-id-input').val(deviceId);
    $('#object-type-input').val(deviceType);
    $('#btn_getHistory').on("click",function () {
        let timeFrom = new Date($('#timeFrom_input').val()).getTime();
        let timeTo = new Date($('#timeTo_input').val()).getTime();
        if((timeFrom && timeTo) !== undefined){
            let data = getHistory(deviceId, deviceType, timeFrom, timeTo);
            if(data){
                showHistory(data);
            }
        }
    })
});