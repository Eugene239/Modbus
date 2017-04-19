var reading = false;

setInterval(
    function () {
        isMonitor();
        if (reading) getSerial();
    }, 500);

function isMonitor(){
    $.ajax({
        url: "/serial",
        type: "POST",
        data: {'action': 'status'},
        success: function (resp) {
            //console.log(resp);
            reading =resp.entity;
            if (reading){
                $("#alert-danger").hide();
                $("#alert-success").show();
                $("#alert-warning").hide();
            } else {
                $("#alert-danger").hide();
                $("#alert-success").hide();
                $("#alert-warning").show();
            }
        }
    });
}
function initMonitor(){
    getPorts("monitor");
    //ports.init("monitor");
}

function connect(){
    $("#text").empty();
    $.ajax({
        url: "/serial",
        type: "POST",
        data: {'action': 'connect', 'port':$("#port").val(),'baudRate':$("#baudRate").val()},
        success: function (resp) {
            console.log("MONITOR::CONNECTED");
        }
    });
}
$("#monitorConnect").on("click",function(){
   disconnectModbus(function(){connect();}); //отрубаемся от модбаса и подрубаем монитор
});
$("#monitorDisconnect").on("click",function (){
   disconnect();
});
function disconnect(){
    $.ajax({
        url: "/serial",
        type: "POST",
        data: {'action': 'disconnect'},
        success: function (resp) {
            console.log("MONITOR::DISCONNECTED");
        }
    });
}
function getSerial(){
    $.ajax({
        url: "/serial",
        type: "POST",
        data: {'action': 'read'},
        success: function (resp) {
            if(resp.entity.length>0)  console.log(resp.entity);
            $("#text").val('');
            for(i=0;i<resp.entity.length;i++){
                $("#text").val($("#text").val()+resp.entity[i]);
            }
            $('#text').scrollTop($('#text')[0].scrollHeight);
        }
    });
}
window.onbeforeunload = function(){
    disconnect();
  //  alert('nooo');
};