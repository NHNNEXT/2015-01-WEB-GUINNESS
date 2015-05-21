var mousePosition = {};
mousePosition.downPoint = {
	x : 0,
	y : 0
};
mousePosition.upPoint = {
	x : 0,
	y : 0
};

pComment = {
	pCommentText : null,
	selectedText : null,
	sameSenCount : 0,
	sameSenIndex : 0,
	userId : null,
	pId : null,
	noteId : null
};

window.addEventListener('load', function() {
	createPopupPCommentBtn();
	setPopupPCommentBtn();
}, false);

function selectText() {
	var select = "";
	if (document.getSelection) {
		select = document.getSelection();
	} else if (document.selection) {
		select = document.selection.createRange().text;
	}
	var selectedText = select.toString();
	if (selectedText.length > 0) {
		return selectedText;
	}
	return false;
}

function createPopupPCommentBtn() {
	var templatePopupBtn = document.querySelector("#popupCommentBtnTemplate").text;
	document.body.insertAdjacentHTML("afterend", templatePopupBtn);

	var popupCommentBtn = document.querySelector(".popupCommentBtn");
	popupCommentBtn.addEventListener('click', function(e) {
		e.target.style.display = "none";
	}, false);
}

function setPopupPCommentBtn() {
	var elNoteText = document.body.querySelector(".noteText");

	elNoteText.addEventListener('mousedown', function(e) {
		mousePosition.downPoint.x = e.pageX;
		mousePosition.downPoint.y = e.pageY;
	}, false);

	elNoteText
			.addEventListener(
					'mouseup',
					function(e) {
						mousePosition.upPoint.x = e.pageX;
						mousePosition.upPoint.y = e.pageY;

						var selectedText = selectText();
						var selectedElClass = getSelection().getRangeAt(0).commonAncestorContainer;
						var selectedElClassName = selectedElClass.parentElement !== null ? selectedElClass.parentElement.className
								: noteText;

						var elPopupBtn = document
								.querySelector(".popupCommentBtn");
						var left = mousePosition.upPoint.x < mousePosition.downPoint.x ? mousePosition.upPoint.x
								: mousePosition.downPoint.x;
						left += Math.abs(mousePosition.upPoint.x
								- mousePosition.downPoint.x);
						var top = mousePosition.upPoint.y;

						if (selectedText && "noteText" !== selectedElClassName) {
							elPopupBtn.style.top = top + "px";
							elPopupBtn.style.left = left + "px";
							elPopupBtn.style.display = "block";
							pComment.selectedText = selectedText;
							pComment.noteId = 0;
							pComment.pId = 0;
							pComment.sameSenCount = 1;
							pComment.sameSenIndex = 1;
							pComment.userId = "";
						} else {
							elPopupBtn.style.display = "none";
						}
					}, false);

	// <span class="highlighted"></span> 를 document.body.innerText.search("") 를
	// 사용해 찾고, 각 뒤, 앞 순서로 삽입한다.
	// 이후, 해당 클래스에 색을 없게 주고, 코멘트에 hover 시, 색이 있게 한다.
	// .getRangeAt(0).commonAncestorContainer.parentElement
}