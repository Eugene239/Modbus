var lastString =0;
function initUpload(){
    getPorts('upload');
    getSketches();
}

setInterval(
    function () {
        inProgress();
    }, 500);

function inProgress() { // проверяем идет ли загрузка скетча
    $.ajax({
        url: "/sketch",
        type: "POST",
        data: {'action': 'inProgress'},
        success: function (resp) {
            //if (resp.entity){
                getLogs();
            //}
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
                    $("#textarea").val( $("#textarea").val()+resp.entity[i]);
                    $('#textarea').scrollTop($('#textarea')[0].scrollHeight);
                    lastString=resp.entity.length;
                }
            }
        }
    })
}
function getSketches() {
    $.ajax({
        url: "/sketch",
        type: "POST",
        data: {'action': 'list'},
        success: function (resp) {
        console.log(resp.entity);
            $("#code").empty();
            for(i=0;i<resp.entity.length;i++){
                option = document.createElement("option");
                option.innerHTML = resp.entity[i];
                $("#code").append(option);
            }
        }
    });
}
function load(name) {
    var portName = $("#port").val();
    $.ajax({
        url: "/sketch",
        type: "POST",
        data: {'action': 'upload', 'name': name, 'port': portName, 'plata':$("#plata").val()},
        success: function (resp) {
            //getLogs();
        }
    });
    console.log("Loading::  " + name);
}
$("#uploadBtn").click(function(){
    $('#textarea').val('');
    lastString = 0;
    load($("#code").val());
});
$("#clear").click(function(){
    $("#textarea").val('');
    $.ajax({
        url: "/sketch",
        type: "POST",
        data: {'action': 'clear'},
        success: function (resp) {
            //getLogs();
        }
    });
});
$("#stopUpload").click(function(){
    $.ajax({
        url: "/sketch",
        type: "POST",
        data: {'action': 'stop'},
        success: function (resp) {
            //getLogs();
        }
    });
});
$("#addSketch").click(function () {
    var file = $("#inputFile").val();
    if (file!==''){
        var file_data = $('#inputFile').prop('files')[0];
        var form_data = new FormData();
        form_data.append('file', file_data);
        $("#sketchLoader").show();
        $.ajax({
            cache: false,
            contentType: false,
            processData: false,
            url: "/sketch/load",
            type: "POST",
            data: form_data,
            success: function (resp) {
                //getLogs();
                $("#sketchLoader").hide();
                initUpload();
                //console.log(resp);
            }
        });
    }
});
$("#deleteBtn").click(function () {
    var isDel = confirm("Вы действительно хотите удалить скетч?");
    if (isDel) {
        $.ajax({
            url: "/sketch/delete",
            type: "POST",
            data: {'filename': $("#code").val()},
            success: function (resp) {
                initUpload();
            }
        });
    }
});