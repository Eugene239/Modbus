
function updateCoils(){
    $.ajax({
        url: "/coils",
        type: "POST",
        data: {'offset':'0', 'size':'120'},
        success:
            function(resp){
                var s="";
                $("#coils").empty();
               // var div = document.createElement("DIV");
                for (i=0; i<resp.length;i++){
                    var b = document.createElement("BUTTON");
                    b.setAttribute("id","coil"+resp[i].id);

                    b.setAttribute("coilId",resp[i].id);
                    b.setAttribute("value",resp[i].value);
                    if (resp[i].value) b.setAttribute("class","coilButton On");
                    else b.setAttribute("class","coilButton Off");
                    b.innerHTML=resp[i].id;
                    b.onclick= function(){writeCoil(this.getAttribute("coilId"),this.getAttribute("value")); updateCoils();};
                  // div.appendChild(b);
                    $("#coils").append(b);
                }

            },
        error: function(data){
            console.log(data);
            connected=false;
        },
    });
}
function writeCoil(id, value){
   // alert(!value);
    if (connected) {
        console.log(id + " " + !JSON.parse(value));
        $.ajax({
            url: "/setCoil",
            type: "POST",
            data: {'id': id, 'value': !JSON.parse(value)}
        });
        //update();
    }
}
function writeRegister(id, value){
    // alert(!value);
    console.log(id+" "+value);
    $.ajax({
        url: "/setHreg",
        type: "POST",
        data: {'id': id, 'value': value}
    });
   // update();
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
               // console.log(resp.length);
            },
        error: function(data){

        },
    });
}
