$(document).ready(function() {

    function uploadFile(file) {
        var formData = new FormData();
        formData.append('file', file);
        $.ajax({
            url: './upload',
            type: 'POST',
            data: formData,
            cache: false,
            contentType: false,
            processData: false,
            success: function (data, textStatus, jqXHR) {
                var lastFile = data[data.length-1];
                $('#messages').append('<li>' + lastFile.filename + ' successfuly uploaded.</li>');
                $('#managedFiles').append('<li><a href="./file/' + lastFile.id + '">' + lastFile.filename + '</a></li>');
            },
            error: function (jqXHR, textStatus, errorThrown) {
                $('#messages').append('<li style="color: red;">"' + textStatus + '</li>');
            }
        });
    }

    var fileInput = document.getElementById('fileUpload');
    fileInput.addEventListener('change', function (e) {
        var file = fileInput.files[0];
        if (file) {
            uploadFile(file);
        }
    });
});
