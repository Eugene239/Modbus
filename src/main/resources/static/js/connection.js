var connected = false;
function isConnect(){
    $.ajax({
        url: "/connect",
        type: "POST",
        data:{'action':"status"},
        success: function (resp) {
            if (!JSON.parse(resp.entity)){
                connected = false;
              //  parameters("RTU","create");
            }
            else {
                connected = true;
            }
            console.log(resp.entity);
        }
    });
}
function parameters(type , action, portName){
    $.ajax({
        url: "/parameters",
        type: "POST",
        data:{'type':type, 'action':action,'portName':portName},
        success: function (resp) {
            console.log("parameters: type: "+type+" action:"+action+"--->" + resp.status);
            if (action=='create' && resp.status==200){
                connectToMOdbus("connect");
            }
            //$("#port").empty();
            //for(i = 0;i<resp.entity.length;i++){
            //    $("#port").append("<option>"+ resp.entity[i]+"</option>")
            //}
            //console.log(resp.entity.length);
        }
    });
}
function connectToMOdbus(action){
    //action
    //  status, connect

    $.ajax({
        url: "/connect",
        type: "POST",
        data:{'action':action},
        success: function (resp) {
            console.log('connectToMOdbus:' + action+"--->"+resp.status);
            if (action=='connect' && resp.status==200){
                alert("connected to"+resp.entity.serialPort);
                connected=true;
            }
        }
    });
}
function mainOnclick(){
    console.log( $("#port :selected").text());
  parameters("RTU","create", $("#port :selected").text());
}

function getPorts(){
    $.ajax({
        url: "/port",
        type: "POST",
        data:{'reg':"list"},
        success: function (resp) {
            for(i = 0;i<resp.entity.length;i++){
                $("#port").append("<option>"+ resp.entity[i]+"</option>");
                console.log(resp.entity);
            }
            //console.log(resp.entity.length);
        }
    });
}