/**
 * Created by Eugene on 28.03.2017.
 */

function initConnect() {
    isConnect(); // проверка подключения
    getPorts("connection"); //заполняем порты
    getParameters(); //получаем параметры подключения
    //if (parameters==null){
    //    $("#alert-danger").hide();
    //    $("#alert-success").hide();
    //    $("#alert-warning").show();
    //} else {
    //
    //}
}


$("#connect").click(function(){
    $("#loader").show();
    parameters=setParameters(function(){
    connectToModbus("connect")});//устанавливаем параметры
});
$("#disconnect").click(function(){
    disconnectModbus();
});