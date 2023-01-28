// input 범위지정
function checkNumber($obj, min, max) {
    if(!($obj.val() >= min && $obj.val() <= max)) $obj.val("");
}

// ajax 메소드:post, 파라미터:json, Fire
function fnPostFireJsonAjax(data, url, fnCallBack, successMsg) {

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
function fnPostJsonAjax(data, url, fnCallBack) {

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