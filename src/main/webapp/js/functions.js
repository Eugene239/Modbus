var x;
var y;
var offset =0;
var hregSize=10;
function updateCoils(offset, size){
    $.ajax({
        url: "/coils",
        type: "POST",
        async:true,
        data: {'offset':offset, 'size':size},
        success:
            function(resp){
                var s="";
                //$("#coils").empty();
                for (i=0; i<resp.length;i++){
                    var b = document.getElementById("coil"+ (+offset + +i));
                    //b.innerHTML=+offset+ +i;
                    b.setAttribute("value",resp[i].value);
                    if (resp[i].value){
                        b.setAttribute("class","coilButton On");
                    }else {
                        b.setAttribute("class","coilButton Off");
                    }
                    //console.log("coil"+ +offset+ +i);
                   // $("#coil"+ +offset+ +i).val()
                    //if(i%10==0)  $("#coils").append("<br>");
                  //  var b = document.createElement("BUTTON");
                  //  b.setAttribute("id","coil"+resp[i].id);
                  //  b.setAttribute("coilId",resp[i].id);
                   // b.setAttribute("value",resp[i].value);
                  //  if (resp[i].value) b.setAttribute("class","coilButton On");
                  //  else b.setAttribute("class","coilButton Off");
                  //  b.innerHTML=resp[i].id;
                  //  b.onclick= function(){writeCoil(this.getAttribute("coilId"),this.getAttribute("value")); updateCoils();};
                  //// div.appendChild(b);
                  //  $("#coils").append(b);
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
            data: {'id': id, 'value': !JSON.parse(value)},
            success:function(data){
                //console.log(data);
                if (data.status==200) {
                    updateCoils(x, y);
                } else {
                  alert(data.entity);
                }
            }
        });

    }
}
function writeRegister(id, value){
    // alert(!value);
    console.log(id+" "+value);
    $.ajax({
        url: "/setHreg",
        type: "POST",
        async:false,
        data: {'id': id, 'value': value},
        success: updateHregs()
    });
   // update();
}
function updateHregs(x,y){
    offset=x;
    hregSize=y;
    $.ajax({
        url: "/hregs",
        type: "POST",
        data: {'offset':x, 'size':y},
        success:
            function(resp){
                if (resp.status!=200) {
                    console.log(resp);
                    isConnect() ;
                    return;
                }
                for(i=0;i<resp.entity.length;i++){
                   // console.log("#hregrange"+(+resp.entity[i].id+ +offset));
                    $("#hregrange"+(+resp.entity[i].id+ +offset)).val(resp.entity[i].value);
                    $("#hregval"+(+resp.entity[i].id+ +offset)).val(resp.entity[i].value);
                }
            },
        error: function(data){
           // isConnect();
        },
    });
}




