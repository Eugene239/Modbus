var desc;
setInterval(
    function () {
        x = $("#above").val();
        y = $("#below").val();
        isConnect();
        if (connected && y <= 500) updateCoils(x, y);
    }, 500);

function coilInit() {
    isConnect();
    $("#above").val(0);
    $("#below").val(100);
    updateCoilDiv();
    getList();
}
function updateCoilDiv() {
    offset = $("#above").val();
    hregSize = $("#below").val();
    $("#coils").empty();
        console.log(getDescText(0));
        for (i = 0; i < hregSize; i++) {
            //if(i%10==0)  $("#coils").append("<br>");
            var b = document.createElement("BUTTON");
            b.setAttribute("id", "coil" + (+i + +offset));
            b.setAttribute("coilId", +i + +offset);
            b.setAttribute("value", false);
            b.setAttribute("class", "coilButton");
            //else b.setAttribute("class", "coilButton Off");
            var text = getDescText(+i + +offset);
            //if (text=="" && text!=0) {
                if (text==""){
                    console.log(">>"+ text);
                    b.style.display='none';
                }
            //    b.setAttribute("visability","hidden");
           // }
            b.innerHTML =text;
            b.onclick = function () {
                writeCoil(this.getAttribute("coilId"), this.getAttribute("value"));
                updateCoils();
            };
            // div.appendChild(b);
            $("#coils").append(b);
        }

}
function getList() {
    $.ajax({
        url: "/description",
        type: "POST",
        data: {'action': 'getList'},
        success: function (resp) {
            if (resp.status == 200) {
                for (i = 0; i < resp.entity.length; i++) {
                    op = document.createElement("option");
                    op.innerHTML=resp.entity[i].split(".")[0];
                    $("#selectDesc").append(op);
                }

            } else {
                console.log(resp);
            }
        },
        error: function (data) {
            // isConnect();
        },
    });
}

function getDesc(name){
    if (name==".json") {
        desc = undefined;
        updateCoilDiv();
        return;
    }
    console.log("get: "+name);
    $.ajax({
        url: "/description",
        type: "POST",
        data: {'action': 'get', 'name': name},
        success:function(data){
            //console.log(data);
            if (data.status==200) {
                //console.log(data.entity[0]);
                desc = data.entity;
                updateCoilDiv();
            }
        }
    });
}
function getDescText(id){
    if (desc==undefined)  return id+"";
    else {
       // console.log(desc.length);
        for(var j=0;j<desc.length;j++){
            if (desc[j].id==id){
                return desc[j].text;
            }
        }
    }
    return id+"";
}