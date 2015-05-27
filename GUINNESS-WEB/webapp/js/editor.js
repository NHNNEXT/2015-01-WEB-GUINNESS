window.addEventListener('load', function() {
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
	textBox.addEventListener('keyup', function() {
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
	}, false);
    
    function previewText(json) {
        var previewBox = document.querySelector('#previewBox');
		previewBox.innerHTML = json.message;
    }

    textBox.addEventListener('keydown', keyHandler,false);

    function keyHandler(e) {
        var TABKEY = 9;
        if(e.keyCode == TABKEY) {
            var insertTabPoint = e.currentTarget.selectionStart;
            this.value = this.value.slice(0, insertTabPoint) + "\t" + this.value.slice(insertTabPoint);
            if(e.preventDefault) {
                e.preventDefault();
            }
            return false;
        }
    }
}, false);



