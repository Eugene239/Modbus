
function updateCoils(){
    $.ajax({
        url: "/coils",
        type: "POST",
        data: {'offset':'0', 'size':'40'},
        success:
            function(resp){
                var s="";
                $("#field").empty();
                var div = document.createElement("DIV");
                for (i=0; i<resp.length;i++){
                    if (i%10==0){
                       // console.log(i);
                        var div = document.createElement("DIV");
                        $("#field").append(div);
                    }
                    var b = document.createElement("BUTTON");
                    b.setAttribute("id","coil"+resp[i].id);
                    b.setAttribute("class","coilButton");
                    b.setAttribute("coilId",resp[i].id);
                    b.setAttribute("value",resp[i].value);
                    b.innerHTML=resp[i].id+" "+resp[i].value;
                    b.onclick= function(){writeCoil(this.getAttribute("coilId"),this.getAttribute("value")); update();};
                    div.appendChild(b);
                }
                $("#field").append(div);
            },
        error: function(data){

        },
    });
}
function writeCoil(id, value){
   // alert(!value);
    console.log(id+" "+!JSON.parse(value));
    $.ajax({
        url: "/setCoil",
        type: "POST",
        data: {'id': id, 'value': !JSON.parse(value)}
    });
    update();
}
function writeRegister(id, value){
    // alert(!value);
    console.log(id+" "+value);
    $.ajax({
        url: "/setHreg",
        type: "POST",
        data: {'id': id, 'value': value}
    });
    update();
}
function updateHregs(){
    $.ajax({
        url: "/hregs",
        type: "POST",
        data: {'offset':'0', 'size':'5'},
        success:
            function(resp){
                $("#hreg").empty();
                for(i=0;i<resp.length;i++){
                    var range = document.createElement("INPUT");
                    var rangeVal = document.createElement("SPAN");
                    range.setAttribute("type","range");
                    range.setAttribute("min","0");
                    range.setAttribute("max","255");
                    range.setAttribute("value",resp[i].value);
                    rangeVal.innerText=resp[i].id+" "+resp[i].value;
                    //rangeVal.html("id:"+ resp[i].id+" "+resp[i].value);
                    $("#hreg").append(range);
                    $("#hreg").append(rangeVal);
                }
                console.log(resp.length);
            },
        error: function(data){

        },
    });
}

