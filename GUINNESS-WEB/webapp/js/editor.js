window.addEventListener('load', function() {
	var groupName = document.body.querySelector("input#hiddenGroupName").value;
	document.title = groupName;
	var groupName = (groupName.replace(/</g, "&lt;")).replace(/>/g, "&gt;");
	document.querySelector('#group-name').innerHTML = groupName;

	var previewBox = document.querySelector('#previewBox');
	previewBox.innerHTML = new markdownToHtml(document
			.querySelector('#noteTextBox').value).getHtmlText();
	
	document.querySelector("#noteTargetDate").value = guinness.util.today("-");
	datepickr('#calendar', {
		dateFormat : 'Y-m-d',
		altInput : document.querySelector('#noteTargetDate')
	});
	var textBox = document.querySelector("#noteTextBox");
	textBox.addEventListener('keyup', function(e) {

		var previewBox = document.querySelector('#previewBox');
		previewBox.innerHTML = new markdownToHtml(e.target.value).getHtmlText();
	}, false);
}, false);



