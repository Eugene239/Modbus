/**
 * Created by Eugene on 28.03.2017.
 */
var parameters;
var ports;
var connected = false;

function isConnect() {
    $.ajax({
        url: "/connect",
        type: "POST",
        data: {'action': "status"},
        success: function (resp) {
            connected = JSON.parse(resp.entity);
            if (connected) {
                $("#alert-danger").hide();
                $("#alert-success").show();
                $("#alert-warning").hide();
            }
            else {
                $("#alert-danger").hide();
                $("#alert-success").hide();
                $("#alert-warning").show();
            }
        }
    });
}

function getPorts(page) {
    $.ajax({
        url: "/port",
        type: "POST",
        data: {'reg': "list"},
        success: function (resp) {
            ports = new Ports();
            console.log("connect::getPorts:" + resp.entity);
            ports.list = resp.entity;
            if (page == "connection")  ports.initConnection();
            if (page == "monitor") ports.initMonitor();
            if (page == "upload") ports.initMonitor();
        }
    });
}
function getParameters() {
    $.ajax({
        url: "/parameters",
        type: "POST",
        data: {'action': 'get'},
        success: function (resp) {
            //console.log("connect::getParameters: " + JSON.stringify(resp));
            if (resp.entity != null) {
                console.log("connect::getParameters:" + JSON.stringify(resp.entity));
                entity = resp.entity;
                parameters = new Parameters();
                parameters.type = entity.type;
                parameters.mbId = entity.modbusId;
                parameters.portName = entity.serialPort;
                parameters.baudRate = entity.baudRate;
                parameters.dataBits = entity.dataBits;
                parameters.stopBits = entity.stopBits;
                parameters.echo = entity.echo + "";
                parameters.ip = entity.ip;
                parameters.ipPort = entity.ipPort;
                parameters.parity = entity.parity;
                $("#selectType").val(resp.entity.type);
                setType(resp.entity.type);
                parameters.initConnection();
            } else {
                console.log("connect::getParameters: NO PARAMETERS");
                setType("RTU");
            }

        }
    });
}
function Ports() {
    this.list;
    this.initConnection = function () {
        $("#port").empty();
        for (i = 0; i < this.list.length; i++) {
            option = document.createElement("option");
            option.setAttribute("id", this.list[i]);
            option.innerHTML = this.list[i];
            $("#port").append(option);
            if (i == 0) $("#port select").val(this.list[i]);
        }
        if (parameters) parameters.initConnection;

    };
    this.initMonitor = function () {
        $("#port").empty();
        for (i = 0; i < this.list.length; i++) {
            option = document.createElement("option");
            option.setAttribute("id", this.list[i]);
            option.innerHTML = this.list[i];
            $("#port").append(option);
            if (i == 0) $("#port select").val(this.list[i]);
        }
        if (parameters) parameters.initConnection;
    }
}
function Parameters() {
    this.type;
    this.mbId;
    this.portName;
    this.baudRate;
    this.echo;
    this.parity;
    this.dataBits;
    this.stopBits;
    this.ip;
    this.ipPort;

    this.initConnection = function () {
        if (this.type == "RTU") {
            $("#port").val(this.portName);
            $("#mbId").val(this.mbId);
            $("#baudrate").val(this.baudRate);
            $("#stop_bits").val(this.stopBits);
            $("#data_bits").val(this.dataBits);
            $("#echo").val(this.echo);
            $("#parity").val(this.parity);
        }
        if (this.type == "TCP") {
            $("#mbId").val(this.mbId);
            $("#IP").val(this.ip);
            $("#IPport").val(this.ipPort);
        }
    };
}
function setType(type) {
    if (type == "RTU") {
        $("#PORTROW").show();
        $("#BAUDRATEROW").show();
        $("#STOPBITSROW").show();
        $("#DATABITSROW").show();
        $("#ECHOROW").show();
        $("#PARITYROW").show();
        $("#IPROW").hide();
        $("#IPPORTROW").hide();
    }
    if (type == "TCP") {
        $("#PORTROW").hide();
        $("#BAUDRATEROW").hide();
        $("#STOPBITSROW").hide();
        $("#DATABITSROW").hide();
        $("#ECHOROW").hide();
        $("#PARITYROW").hide();
        $("#IPROW").show();
        $("#IPPORTROW").show();
    }
}

function setParameters(callback) {
    var mbId=  $("#mbId").val();
    $.ajax({
        url: "/parameters",
        type: "POST",
        data: {
            'type': $("#selectType").val(),
            'action': 'create',
            'modbusId': mbId,
            'portName': $("#port").val(),
            'baudRate': $("#baudrate").val(),
            'echo': $("#echo").val(),
            'parity': $("#parity").val(),
            'dataBits': $("#data_bits").val(),
            'stopBits': $("#stop_bits").val(),
            'ip': $("#IP").val(),
            'ipPort': $("#IPport").val()
        },
        success: function (resp) {
            console.log($("#mbId").val());
            console.log("connect::setParameters:" + resp.status + " " + JSON.stringify(resp.entity));
            callback();
            //return data = resp.entity;
        }
    });
}

function connectToModbus(action) {
    //action
    //  status, connect
    $.ajax({
        url: "/connect",
        type: "POST",
        data: {'action': action},
        success: function (resp) {
            console.log('connectToModbus:' + action + "--->" + resp.status);
            $("#loader").hide();
            if (action == 'connect' && resp.status == 200) {
                // alert("connected to "+ $("#port :selected").text());
                connected = true;
                // $("#status").innerHTML="Подключен";
                isConnect();
            } else {
                // alert("Не удалось подключиться");
                $("#alert-danger").show();
                $("#alert-success").hide();
                $("#alert-warning").hide();
                // isConnect();
            }

        },
        error: function (resp) {
            console.log(resp);

        }
    });
}

function disconnectModbus(callback) {
    $.ajax({
        url: "/connect",
        type: "POST",
        data: {'action': 'disconnect'},
        success: function (resp) {
            console.log("disconnectModbus:disconnected");
            if (resp.status == 200) {
                isConnect();
            }
            callback();
            //if (action=='connect' && resp.status==200){
            //    connected = false;
            //}

        }
    });
}

$("#selectType").on('input', function () {
    setType($("#selectType").val());
});