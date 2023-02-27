$(document).ready(function() {
    $("#multipleFiles").on("change", checkFileCount);
});

let imageFileDict = {};
const content_files = [];
let fileNum = 0;
let fileCount = 0;

function checkFileCount(e) {
    const files = e.target.files;
    if (files.length > 5) {
        alert("최대 5개의 파일까지 업로드할 수 있습니다. 파일을 다시 선택해주세요.");
        e.value = "";
        return false;
    } else {
        const filesArr = Array.prototype.slice.call(files);

        filesArr.forEach(function (f) {
            const reader = new FileReader();
            reader.onload = function (e) {
                imageFileDict[fileNum] = files;
                content_files.push(f);
                $('#imageChange').append(
                    '<div class="mx-1" id="file' + fileNum + '" onclick="fileDelete(\'file' + fileNum + '\')">' +
                    '<button class="m-auto"> 삭제 </button>' +
                    `<img src="${e.target.result}" data-file=${f.name} className="article-image" style="height: 150px"/>` +
                    '</div>'
                );
                fileNum++;
                fileCount++;
            };
            reader.readAsDataURL(f);
        });
        //초기화 한다.
        $("#input_file").val("");
    }
}

function fileDelete(fileNum){
    var no = fileNum.replace(/[^0-9]/g, "");
    content_files[no].is_delete = true;
    $('#' + fileNum).remove();
    fileCount --;
}

function imageUpload(entity, id, url, type) {
    if (fileCount !== 0) {
        const form = $("#s3Form")[0];
        const formData = new FormData(form);

        formData.append("entity", entity);
        formData.append("id", id);


        for (let x = 0; x < content_files.length; x++) {
            // 삭제 안한것만 담아 준다.
            if (!content_files[x].is_delete && x < 5) {
                formData.append("files", content_files[x]);
            }
        }

        /*
        * 파일업로드 multiple ajax처리
        */
        $.ajax({
            type: type,
            enctype: "multipart/form-data",
            url: url,
            processData: false,
            contentType: false,
            data: formData,
            beforeSend: function(jqXHR, settings) {
                var header = $("meta[name='_csrf_header']").attr("content");
                var token = $("meta[name='_csrf']").attr("content");
                jqXHR.setRequestHeader(header, token);
            },
            error: function (xhr, status, error) {
                Swal.fire({
                    icon: 'error',
                    text: '이미지 업로드에 실패했습니다.',
                });
            }
        });
    }
}