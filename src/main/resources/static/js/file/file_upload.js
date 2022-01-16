function fileFormatCheck(file) {
    let fileValue = file.value;
    let fileLength = fileValue.length;
    let fileDot = fileValue.lastIndexOf('.');
    let fileType = fileValue.substring(fileDot + 1, fileLength).toLowerCase();

    if (!(fileType === 'jpg' || fileType === 'jpeg' || fileType === 'png')) {
        alert('파일 형식 오류\nJPG, JPEG, PNG 형식의 파일만 첨부할 수 있습니다.');
        file.value = "";
        return false;
    }
    return true;
}

function fileSizeCheck(file) {
    let fileSize = file.files[0].size;
    let maxSize = 300 * 1024;

    if (fileSize > maxSize) {
        alert('파일 크기 오류\n업로드할 수 있는 파일의 최대 크기는 300KB입니다.');
        file.value = "";
        return false;
    }
    return true;
}
