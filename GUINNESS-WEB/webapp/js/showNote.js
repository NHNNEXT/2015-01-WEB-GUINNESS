var search = {};
search.note = {};
search.note.pComment = {};

search.note.load = function (noteId) {
    "use strict";
    var ajaxObj = {
        method: "get",
		url: "/notes/" + noteId,
		success: function (req) {
			var json = JSON.parse(req.responseText);
			search.note.create(json);
		}
    };
	guinness.ajax(ajaxObj);
};

search.note.create = function (json) {
    "use strict";
	document.querySelector(".note-list").lastElementChild.insertAdjacentHTML("beforeend", document.querySelector(".noteTemplate").text);
	var note = document.querySelector(".note-list > li:last-child");
	note.className = "noteCard";
	note.id = json.noteId;
	note.querySelector(".avatar").src = "/img/profile/" + json.userImage;
	note.querySelector(".userName").innerText = json.userName;
	note.querySelector(".userId").innerText = json.userId;
	note.querySelector(".note-date").innerText = json.targetDate;
	note.querySelector(".noteText").innerHTML = new markdownToHtml(json.noteText).getHtmlText();
	note.querySelector(".fa-comments").innerText = " " + json.commentCount;
    var elNoteParagraph = document.querySelectorAll(".noteCard p");
    NodeList.prototype.forEach = Array.prototype.forEach;
    elNoteParagraph.forEach(function(paragraph) {
        var elPlusSquare = document.createElement('i')
        elPlusSquare.className = 'fa fa-plus-square-o';
        elPlusSquare.style.display="none";
        elPlusSquare.addEventListener('click', function (e) {
            search.note.pComment.show(e.target);
        }, false);
        paragraph.appendChild(elPlusSquare);
        paragraph.addEventListener("mouseover", function () {
            search.note.pCommentHover(event);
        }, false);
        paragraph.addEventListener("mouseleave", function (event) {
            search.note.pCommentHover(event);
        }, false);
    });
};

search.note.pCommentHover = function (event) {
    "use strict";
    if (event.target.className==="fa fa-plus-square-o") {
        return;
    }
    var plusSquare = event.target.querySelector("i");
    if(event.type === 'mouseleave') {
        plusSquare.style.display="none";
        return;
    }
    plusSquare.style.display="inline";
};

search.note.pComment.create = function(template) {
    document.body.insertAdjacentHTML("beforeend", template);
    var elpCommentBox = document.body.querySelector(".pCommentBox");
    elpCommentBox.style.display="none";
}

search.note.pComment.show = function(target) {
    search.note.pComment.targetI = target;
    search.note.pComment._setPosition(target);
    var elpCommentBox = document.body.querySelector(".pCommentBox");
    window.addEventListener('resize', function() {
        if (document.body.querySelector(".pCommentBox").style.display !== "none" ) {
            search.note.pComment._setPosition(search.note.pComment.targetI);
        }
    }, false);
}

search.note.pComment._setPosition = function (target) {
    var rect = target.getBoundingClientRect();
    var elpCommentBox = document.body.querySelector(".pCommentBox");
    elpCommentBox.style.display="block";
    elpCommentBox.style.top = rect.top+"px";
    elpCommentBox.style.left = rect.left+"px";
}

window.addEventListener('load', function () {
    "use strict";
	search.note.load(document.querySelector("#noteId").value);
    search.note.pComment.create(document.querySelector(".pCommentTemplate").text);
    
    document.querySelector('#pCommentForm').addEventListener('submit', function(e) { e.preventDefault(); createComment(e); }, false);

}, false);


function createComment(e) {
	var commentText = document.querySelector('#pCommentText').value;
	if(commentText !== ""){
		var userId = document.getElementById("sessionUserId").value;
		var noteId = document.querySelector('#noteId').value;
		var commentType = "B";
		var paragraphText = "문단 원본이라고 생각해줘요.";
		guinness.ajax({
			method:"put",
			url:"/comment/create/" + commentText + "/" + commentType + "/" + noteId + "/" + paragraphText,
			success: function(req) {
				appendComment(JSON.parse(req.responseText));
				document.querySelector('#pCommentText').value ="";
			}
		});
	}
}
