function appendMarkList(json) {
    if (json === null)
        return;
    var attentionListElement = document.querySelector("#attention-list");
    var questionListElement = document.querySelector("#question-list");
    var attentionList;
    var questionList;
    var newEl = undefined;
    for (var i = 0; i < json.length; i++) {
        attentionList = json[i].attentionList.replace("[", "").replace("]", "").replace(", ", ",").split(",");
        for (var j = 0; j < attentionList.length; j++) {
        	if(attentionList[j] === "")
        		continue;
            newEl = document.createElement("li");
            newEl.setAttribute("class", "mark-list");
            newEl.setAttribute("value", json[i].note.noteId);
            newEl.innerHTML = attentionList[j];
            attentionListElement.appendChild(newEl);
        }
        questionList = json[i].questionList.replace("[", "").replace("]", "").replace(", ", ",").split(",");
        for (var j = 0; j < questionList.length; j++) {
        	if(questionList[j] === "")
        		continue;
            newEl = document.createElement("li");
            newEl.setAttribute("class", "mark-list");
            newEl.setAttribute("value", json[i].note.noteId);
            newEl.innerHTML = questionList[j];
            questionListElement.appendChild(newEl);
        }
    }
}

function appendNoteList(json) {
    if (json === null)
        return;
    if (json.length !== 0) {
    	var el = document.querySelector("#empty-message");
    	el.style.visibility = "hidden";
    }
    var newEl = undefined;
    var obj = undefined;
    var out = "";
    for (var i = 0; i < json.length; i++) {
        obj = json[i];
        var createDate = obj.note.noteTargetDate;
        createDate = createDate.split(" ");
        createDate = createDate[0];
        el = document.querySelector("#day-" + createDate); // #day-2015-05-21

        if (el == undefined) {
            el = document.createElement("ul");
            el.setAttribute("id", "day-" + createDate);
            el.setAttribute("class", "note-list");
            newEl = document.createElement("div");
            newEl.setAttribute("class", "note-date");
            newEl.innerHTML = "<span class='sDate'>" + createDate + "</span>";
            el.appendChild(newEl);
            document.querySelector('#note-list-container').appendChild(el);
        }

        var attention = obj.attentionList.replace("[", "").replace("]", "").replace(", ", ",").split(",");
        var question = obj.questionList.replace("[", "").replace("]", "").replace(", ", ",").split(",");

		newEl = document.createElement("a");
		newEl.setAttribute("id", obj.note.noteId);
		newEl.setAttribute("href", "#");
        newEl.setAttribute("class", "preview-note");
        newEl.setAttribute("data-id", obj.user.userId);
        if(onOffMemberList[obj.user.userId] === "off" && onOffMemberList[obj.user.userId] !== undefined) {
            newEl.setAttribute("style", "display: none");
        }
		out = "";
		out += "<li><img class='avatar' class='avatar' src='/img/profile/"
				+ obj.user.userImage + "'>";

        var userId = document.getElementById("sessionUserId").value;
        if (userId === obj.user.userId) {
            out += "<div class='note-util'><div><div><span>수정</span><i class='fa fa-pencil'></i></div><span>삭제</span><i class='fa fa-trash'></i></div></div>";
        }
        out += "<div class='content-container'>";
        out += "<div><span class='userName'>" + obj.user.userName
            + "</span><span class='userId'>" + obj.user.userId + "</span></div>";
        out += "<div><span class='note-date'>" + obj.note.noteTargetDate
            + "</span></div>";
        if (attention.length) {
            out += "<span class='attention'>" + attention + "</span><br />";
        }
        if (question.length) {
            out += "<span class='question'>" + question + "</span><br />";
        }
        out += "<div class='comment-div'><i class='fa fa-comments'> " + obj.note.commentCount
            + "</i></div></div></li>";
        newEl.innerHTML = out;
        el.appendChild(newEl);
        document.getElementById(obj.note.noteId).addEventListener(
            "click",
            function (e) {
                if (e.target.className === "fa fa-trash") {
                    confirmDeleteNote(this.getAttribute("id"));
                } else if (e.target.className === "fa fa-pencil") {
                    window.location.href = "/notes/editor/" + this.getAttribute("id");
                } else {
                    readNoteContents(this.getAttribute("id"));
                }
            }, false);
    }
}

function confirmDeleteNote(noteId) {
    var message = "노트를 삭제하시겠습니까?";
    guinness.util.alert("노트 삭제", message, function () {
        document.body.style.overflow = "auto";
        deleteNote(noteId);
    }, function () {
        document.body.style.overflow = "auto";
        return;
    });
}

function deleteNote(noteId) {
    guinness.ajax({
        method: "delete",
        url: "/notes/" + noteId,
        success: function (req) {
            var json = JSON.parse(req.responseText);
            if (json.success === true) {
                var t = document.getElementById(noteId);
                if (t.parentElement.childElementCount <= 2) {
                    t.parentElement.remove();
                    document.querySelector("#empty-message").style.visibility = "visible";
                } else {
                    t.remove();
                }
                var list = document.querySelectorAll("#summary-container>ul>li");
                for(var i=0; i<list.length; i++) {
                	if( list[i].getAttribute("value") === noteId ) {
                		list[i].remove();
                	}
                }
            }
        }
    });
}

var currScrollTop;
function readNoteContents(noteId) {
    currScrollTop = document.body.scrollTop;
    guinness.ajax({
        method: 'get',
        url: '/notes/' + noteId,
        success: function (req) {
            var result = JSON.parse(req.responseText);
            if (result.success !== true)
                return;
            showNoteModal(result.object);
            document.body.scrollTop = currScrollTop;
            createPopupPCommentBtn();
        	setPopupPCommentBtn();
        }
    });
}

var commentTimeUpdate;

function showNoteModal(obj) {
    var bodyTemplate = document.querySelector("#view-note-template").content;
    bodyTemplate = document.importNode(bodyTemplate, true);
    guinness.util.modal({
        header: obj.user.userName,
        body: bodyTemplate,
        defaultCloseEvent: false,
        whenCloseEvent: function () {
            clearInterval(commentTimeUpdate);
            var elPopupBtn = document.querySelector(".popupCommentBtn");
            if (elPopupBtn !== null ){
                elPopupBtn.remove();
            }
            var elPopupBox = document.querySelector(".pCommentBox");
            if (elPopupBox !== null ){
                elPopupBox.remove();
            }
        }
    });
    document.querySelector('.modal-body').setAttribute('class', 'modal-body note-modal');
    var noteContent = document.querySelector('.note-content');
    var viewContent = document.createElement('DIV');
    viewContent.innerHTML = obj.noteText;
    //TODO 노트의 각 문단별 코멘트 카운트 가져오기.
    // 가져온 카온트가 0보다 큰 경우만 벌브 아이콘 달기.
    pCommentCountByP(obj.noteId);
    
    document.querySelector('.hidden-note-content').value = viewContent.innerHTML;
    refresh();
    viewContent.remove();
    document.querySelector('.hiddenUserId').value = obj.user.userId;
    document.querySelector('.hiddenNoteId').value = obj.noteId;
    document.querySelector('#commentForm').addEventListener('submit',
        function (e) {
            e.preventDefault();
            createComment(obj);
        }, false);
    readComments(obj);
}

function readComments(obj) {
    var userId = document.getElementById("sessionUserId").value;
    var noteId = obj.noteId;
    guinness.ajax({
        method: "get",
        url: "/comments/" + noteId,
        success: function (req) {
            var result = JSON.parse(req.responseText);
            if (result.success !== true)
                return;
            appendComment(result.mapValues, noteId);
            guinness.util.setModalPosition();
        }
    });
}

function appendComment(json, noteId) {
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
        commentEl.querySelector('.comment').innerHTML = (obj.commentText).replace(/\n/g, '\n<br/>');
        commentEl.querySelector('.avatar').setAttribute("src",
            "/img/profile/" + obj.userImage);
        if (userId === obj.userId) {
            commentEl.querySelector('.comment-util').innerHTML = "<div class='default-utils'><a href='#' onclick='showEditInputBox("+obj.commentId+")'>수정</a><a href='#' onclick='deleteComment("+obj.commentId+", "+noteId+")'>삭제</a></div>"
        }
    }

    commentTimeUpdate = setInterval(function (els) {
        var els = document.querySelectorAll("#commentListUl li .comment-date");
        for (var i in els) {
            els[i].innerHTML = guinness.util.koreaDate(Number(els[i].id));
        }
    }, 5000);
}

function updateComment(commentId, commentText) {
    guinness
        .ajax({
            method: "put",
            url: "/comments/" + commentId,
            param: "commentText=" + commentText,
            success: function (req) {
                var result = JSON.parse(req.responseText);
                if (result.success !== true)
                    return;
                var json = result.object;
                var el = document.querySelector("#cmt-" + commentId);
                el.querySelector('.comment').innerHTML = json.commentText.replace(/\n/g, '<br/>');
                el.querySelector('.comment-date').innerHTML = json.commentCreateDate;
                el.querySelector('.comment-date').id = Number(new Date(json.commentCreateDate));
                el.querySelector('.comment').setAttribute(
                    'contentEditable', false);
                el.querySelectorAll('.comment-update').remove();
                el.querySelector('.default-utils').show();
            }
        });
}

function deleteComment(commentId, noteId) {
    guinness.ajax({
        method: "delete",
        url: "/comments/" + commentId,
        success: function (req) {
            if (JSON.parse(req.responseText).success === true) {
            	document.querySelector('#cmt-' + commentId).remove();
                // 주석 단 사람 : ybin
                // TODO : 이곳에 noteId를 받아오는 부분이 없음.
            	document.getElementById(noteId).querySelector(".fa.fa-comments").innerHTML = " "+document.querySelector("#commentListUl").childElementCount;
            }
        }
    });
}

function showEditInputBox(commentId) {
    var el = document.querySelector('#cmt-' + commentId);
    var commentText = el.querySelector('.comment').innerHTML;
    el.querySelector('.default-utils').hide();
    el.querySelector('.comment').setAttribute('contentEditable', true);
    var updateButton = guinness.createElement({
        name: "a",
        attrs: {
            'class': "comment-update"
        },
        content: "확인"
    });
    var cancelButton = guinness.createElement({
        name: "a",
        attrs: {
            'class': "comment-update"
        },
        content: "취소"
    });
    updateButton.addEventListener('click', function(e) {
        var el = document.querySelector('#cmt-' + commentId);
        var commentText = el.querySelector('.comment').innerText;
        updateComment(commentId, commentText);
    }, false);
    cancelButton.addEventListener('click', function(e) {
        var el = document.querySelector('#cmt-' + commentId);
        el.querySelector('.comment').setAttribute('contentEditable', false);
        el.querySelector('.comment').innerHTML = (commentText).replace(/\n/g, '\n<br/>');
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
        guinness.ajax({
            method: "post",
            url: "/comments/",
            param: "commentText=" + commentText + "&noteId=" + noteId,
            success: function (req) {
                var result = JSON.parse(req.responseText);
                if (result.success !== true) {
                    document.querySelector('#commentText').value = result.message;
                    return;
                }
                appendComment(result.mapValues);
                document.querySelector('#commentText').value = "";
                //노트 리스트에서 댓글 수 수정(노트 에디트 화면에서는 필요없음)
                if(document.getElementById(noteId) !== null)
                	document.getElementById(noteId).querySelector(".fa.fa-comments").innerHTML = " "+document.querySelector("#commentListUl").childElementCount;
            }
        });
    }
}

function isJoinedUser() {
    var sessionUserId = document.getElementById("sessionUserId").value;
    for (var i = 0; i < member.length; i++) {
        if (member[i].userId === sessionUserId) {
            return true;
        }
    }
    document.querySelector(".addMemberTitle").style.display="none";
    document.querySelector("#addMemberForm .inputText").style.display="none";
    document.querySelector("#addMemberForm .inputText").value=sessionUserId;
    document.querySelector("#addMemberForm .inputBtn").style.width="30%";
    document.querySelector("#addMemberForm .inputBtn").value="가입하기";
    return false;
}

function addMember() {
	var sessionUserId = document.getElementById("sessionUserId").value;
	var userId = document.querySelector('#addMemberForm input[name="userId"]').value;
    var alert = document.querySelector(".addMemberAlert");
    alert.style.visibility = "hidden";
    alert.style.color = "#ff5a5a";
    alert.style.fontSize = "11px";
    if (userId.trim() === "") {
        alert.style.visibility = "visible";
        alert.innerHTML = "초대할 멤버의 아이디를 입력하세요.";
        return;
    }

    if(!bJoinedUser){
    	var url = "/groups/members/join";
    	var message = "가입 요청을 보냈습니다.";
    }
    else{
    	var url = "/groups/members/invite";
    	var message = "초대 요청을 보냈습니다.";
    }
    
    guinness.ajax({
        method: "post",
        url: url,
        param: "userId=" + userId + "&groupId=" + groupId + "&sessionUserId=" + sessionUserId,
        success: function (req) {
            var json = JSON.parse(req.responseText);
            if (json.success === false) {
                alert.style.visibility = "visible";
                alert.style.color = "#ff5a5a";
                alert.style.fontSize = "11px";
                alert.innerHTML = "<br/>"+json.message;
                if(bJoinedUser){
                	document.querySelector('#addMemberForm input[name="userId"]').value = "";
                }
                return;
            } else {
                alert.style.visibility = "visible";
                alert.style.color = "#86E57F";
                alert.style.fontSize = "11px";
                alert.innerHTML = "<br/>"+message;
                if(bJoinedUser){
                	document.querySelector('#addMemberForm input[name="userId"]').value = "";
                }
                return;
            }
        }
    });
}


var member;

function readMember(groupId) {
    guinness.ajax({
        method: "get",
        url: "/groups/members/" + groupId,
        success: function (req) {
            if (JSON.parse(req.responseText).success) {
                member = JSON.parse(req.responseText).mapValues;
                bJoinedUser = isJoinedUser();
                appendMembers(member);
            } else {
                window.location.href = JSON.parse(req.responseText).locationWhenFail;
            }
        }
    });
}

var memberTemplate;
var onOffMemberList = [];
if(document.querySelector("#member-template") !== null)
	memberTemplate = document.querySelector("#member-template").content;
function appendMember(obj) {
	var userId = document.getElementById("sessionUserId").value;
	var newMember = document.importNode(memberTemplate, true);
	if(userId === groupCaptainUserId){
		newMember.querySelector(".member-delete").style.visibility = "visible";
	}
	newMember.querySelector(".member-info").setAttribute("id", obj.userId);
	newMember.querySelector(".memberChk").value = obj.userId;
	newMember.querySelector(".member-name").innerHTML = obj.userName;
	newMember.querySelector(".member-id").innerHTML = obj.userId;
	newMember.querySelector(".fa-eye").setAttribute("data-id", obj.userId);
    newMember.querySelector(".fa-eye").addEventListener("mousedown", 
        function(e) {
            if(e.target.className === "fa fa-eye") {
                e.target.setAttribute("class", "fa fa-eye-slash");
                onOffMemberNotes("off", obj.userId);
                onOffMemberList[obj.userId] = "off";
            } else {
                e.target.setAttribute("class", "fa fa-eye");
                onOffMemberNotes("on", obj.userId);
                onOffMemberList[obj.userId] = "on";
            }
        }, false);
    newMember.querySelector(".fa-times").addEventListener("mousedown",
			function(e) {
				e.preventDefault();
				guinness.confirmDeleteUser(obj.userId, obj.userName);
			}, false);
	document.querySelector("#group-member").appendChild(newMember);
}

function appendMembers(json) {
    for (var i = 0; i < json.length; i++) {
        appendMember(json[i]);
    }
}

function onOffMemberNotes(flag, userId) {
    var previewNotes = document.querySelectorAll(".preview-note");
    for(var i = 0; i < previewNotes.length; i++) {
        if(previewNotes[i].dataset.id === userId && flag === "off") {
            previewNotes[i].setAttribute("style", "display: none");
        }
        if(previewNotes[i].dataset.id === userId && flag === "on") {
            previewNotes[i].setAttribute("style", "display: ");
        }
    }
}

/*
 * 주석 처리 한 사람 : ybin
 * 이유 : 현재 이 메소드 사용하는 곳이 안보임
 */
// function OnOffMemberAllClickBtn() {
//     var objs = document.querySelectorAll(".memberChk");
//     var allchk = document.querySelector(".memberAllClick");
//     var existUnchecked = false;
//     for (var i = 0; i < objs.length; i++) {
//         if (objs[i].checked === false) {
//             existUnchecked = true;
//             break;
//         }
//     }
//     if (existUnchecked === false) {
//         allchk.checked = true;
//     } else {
//         allchk.checked = false;
//     }
// }

function deleteNoteList() {
    var el = document.querySelectorAll(".note-list");
    var elLength = el.length;
    if (el != undefined) {
        for (var i = elLength - 1; i >= 0; i--) {
            el[i].outerHTML = "";
        }
    }
}
function deleteMarkList() {
	var el = document.querySelectorAll(".mark-list");
	el.remove();
}


function reloadNoteList(noteTargetDate) {
    var objs = document.querySelectorAll(".memberChk");
    guinness.ajax({
        method: "get",
        url: '/notes/reload/?groupId=' + groupId + '&noteTargetDate=' + noteTargetDate,
        success: function (req) {
            var result = JSON.parse(req.responseText);
            if (result.success) {
                deleteNoteList();
                deleteMarkList();
                appendNoteList(result.objectValues);
                appendMarkList(result.objectValues);
            }
        }
    });
    getDateExistNotes();
}

var infiniteScroll = function () {
    var scrollHeight = document.body.scrollTop + window.innerHeight;
    var documentHeight = document.body.scrollHeight;

    if (scrollHeight == documentHeight) {
        var list = document.querySelectorAll(".note-list");
        if (list.length == 0)
            return;
        var date = list.item(list.length - 1);
        var last = date.childNodes.item(date.childNodes.length - 1);
        if (date.childNodes.length == 0)
            return;
        var noteTargetDate = last.querySelector(".note-date").innerHTML;
        noteTargetDate = noteTargetDate.substring(0, noteTargetDate.length - 2);
        reloadWithoutDeleteNoteList(noteTargetDate);
    }
};

var reloadWithoutDeleteNoteList = function (noteTargetDate) {
    var objs = document.querySelectorAll(".memberChk");
    guinness.ajax({
        method: "get",
        url: '/notes/reload/?groupId=' + groupId + '&noteTargetDate=' + noteTargetDate,
        success: function (req) {
            var result = JSON.parse(req.responseText);
            if (result.success && result.objectValues.length !== 0) {
                appendNoteList(result.objectValues);
                appendMarkList(result.objectValues);
            }
        }
    });
}

function tempSave() {
    console.log("임시저장");
    var noteId = document.querySelector("#hiddenTempNoteId").value;
    var noteText = document.querySelector("#noteTextBox").value;
    var createDate = new Date().toISOString().slice(0, 10);

    if(noteId === "") {
        guinness.ajax({
            method: "post",
            url: '/notes/temp',
            param: "noteText=" + noteText + "&createDate=" + createDate,
            success: function (req) {
                var result = JSON.parse(req.responseText);
                var tempNoteId = result.object;
                var dropdownMenu = document.querySelector(".dropdown-menu");
                var el = document.createElement("li");

                el.innerHTML = "<a href='#' data-id='" + tempNoteId + "' onclick='loadTempNote(" + tempNoteId + ")'>" + guinness.util.koreaDate(new Date()) + "에 저장된 글이 있습니다</a>" +
                 "<i class='fa fa-close' onclick='deleteTempNote(" + tempNoteId + ");'></i>";

                dropdownMenu.appendChild(el);

                document.querySelector("#hiddenTempNoteId").value = tempNoteId;
            }
        }); 
    } else {
        guinness.ajax({
            method: "put",
            url: '/notes/temp',
            param: "noteId=" + noteId + "&noteText=" + noteText + "&createDate=" + createDate,
            success: function (req) {
                var result = JSON.parse(req.responseText);
                var el = document.querySelector("a[data-id='" + result.object.noteId + "']");
                el.innerText = guinness.util.koreaDate(result.object.createDate) + "에 저장된 글이 있습니다";
            }
        }); 
    }
}

function appendTempNoteList(tempNotes) {
    console.log(tempNotes);
    var dropdownMenu = document.querySelector(".dropdown-menu");
    for(var i = 0; i < tempNotes.length; i++) {
        var el = document.createElement("li");
        el.innerHTML = "<a href='#' data-id='" + tempNotes[i].noteId + "' onclick='loadTempNote(" + tempNotes[i].noteId + ")'>" + tempNotes[i].createDate + "에 저장된 글이 있습니다</a>" +
        "<i class='fa fa-close' onclick='deleteTempNote(" + tempNotes[i].noteId + ");'></i>";

        dropdownMenu.appendChild(el);
    }
}

function loadTempNote(tempNoteId) {
    console.log(tempNoteId);
    guinness.ajax({
        method: "get",
        url: '/notes/temp/' + tempNoteId,
        success: function (req) {
            var result = JSON.parse(req.responseText);
            document.querySelector("#noteTextBox").value = result.object.noteText;
            document.querySelector("#hiddenTempNoteId").value = result.object.noteId;
        }
    });
}

function deleteTempNote(tempNoteId) {
    console.log(tempNoteId);
    guinness.ajax({
        method: "delete",
        url: '/notes/temp/' + tempNoteId,
        success: function (req) {
            var result = JSON.parse(req.responseText);
            if(result.success) {
                document.querySelector("a[data-id='" + tempNoteId + "']").parentElement.remove();
                document.querySelector("#hiddenTempNoteId").value = "";
            }
        }
    });
}

function resizeSideMenu(e) {
	document.querySelector("#group-member").style.maxHeight = document.body.clientHeight - 241 +"px";
	if (document.body.clientHeight < 420) {
		document.querySelector("#summary-container").hide();
		return;
	}
	document.querySelector("#summary-container").show();
	document.querySelector("#question-list").style.maxHeight = document.querySelector("#attention-list").style.maxHeight = Math.floor((document.body.clientHeight - 395) / 2)+"px";
}
