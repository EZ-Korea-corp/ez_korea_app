// input 범위지정
function checkNumber($obj, min, max) {
    if(!($obj.val() >= min && $obj.val() <= max)) $obj.val("");
}

jQuery.fn.serializeObject = function() {
    let obj = null;
    try {
        if (this[0].tagName && this[0].tagName.toUpperCase() == "FORM") {
            var arr = this.serializeArray();
            if (arr) {
                obj = {};
                jQuery.each(arr, function() {
                    obj[this.name] = this.value;
                });
            }//if ( arr ) {
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
            Swal.fire({
                icon: 'error',
                text: '오류가 발생했습니다.',
            });
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