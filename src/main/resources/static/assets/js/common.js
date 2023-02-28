// input 범위지정
function checkNumber($obj, min, max) {
    if(!($obj.val() >= min && $obj.val() <= max)) $obj.val("");
}

jQuery.fn.serializeObject = function() {
    let obj = null;
    try {
        if (this[0].tagName && this[0].tagName.toUpperCase() === "FORM") {
            let formData = this.serializeArray();
            console.log(formData);
            if (formData) {
                obj = {};
                jQuery.each(formData, function() {
                    obj[this.name] = this.value;
                });
            }
            let checkboxData = this.find('input[type=checkbox]').serializeArray();
            if (checkboxData) {
                $.each(checkboxData, function(index, value) {
                    if (index === 0) {
                        obj[this.name] = '';
                    }
                    obj[this.name] += (this.value + ',');
                });
            }
        }
    } catch (e) {
        console.log(e);
        alert(e.message);
    } finally {
    }

    return obj;
};

// ajax 메소드:post, put, delete, 파라미터:json
function fnCrudJsonAjax(data, url, fnCallBack, method, successMsg) {

    $.ajax({
        type: method,
        url: url,
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data),
        beforeSend: function(jqXHR, settings) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            jqXHR.setRequestHeader(header, token);
        },
        success: function(data) {
            Swal.fire({
                icon: 'success',
                text: successMsg,
            }).then(() => {
                if(fnCallBack) fnCallBack(data);
            })
        },
        error: function(xhr, status, error) {
            if (xhr.status === 400 || status === 400) {
                Swal.fire({
                    icon: 'error',
                    text: xhr.responseText,
                });
            } else if(xhr.status === 404 || status === 404) {
                Swal.fire({
                    icon: 'error',
                    text: eval("(" + xhr.responseText + ")").message,
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    text: '에러가 발생했습니다.',
                });
            }
        }
    });
}

function fnResponseCrudJsonAjax(data, url, method, fnCallBack) {

    $.ajax({
        type: method,
        url: url,
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data),
        beforeSend: function(jqXHR, settings) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            jqXHR.setRequestHeader(header, token);
        },
        success: function(xhr, data) {
            Swal.fire({
                icon: 'success',
                text: xhr.message,
            }).then(() => {
                if(fnCallBack) fnCallBack(data, xhr);
            })
        },
        error: function(xhr, status, error) {
            if (xhr.status === 400 || status === 400) {
                Swal.fire({
                    icon: 'error',
                    text: jQuery.parseJSON(xhr.responseText).message,
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    text: '에러가 발생했습니다.',
                });
            }
        }
    });
}

// ajax 메소드:post, 파라미터:json
function fnFindJsonAjax(data, url, fnCallBack) {

    $.ajax({
        type: 'post',
        url: url,
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data),
        beforeSend: function(jqXHR, settings) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            jqXHR.setRequestHeader(header, token);
        },
        success: function(data) {
            if(fnCallBack) fnCallBack(data);
        },
        error: function(xhr, status, error) {
            Swal.fire({
                icon: 'error',
                text: '오류가 발생했습니다.',
            });
        }
    });
}

function fnMethodFindJsonAjax(method, data, url, fnCallBack) {

    $.ajax({
        type: method,
        url: url,
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data),
        beforeSend: function(jqXHR, settings) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            jqXHR.setRequestHeader(header, token);
        },
        success: function(data) {
            if(fnCallBack) fnCallBack(data);
        },
        error: function(xhr, status, error) {
            Swal.fire({
                icon: 'error',
                text: '오류가 발생했습니다.',
            });
        }
    });
}

function fnCrudRefreshAjax(data, url, id, method) {

    $.ajax({
        type: method,
        url: url,
        data: JSON.stringify(data),
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        beforeSend: function (jqXHR, settings) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            jqXHR.setRequestHeader(header, token);
        },
        success: function () {
            Swal.fire({
                icon: 'success',
                text: '정상처리 되었습니다.',
            }).then(() => {
                $(`#${id}`).load(location.href+` #${id}`);
            })
        },
        error: function (xhr, status, error) {
            Swal.fire({
                icon: 'error',
                text: '오류가 발생했습니다.',
            })
        }
    });
}


function fnFormDataAjax(data, url, fnCallBack) {
    $.ajax({
        url: url,                       //주소
        data: data,                     //전송 데이터
        type: "POST",                   //전송 타입
        enctype: "multipart/form-data", //form data 설정
        processData: false,             //프로세스 데이터 설정 : false 값을 해야 form data로 인식합니다
        contentType: false,             //헤더의 Content-Type을 설정 : false 값을 해야 form data로 인식합니다
        beforeSend: function (jqXHR, settings) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            jqXHR.setRequestHeader(header, token);
        },
        success: function(data) {
            Swal.fire({
                icon: 'success',
                text: '저장되었습니다.',
            }).then(() => {
                if(fnCallBack) fnCallBack(data);
            })
        },

        error: function (xhr, status, error) {
            Swal.fire({
                icon: 'error',
                text: '오류가 발생했습니다.',
            })
        }
    });

}


function fnCalenderMaker($calender, date) {
    $calender.datepicker({
          dateFormat: 'yy-mm-dd'
        , showMonthAfterYear: true
        , monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월']
        , monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월']
        , dayNamesMin: ['일','월','화','수','목','금','토']
        , maxDate: "+0d"
    });

    if(date) {
        $calender.datepicker('setDate', date);
    } else {
        $calender.datepicker('setDate', 'today');
    }
}

function setTimeLoadingSpinner(time, msg) {
    $('#body_div').hide();
    let timerInterval;
    Swal.fire({
        title: msg,
        html: '완료될 때까지 <b></b> ms 남았습니다.',
        timer: time,
        timerProgressBar: true,
        didOpen: () => {
            Swal.showLoading()
            const b = Swal.getHtmlContainer().querySelector('b')
            timerInterval = setInterval(() => {
                b.textContent = Swal.getTimerLeft()
            }, 100)
        },
        willClose: () => {
            clearInterval(timerInterval)
        }
    })
    return time;
}

function showLoadingSpinner(msg) {
    let time = fileCount * 1100;
    let timerInterval
    Swal.fire({
        title: msg,
        html: '완료될 때까지 <b></b> ms 남았습니다.',
        timer: time,
        timerProgressBar: true,
        didOpen: () => {
            Swal.showLoading()
            const b = Swal.getHtmlContainer().querySelector('b')
            timerInterval = setInterval(() => {
                b.textContent = Swal.getTimerLeft()
            }, 100)
        },
        willClose: () => {
            clearInterval(timerInterval)
        }
    })
    return time;
}