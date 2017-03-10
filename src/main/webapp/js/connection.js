var connected = false;
function isConnect(){
    $.ajax({
        url: "/connect",
        type: "POST",
        data:{'action':"status"},
        success: function (resp) {
            connected =JSON.parse(resp.entity);
            if (connected) {
                $("#status").text("Подключен");
            }
            else  $("#status").text("Не подключен");
        }
    });
}
function parameters(type , action, portName, baudRate, dataBits,stopBits, echo, parity){
    console.log("["+portName+" "+ baudRate+" "+ dataBits+" "+stopBits+" "+echo+" "+parity+"]");
    $.ajax({
        url: "/parameters",
        type: "POST",
        data:{'type':type, 'action':action,'portName':portName, 'baudRate':baudRate, 'dataBits':dataBits, 'stopBits':stopBits,'echo':echo!=0, 'parity':parity},
        success: function (resp) {
            console.log("parameters: type: "+type+" action:"+action+"--->" + resp.status);
            if (action=='create' && resp.status==200){
                connectToMOdbus("connect");
            }

        },
        error: function(resp){
            console.log(resp);
        }
    });
}
function getParameters(){
    $.ajax({
        url: "/parameters",
        type: "POST",
        data:{},
        success: function (resp) {
            if (resp.entity!=null){
                console.log(resp.entity);
            }

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
                alert("connected to "+ $("#port :selected").text());
                connected=true;
                $("#status").innerHTML="Подключен";
            } else {
                alert("Не удалось подключиться");
            }

        },
        error: function(resp){
            console.log(resp);

        }
    });
}
function mainConnect(){
 //   console.log( $("#port :selected").text());
    parameters("RTU","create", $("#port :selected").val(), $("#baudRate").val(), $("#dataBits").val(), $("#stopBits").val(),$("#echo").val(),$("#parity").val());
}
function mainDisconnect(){
    disconnect();
    connected = false;
    $("#status").innerHTML="Не подключен";
}

function getPorts(){
    $.ajax({
        url: "/port",
        type: "POST",
        data:{'reg':"list"},
        success: function (resp) {
            for(i = 0;i<resp.entity.length;i++){
                $("#port").append("<option>"+ resp.entity[i]+"</option>");
              //  console.log(resp.entity);
            }
        }
    });
}
function disconnect(){
    $.ajax({
        url: "/connect",
        type: "POST",
        data:{'action':'disconnect'},
        success: function (resp) {
           // console.log(action+"--->"+resp.status);
            if (action=='connect' && resp.status==200){
                connected = false;
            }
        }
    });
}
function update() {
    if (window.connected) {
       // console.log(connected);
        updateCoils();
        updateHregs();
    }
}
function mainInit(){
    getPorts();
    isConnect();
    getParameters();
}

