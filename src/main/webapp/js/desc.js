var desc;
function getDesc(name){
    console.log("get: "+name);
    $.ajax({
        url: "/description",
        type: "POST",
        data: {'action': 'get', 'name': name},
        success:function(data){
            //console.log(data);
            if (data.status==200) {
                console.log(data.entity[0]);
                desc = data.entity;
            }
        }
    });
}
function getList(){
    $.ajax({
        url: "/description",
        type: "POST",
        data: {'action':'getList'},
        success:
            function(resp){
                if (resp.status==200) {
                    $("#fileField").empty();
                    for(i=0;i<resp.entity.length;i++){
                       // console.log(resp.entity[i]);
                        div = document.createElement("DIV");
                        div.setAttribute("class","files");
                        del = document.createElement("Button");
                        watch = document.createElement("Button");
                        del.setAttribute("id","del"+i);
                        watch.setAttribute("id","watch"+i);
                        del.setAttribute("class","descBtn");
                        watch.setAttribute("class","descBtn");
                        watch.setAttribute("name",resp.entity[i]);
                        del.setAttribute("name",resp.entity[i]);
                        watch.setAttribute("onclick", 'getRawText(this.getAttribute("name"))');
                        del.setAttribute("onclick", 'deleteDesc(this.getAttribute("name"))');
                        div.innerHTML=resp.entity[i].split(".")[0];
                        del.innerHTML="Удалить";
                        watch.innerHTML="Смотреть";

                        div.appendChild(del);
                        div.appendChild(watch);
                        $("#fileField").append(div);
                    }

                    //isConnect() ;
                    //return;
                } else {
                    console.log(resp);
                }
            },
        error: function(data){
            // isConnect();
        },
    });
}
function deleteDesc(name){
    console.log("watch: "+name);
    var isDel= confirm("Вы действительно хотите удалть описание?");
    if (isDel) {
        $('#text').val('');
        $.ajax({
            url: "/description",
            type: "POST",
            data: {'action': 'delete', 'name': name},
            success: function (data) {
                //console.log(data);
                getList();
            }
        });
    }
}

function getRawText(name){
    console.log("watch: "+name);
    $('#text').val('');
    text = document.getElementById("text");
    text.setAttribute("file",name);
    //console.log(text.getAttribute("file"));
    //$("#text").attr("file",name);
    $.ajax({
        url: "/description",
        type: "POST",
        data: {'action': 'raw', 'name': name},
        success:function(data){
            //console.log(data);
            $("#text").empty();
            var find = '},';
            var re = new RegExp(find, 'g');
            str=data.entity.replace(re, '},\n');
           // var s =data.entity.replace(",",",69");
            $("#text").val(str);
        }
    });
}