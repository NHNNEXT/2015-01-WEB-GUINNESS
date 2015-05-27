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
			if(json.success !== true)
				return;
			search.note.create(json.object);
		}
    };
	guinness.ajax(ajaxObj);
};

search.note.create = function (json) {
    "use strict";
	document.querySelector(".search-note-list").lastElementChild.insertAdjacentHTML("beforeend", document.querySelector(".noteTemplate").text);
	var note = document.querySelector(".search-note-list > li:last-child");
	note.className = "noteCard";
	note.id = json.noteId;
	note.querySelector(".avatar").src = "/img/profile/" + json.user.userImage;
	note.querySelector(".userName").innerText = json.user.userName;
	note.querySelector(".userId").innerText = json.user.userId;
	note.querySelector(".note-date").innerText = json.noteTargetDate;
	note.querySelector(".noteText").innerHTML = new markdownToHtml(json.noteText).getHtmlText();
	note.querySelector(".fa-comments").innerText = " " + json.commentCount;
    var elNoteParagraph = document.querySelectorAll(".noteCard p");
    NodeList.prototype.forEach = Array.prototype.forEach;
//    elNoteParagraph.forEach(function(paragraph) {
//        var elPlusSquare = document.createElement('i')
//        elPlusSquare.className = 'fa fa-plus-square-o';
//        elPlusSquare.style.display="none";
//        elPlusSquare.addEventListener('click', function (e) {
//            search.note.pComment.show(e.target);
//        }, false);
//        paragraph.appendChild(elPlusSquare);
//        paragraph.addEventListener("mouseover", function () {
//            search.note.pCommentHover(event);
//        }, false);
//        paragraph.addEventListener("mouseleave", function (event) {
//            search.note.pCommentHover(event);
//        }, false);
//    });
};

search.note.pCommentHover = function (event) {
    "use strict";
    if (event.target.className==="fa fa-plus-square-o") {
        return;
    }
    var plusSquare = event.target.querySelector(".fa-plus-square-o");
    if (plusSquare === null ) {
        return;
    }
    if(event.type === 'mouseleave') {
        plusSquare.style.display="none";
        return;
    }
    plusSquare.style.display="inline";
};

search.note.pComment.create = function(template) {
    document.body.insertAdjacentHTML("beforeend", template);
    document.body.querySelector(".pCommentBox").hide();
}

search.note.pComment.show = function(target) {
    search.note.pComment.targetI = target;
    search.note.pComment._setPosition(target);
    window.addEventListener('resize', function() {
        if (document.body.querySelector(".pCommentBox").style.display !== "none" ) {
            search.note.pComment._setPosition(search.note.pComment.targetI);
        }
    }, false);
}

search.note.pComment._setPosition = function (target) {
    var rect = target.getBoundingClientRect();
    var elpCommentBox = document.body.querySelector(".pCommentBox");
    elpCommentBox.show();
    elpCommentBox.style.top = rect.top+"px";
    elpCommentBox.style.left = rect.left+"px";
}

window.addEventListener('load', function () {
    "use strict";
	search.note.load(document.querySelector("#noteId").value);
    search.note.pComment.create(document.querySelector(".pCommentTemplate").text);
}, false);
