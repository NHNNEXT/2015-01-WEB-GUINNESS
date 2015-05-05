var search = {};
search.note = {};

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
	note.id = json.noteId;
	note.querySelector(".avatar").src = "/img/profile/" + json.userImage;
	note.querySelector(".userName").innerText = json.userName;
	note.querySelector(".userId").innerText = json.userId;
	note.querySelector(".note-date").innerText = json.targetDate;
	note.querySelector(".noteText").innerHTML = new markdownToHtml(json.noteText).getHtmlText();
	note.querySelector(".fa-comments").innerText = " " + json.commentCount;
};

window.addEventListener('load', function () {
    "use strict";
	search.note.load(document.querySelector("#noteId").value);
}, false);