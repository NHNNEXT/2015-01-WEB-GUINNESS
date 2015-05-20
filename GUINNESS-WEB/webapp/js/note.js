function cancelNoteCreate(e) {
	if (document.querySelector(".modal-cover #noteText").value != "") {
		guinness.util.alert("취소", "작성중인 노트 기록을 취소하시겠습니까?", function() {
			document.querySelector('.modal-cover').remove();
		}, function() {
		});
		return;
	}
	document.querySelector('.modal-cover').remove();
}

function readNoteList(groupId, noteTargetDate) {
	guinness.ajax({
		method : "get",
		url : "/note/list?groupId=" + groupId + "&noteTargetDate="
				+ noteTargetDate,
		success : function(req) {
			var json = JSON.parse(req.responseText);
			if (json.length != 0) {
				appendNoteList(json);
			}
		}
	});
}

function appendNoteList(json) {
	var el = document.querySelector("#empty-message");
	if (el != undefined) {
		el.parentNode.removeChild(el);
	}
	el = document.querySelectorAll(".note-list");
	var elLength = el.length;
	if (el != undefined) {
		for (var i = elLength - 1; i >= 0; i--) {
			el[i].outerHTML = "";
		}
	}
	var newEl = undefined;
	var obj = undefined;
	var out = "";
	for (var i = 0; i < json.length; i++) {
		obj = json[i];
		var noteTargetDate = obj.noteTargetDate;
		noteTargetDate = noteTargetDate.split(" ");
		noteTargetDate = noteTargetDate[0];
		noteTargetDate = noteTargetDate.replace(/'-'/g, '');
		el = document.querySelector("#day-" + noteTargetDate);
		if (el == undefined) {
			el = document.createElement("ul");
			el.setAttribute("id", "day-" + noteTargetDate);
			el.setAttribute("class", "note-list");
			newEl = document.createElement("div");
			newEl.setAttribute("class", "note-date");
			newEl.innerHTML = "<span>" + noteTargetDate + "</span>";
			el.appendChild(newEl);
			document.querySelector('#note-list-container').appendChild(el);
		}
		var noteText = new markdownToHtml(obj.noteText).getHtmlText();
		var attention = noteText
				.match(/<span class="attention">.{1,}<\/span>/g);
		if (attention !== null) {
			attention = attention.join('<br />');
		}
		var question = noteText.match(/<span class="question">.{1,}<\/span>/g);
		if (question !== null) {
			question = question.join('<br />');
		}
		var tag = noteText.match(/<span class="tag">.{1,}<\/span>/g);
		if (tag !== null) {
			tag = tag.join(' ');
		}
		newEl = document.createElement("a");
		newEl.setAttribute("id", obj.noteId);
		newEl.setAttribute("href", "#");
		out = "";
		out += "<li><img class='avatar' class='avatar' src='/img/profile/"
				+ obj.userImage + "'>";

		var userId = document.getElementById("sessionUserId").value;
		if (userId === obj.userId) {
			out += "<div class='note-util'><i class='fa fa-pencil'></i><i class='fa fa-trash'></i></div>";
		}
		out += "<div class='content-container'>";
		out += "<div><span class='userName'>" + obj.userName
				+ "</span><span class='userId'>" + obj.userId + "</span></div>";
		out += "<div><span class='note-date'>" + obj.noteTargetDate
				+ "</span></div>";
		if (attention !== null) {
			out += attention + '<br />'
		}
		if (question !== null) {
			out += question + '<br />'
		}
		if (tag !== null) {
			out += tag + '<br />'
		}
		out += "<div class='comment-div'><i class='fa fa-comments'> " + obj.commentCount
				+ "</i></div></div></li>";
		newEl.innerHTML = out;
		el.appendChild(newEl);
		document.getElementById(obj.noteId).addEventListener(
				"click",
				function(e) {
					if (e.target.className === "fa fa-trash") {
						confirmDeleteNote(this.getAttribute("id"));
					} else if (e.target.className === "fa fa-pencil") {
						window.location.href = "/notes/editor/"
								+ this.getAttribute("id");
					} else {
						readNoteContents(this.getAttribute("id"));
					}
				}, false);
	}
}

function confirmDeleteNote(noteId) {
	var message = "노트를 삭제하시겠습니까?";
	guinness.util.alert("노트 삭제", message, function() {
		document.body.style.overflow = "auto";
		deleteNote(noteId);
	}, function() {
		document.body.style.overflow = "auto";
		return;
	});
}

function deleteNote(noteId) {
	guinness.ajax({
		method: "delete",
		url: "/notes/" + noteId,
		success: function(req) {
		var json = JSON.parse(req.responseText);
			if(json.success === true) {
				var t = document.getElementById(noteId);
				if (t.parentElement.childElementCount <= 2) {
					t.parentElement.remove();
				} else {
					t.remove();
				}
			}
		}
	});
}

var currScrollTop;
function readNoteContents(noteId) {
	currScrollTop = document.body.scrollTop;
	guinness.ajax({
		method : 'get',
		url : '/notes/' + noteId,
		success : function(req) {
			var result = JSON.parse(req.responseText);
			if (result.success !== true)
				return;
			showNoteModal(result.object);
			document.body.scrollTop = currScrollTop;
		}
	});
}

var commentTimeUpdate;

function showNoteModal(obj) {
	var bodyTemplate = document.querySelector("#view-note-template").content;
	bodyTemplate = document.importNode(bodyTemplate, true);
	guinness.util.modal({
		header : obj.user.userName,
		body : bodyTemplate,
		defaultCloseEvent : false,
		whenCloseEvent : function() {
			reloadNoteList();
			clearInterval(commentTimeUpdate);
		}
	});
	document.querySelector('.modal-body').setAttribute('class',
			'modal-body note-modal');
	document.querySelector('.note-content').innerHTML = new markdownToHtml(
			obj.noteText).getHtmlText();
	document.querySelector('#commentForm').addEventListener('submit',
			function(e) {
				e.preventDefault();
				createComment(obj);
			}, false);

	readComments(obj);
}

function readComments(obj) {
	var userId = document.getElementById("sessionUserId").value;
	var noteId = obj.noteId;
	guinness.ajax({
		method : "get",
		url : "/comments/" + noteId,
		success : function(req) {
			var result = JSON.parse(req.responseText);
			if (result.success !== true)
				return;
			appendComment(result.mapValues);
			guinness.util.setModalPosition();
		}
	});
}

function appendComment(json) {
	var el = document.querySelector('#commentListUl');
	var userId = document.getElementById("sessionUserId").value;
	while (el.hasChildNodes()) {
		el.removeChild(el.firstChild);
	}
	for (var i = 0; i < json.length; i++) {
		obj = json[i];
		var commentTemplate = document.querySelector("#comment-template").content;
		commentTemplate = document.importNode(commentTemplate, true);
		var commentList = document.querySelector('#commentListUl');
		commentList.appendChild(commentTemplate);
		var commentEl = commentList.querySelector('li:last-child');
		commentEl.setAttribute('id', 'cmt-' + obj.commentId);
		commentEl.querySelector('.comment-user').innerHTML = obj.userName;
		commentEl.querySelector('.comment-date').innerHTML = guinness.util
				.koreaDate(obj.commentCreateDate);
		commentEl.querySelector('.comment-date').id = obj.commentCreateDate;
		commentEl.querySelector('.comment').innerHTML = obj.commentText;
		commentEl.querySelector('.avatar').setAttribute("src",
				"/img/profile/" + obj.userImage);
		if (userId === obj.userId) {
			commentEl.querySelector('.comment-util').innerHTML = "<div class='default-utils'><a href='#' onclick='showEditInputBox(&quot;"
					+ obj.commentText
					+ "&quot; , &quot;"
					+ obj.commentId
					+ "&quot;)'>수정</a><a href='#' onclick='deleteComment(&quot;"
					+ obj.commentId + "&quot;)'>삭제</a></div>"
		}
	}

	commentTimeUpdate = setInterval(function(els) {
		var els = document.querySelectorAll("#commentListUl li .comment-date");
		for ( var i in els) {
			els[i].innerHTML = guinness.util.koreaDate(Number(els[i].id));
		}
	}, 5000);
}

function updateComment(commentId, commentText) {
	guinness
			.ajax({
				method : "put",
				url : "/comments/" + commentId,
				param : "commentText=" + commentText,
				success : function(req) {
					var result = JSON.parse(req.responseText);
					if (result.success !== true)
						return;
					var json = result.object;
					var el = document.querySelector("#cmt-" + commentId);
					el.querySelector('.comment').innerHTML = json.commentText;
					el.querySelector('.comment-date').innerHTML = json.commentCreateDate;
					el.querySelector('.comment').setAttribute(
							'contentEditable', false);
					el.querySelectorAll('.comment-update').remove();
					el.querySelector('.default-utils').show();
				}
			});
}

function deleteComment(commentId) {
	guinness.ajax({
		method : "delete",
		url : "/comments/" + commentId,
		success : function(req) {
			if (JSON.parse(req.responseText).success === true)
				document.querySelector('#cmt-' + commentId).remove();
		}
	});
}

function showEditInputBox(commentText, commentId) {
	var el = document.querySelector('#cmt-' + commentId);
	el.querySelector('.default-utils').hide();
	el.querySelector('.comment').setAttribute('contentEditable', true);
	var updateButton = guinness.createElement({
		name : "a",
		attrs : {
			'class' : "comment-update"
		},
		content : "확인"
	});
	var cancelButton = guinness.createElement({
		name : "a",
		attrs : {
			'class' : "comment-update"
		},
		content : "취소"
	});
	updateButton.addEventListener('click', function() {
		var el = document.querySelector('#cmt-' + commentId);
		var commentText = el.querySelector('.comment').textContent;
		updateComment(commentId, commentText);
	}, false);
	cancelButton.addEventListener('click', function() {
		var el = document.querySelector('#cmt-' + commentId);
		el.querySelector('.comment').setAttribute('contentEditable', false);
		el.querySelector('.comment').innerHTML = commentText;
		el.querySelectorAll('.comment-update').remove();
		el.querySelector('.default-utils').show();
	}, false);
	el.querySelector('.comment-util').appendChild(updateButton);
	el.querySelector('.comment-util').appendChild(cancelButton);
}

function createComment(obj) {
	var commentText = document.querySelector('#commentText').value;
	if (commentText !== "") {
		var userId = document.getElementById("sessionUserId").value;
		var noteId = obj.noteId;
		var commentType = "A";
		guinness.ajax({
			method : "post",
			url : "/comments/",
			param : "commentText=" + commentText + "&commentType="
					+ commentType + "&noteId=" + noteId,
			success : function(req) {
				var result = JSON.parse(req.responseText);
				if (result.success !== true)
					return;
				appendComment(result.mapValues);
				document.querySelector('#commentText').value = "";
			}
		});
	}
}

function addMember() {
	var userId = document.querySelector('#addMemberForm input[name="userId"]').value;
	var alert = document.querySelector(".addMemberAlert");
	alert.style.visibility="hidden";
	if (userId.trim() === ""){
		alert.style.visibility="visible";
		alert.style.color="#ff5a5a";
		alert.style.fontSize="11px";
		alert.innerHTML = "초대할 멤버의 아이디를 입력하세요.";
		return;
	}
	var groupId = document.querySelector('#addMemberForm input[name="groupId"]').value;
	guinness.ajax({
		method : "post",
		url : "/groups/members/invite",
		param : "userId=" + userId + "&groupId=" + groupId,
		success : function(req) {
			var json = JSON.parse(req.responseText);
			if (json.success === false) {
				alert.style.visibility="visible";
				alert.style.color="#ff5a5a";
				alert.style.fontSize="11px";
				alert.innerHTML = json.message;
				return;
			} else {
				alert.style.visibility="visible";
				alert.style.color="#86E57F";
				alert.style.fontSize="11px";
				alert.innerHTML = "초대 요청을 보냈습니다.";
				return;
			}
		}
	});
}

function readMember(groupId) {
	guinness.ajax({
		method : "get",
		url : "/groups/members/" + groupId,
		success : function(req) {
			if (JSON.parse(req.responseText).success) {
				appendMembers(JSON.parse(req.responseText).mapValues);
			} else {
				window.location.href = JSON.parse(req.responseText).locationWhenFail;
			}
		}
	});
}

var memberTemplate = document.querySelector("#member-template").content;
function appendMember(obj) {
	var newMember = document.importNode(memberTemplate, true);
	newMember.querySelector(".memberChk").value = obj.userId;
	newMember.querySelector(".member-name").innerHTML = obj.userName;
	newMember.querySelector(".member-id").innerHTML = obj.userId;
	document.querySelector("#group-member").appendChild(newMember);
}

function appendMembers(json) {
	for (var i = 0; i < json.length; i++) {
		appendMember(json[i]);
	}
}

//function allCheckMember() {
//	var objs = document.querySelectorAll(".memberChk");
//	var allchk = document.querySelector(".memberAllClick");
//	for (var i = 0; i < objs.length; i++) {
//		objs[i].checked = allchk.checked;
//	}
//}

function OnOffMemberAllClickBtn() {
	var objs = document.querySelectorAll(".memberChk");
	var allchk = document.querySelector(".memberAllClick");
	var existUnchecked = false;
	for (var i = 0; i < objs.length; i++) {
		if (objs[i].checked === false) {
			existUnchecked = true;
			break;
		}
	}
	if (existUnchecked === false) {
		allchk.checked = true;
	} else {
		allchk.checked = false;
	}
}

function reloadNoteList(noteTargetDate) {
	var groupId = window.location.pathname.split("/")[2];
	var objs = document.querySelectorAll(".memberChk");
	var array = [];
	for (var i = 0; i < objs.length; i++) {
		if (objs[i].checked === true)
			array.push("'" + objs[i].value + "'");
	}
	guinness.ajax({
		method : "get",
		url : '/notes/reload/?groupId=' + groupId + '&noteTargetDate='
				+ noteTargetDate + '&checkedUserId=' + array,
		success : function(req) {
			var result = JSON.parse(req.responseText);
			if (result.success) {
				appendNoteList(result.mapValues);
			}
		}
	});
}
