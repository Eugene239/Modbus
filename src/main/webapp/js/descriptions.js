var desc;
var descNames;
function initDescription() {
    getDescList(function () {
        $("#selectDesc").empty();
        for (i = 0; i < descNames.length; i++) {
            option = document.createElement("option");
            option.innerHTML = descNames[i];
            $("#selectDesc").append(option);
        }
    });
}
function getDescList(callback) {
    $.ajax({
        url: "/description",
        type: "POST",
        data: {'action': 'getList'},
        success: function (resp) {
            if (resp.status == 200) {
                console.log(resp.entity);
                descNames = resp.entity;
                callback();
            }
        }
    });
}
function getList() {
    $.ajax({
        url: "/description",
        type: "POST",
        data: {'action': 'getList'},
        success: function (resp) {
            if (resp.status == 200) {
                $("#selectDesc").empty();
                console.log(resp.entity);
                for (i = 0; i < resp.entity.length; i++) {
                    option = document.createElement("option");
                    option.innerHTML = resp.entity[i];
                    $("#selectDesc").append(option);
                }
            }
        }
    });
}
function getDesc(name) {
    console.log("get: " + name);
    $.ajax({
        url: "/description",
        type: "POST",
        data: {'action': 'get', 'name': name},
        success: function (data) {
            console.log(data);
            if (data.status == 200) {
                console.log(data.entity[0]);
                desc = data.entity;
            }
        }
    });
}
function getRawText(name) {
    console.log("watch: " + name);
    $.ajax({
        url: "/description",
        type: "POST",
        data: {'action': 'raw', 'name': name},
        success: function (data) {
            //console.log(data);
            $("#textarea").empty();
            var find = '},';
            var re = new RegExp(find, 'g');
            str = data.entity.replace(re, '},\n');
            $("#textarea").val(str);
        }
    });
}
function getRawTextCallBack(name, callback) {
    console.log("watch: " + name);
    $.ajax({
        url: "/description",
        type: "POST",
        data: {'action': 'raw', 'name': name},
        success: function (data) {
            callback(data);
        }
    });
}
$("#watch").click(function () {
    getRawText($("#selectDesc").val());
});
$("#delete").click(function () {
    deleteDesc($("#selectDesc").val());
    //getRawText($("#selectDesc").val());
});
function deleteDesc(name) {
    console.log("delete: " + name);
    var isDel = confirm("Вы действительно хотите удалить описание?");
    if (isDel) {
        $('#text').val('');
        $.ajax({
            url: "/description",
            type: "POST",
            data: {'action': 'delete', 'name': name},
            success: function (data) {
                //console.log(data);
                $("#textarea").val('');
                getList();
            }
        });
    }
}
function createDesc(name) {
    console.log("create: " + name);
    // var isDel= confirm("Вы действительно хотите удалть описание?");
    //if (isDel) {
    $("#newDescInput").val('');
    $.ajax({
        url: "/description",
        type: "POST",
        data: {'action': 'create', 'name': name},
        success: function (data) {
            //console.log(data);
            getList();
        }
    });
    // }
}
$("#createDesc").click(function () {
    if ($("#newDescInput").val() != '') createDesc($("#newDescInput").val());
});
$("#saveChanges").click(function () {
    editDesc($("#selectDesc").val(), $("#textarea").val());
});
function editDesc(name, text) {
    console.log("edit: " + name);
    $.ajax({
        url: "/description",
        type: "POST",
        data: {'action': 'edit', 'name': name, 'text': text},
        success: function (data) {

        }
    });
}
$("#selectDesc").on('input', function () {
    getRawText($("#selectDesc").val());
});