window.addEventListener('load', function() {
	document.querySelector(".searchForm").setAttribute("style","display: block");
	document.title = groupName;
	document.querySelector('#group-name').innerHTML = groupName;
	
	document.querySelector("#noteTargetDate").value = guinness.util.today("-");
	if(noteTargetDate !== "")
		document.querySelector("#noteTargetDate").value = noteTargetDate.substring(0,10);
	datepickr('#calendar', {
		dateFormat : 'Y-m-d',
		altInput : document.querySelector('#noteTargetDate')
	});
    
	var textBox = document.querySelector("#noteTextBox");
	textBox.addEventListener('keyup', loadPreviewText, false);

    function loadPreviewText() {
        var markdown = document.querySelector('#noteTextBox').value;
        guinness.ajax({
            method:"post",
            url:"/notes/editor/preview",
            param:"markdown="+markdown,
            success : function(req) {
                var json = JSON.parse(req.responseText);
                if (json.length != 0) {
                    previewText(json);
                }
            }
        });
    }

    loadPreviewText(); // editor.jsp가 로드 되었을 때, preview 텍스트 최초 로드
    
    function previewText(json) {
        var previewBox = document.querySelector('#previewBox');
		previewBox.innerHTML = json.message;
    }

    textBox.addEventListener('keydown', tabKeyHandler, false);
}, false);

function tabKeyHandler(e) {
    var TABKEY = 9;
    if(e.keyCode === TABKEY) {
        e.preventDefault();
        insertAtCaret(e.target, '\t');
    }
}

function insertAtCaret(target, text) {
    var scrollPos = target.scrollTop;
    var strPos = 0;
    var br = ((target.selectionStart || target.selectionStart == '0') ? "ff" : (document.selection ? "ie" : false ) );
    if (br == "ie") {
        target.focus();
        var range = document.selection.createRange();
        range.moveStart ('character', -txtarea.value.length);
        strPos = range.text.length;
    } else if (br == "ff") strPos = target.selectionStart;
    var front = (target.value).substring(0,strPos);
    var back = (target.value).substring(strPos,target.value.length);
    target.value = front + text + back;
    strPos = strPos + text.length;
    if (br == "ie") {
        target.focus();
        var range = document.selection.createRange();
        range.moveStart ('character', -target.value.length);
        range.moveStart ('character', strPos);
        range.moveEnd ('character', 0);
        range.select();
    } else if (br == "ff") {
        target.selectionStart = strPos;
        target.selectionEnd = strPos;
        target.focus();
    }
    target.scrollTop = scrollPos;
} 
