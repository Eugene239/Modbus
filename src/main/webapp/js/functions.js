function updateCoils(){
    $.ajax({
        url: "http://188.134.74.148/coils",
        type: "POST",
        data: {'offset':'10', 'size':'5'},
        success:
            function(resp){
               // obj = JSON.parse(resp);
               // console.log(resp)
                var s="";
                for (i=0; i<resp.length;i++){
                   s+=resp[i].id+" "+resp[i].value+"<br>";
                }
                $("#field").html(s);
                //for(i=0;i< resp.size;i++){
                //    console.log(resp[i].value+" "+resp[i].id)
                //   // alert(data[i]);
                //}

            },
        error: function(data){
            //alert("Ошибка запроса")
        },
        //dataType: dataType
    });
}