function selectText() {
	var selectionText = "";
	if (document.getSelection) {
		selectionText = document.getSelection();
	} else if (document.selection) {
		selectionText = document.selection.createRange().text;
	}
	return selectionText;
}

document.addEventListener('mouseup', function() {
	var select = selectText();
    var start = select.extentOffset < select.baseOffset ? select.extentOffset : select.baseOffset;
    var end = select.extentOffset > select.baseOffset ? select.extentOffset : select.baseOffset;
    var selectedText = select.baseNode.textContent.slice(start, end);
    if (select.anchorNode === select.extentNode &&  selectedText.length>0) {
        var baseNode = select.baseNode;

        console.log(select);
    }
    
}, false);