
function updateCoils(offset, size){
    $.ajax({
        url: "/coils",
        type: "POST",
        async:true,
        data: {'offset':offset, 'size':size},
        success:
            function(resp){
                var s="";
                $("#coils").empty();
               // var div = document.createElement("DIV");
                for (i=0; i<resp.length;i++){
                    if(i%10==0)  $("#coils").append("<br>");
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
        async:true,
        data: {'id': id, 'value': value},
        success: updateHregs()
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


function color() {
    console.log(hexToRgb($("#color").val()).r);
    writeRegister(0, hexToRgb($("#color").val()).r);
    writeRegister(1, hexToRgb($("#color").val()).g);
    writeRegister(2, hexToRgb($("#color").val()).b);
    console.log(hexToRgb($("#color").val()).g);
    console.log(hexToRgb($("#color").val()).b);
}
function range(ent) {
    //  console.log(ent);
    //  writeRegister(0,ent);
}
function hexToRgb(hex) {
    // Expand shorthand form (e.g. "03F") to full form (e.g. "0033FF")
    var shorthandRegex = /^#?([a-f\d])([a-f\d])([a-f\d])$/i;
    hex = hex.replace(shorthandRegex, function (m, r, g, b) {
        return r + r + g + g + b + b;
    });

    var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    return result ? {
        r: parseInt(result[1], 16),
        g: parseInt(result[2], 16),
        b: parseInt(result[3], 16)
    } : null;
}

