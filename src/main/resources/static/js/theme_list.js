$(document).ready(function (){
    $('#search-btn').click(function () {
        let searchForm = {
            keyword : $('#keyword').val().replace(/^\s+|\s+$/gm,''),
            area : $('input[name=area-radio]:checked').val(),
            successExclude : $("input:checkbox[name=stamp-checkbox]").is(":checked"),
            closeExclude : $("input:checkbox[name=police-line-checkbox]").is(":checked")
        };

        $.ajax({
            url : '/theme_search',
            type : 'GET',
            data : searchForm,
        })
            .done(function (fragment) {
                $('#theme-list').replaceWith(fragment);
            });
    });

    $('#feedback-submit-btn').click(function () {

        if ($('#theme-name').val().replace(/\s| /gi, "").length==0){
            alert("테마명을 입력해주세요.");
            return false;
        }

        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        document.getElementById("theme-name").value = $('#theme-name').val().replace(/^\s+|\s+$/gm,'');
        document.getElementById("message-text").value = $('#message-text').val().replace(/^\s+|\s+$/gm,'');

        $.ajax({
            url : '/feedback/add',
            type : 'POST',
            data : $('#feedback-form').serialize(),
            beforeSend: function (xhr){
                xhr.setRequestHeader(header, token);
            },
            success : function(data){
                $('#exampleModal').modal("hide");
                alert("의견을 주셔서 감사합니다.");
                $("#sel1 option:eq(0)").prop("selected", true);
                document.getElementById("theme-name").value = null;
                document.getElementById("message-text").value = null;
            },
            error : function(request,status,error){
                console.log("code = "+ request.status + " error = " + error);
            }
        })
    });
});