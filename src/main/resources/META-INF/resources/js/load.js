var logsOn=false;
var lastString =0;
setInterval(
    function () {
        isConnect();
        inProgress()
        if (!connected && logsOn) getLogs();
    }, 100);

function inProgress() { // проверяем идет ли загрузка скетча
    $.ajax({
        url: "/sketch",
        type: "POST",
        data: {'action': 'inProgress'},
        success: function (resp) {
            logsOn= resp.entity;

        }
    });
}
function getSketches(){
    $.ajax({
        url: "/sketch",
        type: "POST",
        data: {'action': 'list'},
        success: function (resp) {
            if (resp.status == 200) {
                $("#fileField").empty();
                console.log(resp.entity);
                for(i=0;i<resp.entity.length;i++){
                    div = document.createElement("DIV");
                    div.setAttribute("class","files");
                    watch = document.createElement("Button");
                    watch.setAttribute("id","watch"+i);
                    watch.setAttribute("class","descBtn");
                    watch.setAttribute("name",resp.entity[i]);
                    watch.setAttribute("onclick", 'load(this.getAttribute("name"))');
                    div.innerHTML=resp.entity[i];
                    watch.innerHTML="Загрузить";
                    div.appendChild(watch);
                    $("#fileField").append(div);
                }
            }
        }
    });
}
function getLogs() {
     console.log("logss...");
    $.ajax({
        url: "/sketch",
        type: "POST",
        data: {'action': 'logs'},
        success: function (resp) {
            if (resp.status==200){
                console.log(resp.entity);
                for(i = lastString;i<resp.entity.length;i++){
                    $("#text").val( $("#text").val()+resp.entity[i]);
                }
            }
        }
    })
}
function load(name) {
    $("#text").val('');
    if (!inProgress()) {

        lastString = 0;
        var portName = $("#port").val();
        $.ajax({
            url: "/sketch",
            type: "POST",
            data: {'action': 'upload', 'name': name, 'port': portName},
            success: function (resp) {

            }
        });
        console.log("Loading::  " + name);
    }
}