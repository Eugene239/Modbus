/**
 * Created by Eugene on 02.04.2017.
 */
var offset_coil;
var reg_size_coil;
var offset_hreg;
var reg_size_hreg;
var temp_connected = false;
var coil_size = 0;
var old_offset = 0;
var desc;
var hreg_cnt = 0;
var idchanging = -1;
var idTime = 0;
function register_init() {
    getDescList(function () {
        for (i = 0; i < descNames.length; i++) {
            option = document.createElement("option");
            option.innerHTML = descNames[i];
            $("#descSelect").append(option);
        }

    });
    //$("#fieldHregs").empty();
    updateCoilsView();
    updateHregView();
}

setInterval(
    function () {
        isConnect();
        if (connected) {
            updateCoils();
            updateHregs();
        }
        if (temp_connected != connected) {
            temp_connected = connected;
            if (temp_connected) {
                $("#alert").attr('class', 'alert alert-success alert-dismissible fade show');
                $("#alert").html('<b>Подключено</b>');
            } else {
                $("#alert").attr('class', 'alert alert-danger alert-dismissible fade show');
                $("#alert").html('<b>Подключение отсутствует</b>');
            }
        }
    }, 500);
// setInterval(function () {
//       if (idchanging!==-1) idTime++;
//       if (idTime>=3) {
//           console.log(idTime);
//           idTime=0;
//           idchanging=-1;
//       }
// },1000);

function updateCoilsView() {
    offset_coil = $("#offset_coils").val();
    reg_size_coil = $("#count_coils").val();
    //  console.log(coil_size+" "+reg_size_coil);
    if (Number(reg_size_coil) > Number(coil_size)) {
        for (i = offset_coil; i < offset_coil + +(reg_size_coil - coil_size); i++) {
            button = document.createElement("button");
            button.setAttribute('class', 'btn coilBtn col');
            id = +coil_size + +i;
            button.setAttribute('id', 'coil' + id);
            button.innerHTML = id;
            $("#fieldCoils").append(button);
        }
        coil_size = reg_size_coil;

    } else if (reg_size_coil != coil_size) {
        // console.log("deleting");
        for (i = 0; i < coil_size - reg_size_coil; i++) {
            id = (+reg_size_coil + +i);
            //  console.log("remove "+ id);
            $("#coil" + id).remove();
        }
        coil_size = reg_size_coil;
    }

}
function writeCoil(id, value) {
    if (connected) {
        console.log(id + " " + JSON.parse(value));
        $.ajax({
            url: "/setCoil",
            type: "POST",
            data: {'id': id, 'value': JSON.parse(value)},
            success: function (data) {
                if (data.status == 200) {
                    updateCoils();
                    //  updateCoils(x, y);
                } else {
                    alert(data.entity);
                }
            }
        });

    }
}
function updateCoils() {
    $.ajax({
        url: "/coils",
        type: "POST",
        async: true,
        data: {'offset': offset_coil, 'size': reg_size_coil},
        success: function (resp) {
            var s = "";
            updateCoilsView();
            // console.log(resp);
            for (i = 0; i < resp.length; i++) {
                var b = document.getElementById("coil" + (+offset_coil + +i));
                //b.innerHTML=+offset_coil+ +i;
                if (b != null) {
                    b.setAttribute("value", resp[i].value);
                    b.setAttribute('onclick', 'writeCoil(' + (+offset_coil + +i) + ',' + !resp[i].value + ')');
                    if (resp[i].value) {
                        b.setAttribute("class", "btn coilBtn col btn-success");
                    } else {
                        b.setAttribute("class", "btn coilBtn col btn-danger");
                    }
                }
            }


        },
        error: function (data) {
            console.log(data);
            connected = false;
        }
    });
}


function updateHregView() {
    offset_hreg = $("#offset_hregs").val();
    reg_size_hreg = $("#count_hregs").val();

    if (Number(hreg_cnt) < Number(reg_size_hreg)) { // добавляем
        for (i = 0; i < reg_size_hreg - hreg_cnt; i++) {
           // console.log(i);

            $("#fieldHregs").append(createHreg(+i + +hreg_cnt));
            id = i + +hreg_cnt;
           // console.log(id);
            $("#range" +id).on('input', function () {
                $("#text" + $(this).attr('index')).val($(this).val());
                writeHreg($(this).attr('index'), $(this).val());
            });
            $("#range" +id).focus(function () {
                idchanging = $(this).attr('index');
            });
            $("#range"+id).focusout(function () {
                idchanging = -1;
            });
            $("#text" +id).focus(function () {
                idchanging = $(this).attr('index');
            });
            $("#text" +id).focusout(function () {
                idchanging = -1;
            });
            $("#text" +id).on('input', function () {
                $("#range" + $(this).attr('index')).val($(this).val());
                writeHreg($(this).attr('index'), $(this).val());
            });

        }
        hreg_cnt = reg_size_hreg;
    } else if (Number(hreg_cnt) !== Number(reg_size_hreg)) {
        for (i = 0; i < hreg_cnt - reg_size_hreg; i++) {
            id = (+reg_size_hreg + +i);
            //  console.log("remove "+ id);
            $("#hreg" + id).remove();
        }
        hreg_cnt = reg_size_hreg;
    }

}


function updateHregs() {
    if  (connected) {
        offset_hreg = $("#offset_hregs").val();
        reg_size_hreg = $("#count_hregs").val();
       // console.log(offset_hreg + " " + reg_size_hreg);
        if (reg_size_hreg>0) {
            $.ajax({
                url: "/hregs",
                type: "POST",
                data: {'offset': offset_hreg, 'size': reg_size_hreg},
                success: function (resp) {
                    //  console.log(resp.entity);
                    for (i = 0; i < resp.entity.length; i++) {
                        if ($("#range" + i) !== undefined && i != idchanging) {
                        $("#range" + i).val(resp.entity[i].value);
                        $("#text" + i).val(resp.entity[i].value);
                         }
                        // $("#hregrange"+(+resp.entity[i].id+ +offset)).val(resp.entity[i].value);
                        // $("#hregval"+(+resp.entity[i].id+ +offset)).val(resp.entity[i].value);
                    }
                },
                error: function (data) {
                    // isConnect();
                },
            });
        }
    }
}
$("#descSelect").change(function () {
    var name = $("#descSelect").val();
    if (name != '') {
        getRawTextCallBack(name, parseDescription)
    } else {
        desc = undefined;
    }
});
function parseDescription(data) {
    desc = JSON.parse(data.entity);
    console.log(desc);
    updateDesc();
}
$("#count_hregs").on('input', function () {
    updateHregView();
});
function updateDesc() {
    for (i = 0; i < desc.length; i++) {
        // btn = $("#coil"+i);
        if ($("#coil" + i) != null) {
            $("#coil" + i).html(desc[i].desc);
            if (desc[i].desc == '') $("#coil" + i).hide();
            else $("#coil" + i).show();
        }
        ;
        // if (btn!=null) btn.innerHTML = desc[i].desc;
    }
}
function writeHreg(id, value) {
    console.log(id + " " + value);
    $.ajax({
        url: "/setHreg",
        type: "POST",
        async: false,
        data: {'id': id, 'value': value},
        success: function (resp) {
          //console.log(resp)
            if (resp.status!=200){
                console.log(resp);
            }
        },
        error: function (data) {
            console.error(data);
        }
    });
};
$("#count_coils").on('input', function () {
    updateCoilsView();
});
$("#offset_coils").on('input', function () {
    updateCoilsView();
});
$("#count_hregs").on('input', function () {
    updateHregs();
});
$("#offset_hregs").on('input', function () {
    updateHregs();
});
function createHreg(id) {
    div = document.createElement("div");
    div.setAttribute('id', 'hreg' + id);
    div.setAttribute('class', 'input-group');
    div.style.width = '15rem';
    div.style.margin = '1rem';
    span = document.createElement("span");
    span.setAttribute('class', 'input-group-addon');
    span.innerHTML = id;
    div.appendChild(span);
    range = document.createElement("input");
    range.setAttribute('type', 'range');
    range.setAttribute('id', 'range' + id);
    range.setAttribute('index', id);
    range.setAttribute('value', 0);
    range.setAttribute('max', '255');
    range.setAttribute('class', 'range');
    div.appendChild(range);
    text = document.createElement("input");
    text.setAttribute('class', 'input-group-addon');
    text.setAttribute('index', id);
    text.setAttribute('type', 'number')
    text.style.width = '4.5rem';
    text.setAttribute('id', 'text' + id);
    text.setAttribute('value', '0');
    div.appendChild(text);
    // div.setAttribute('index',id);

    return div;
}
// <div class="input-group" style="width: 15rem; margin: 1rem" >
//     <span class="input-group-addon">0</span>
//     <input type="range" id="rangeinput0" value="0" max="255" oninput="rangevalue.value=value" style="background: #eceeef; margin: 0 !important;     border: 1px solid rgba(0,0,0,.15);"/>
//     <input type="text" class="input-group-addon" style="width: 4.5rem" id="rangevalue" value="0">
//     </div>