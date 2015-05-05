window.addEventListener('load', function() {
	search.note.load(document.querySelector("#noteId").value);
}, false);

var search = {};
search.note ={};

search.note.load = function (noteId) {
	guinness.ajax({
		method: "get",
		url: "/notes/" + noteId,
		success: function(req) {
			var json = JSON.parse(req.responseText);
			search.note.create(json);
		}
	});
}

search.note.create = function(json) {
	var elContainer = document.querySelector(".note-list");
	var elNote = document.querySelector(".noteTemplate").text;
	elContainer.lastElementChild.insertAdjacentHTML("beforeend", elNote);
	elNote = document.querySelector(".note-list > li:last-child");
	elNote.id = json.noteId;
	elNote.querySelector(".avatar").src="/img/profile/"+json.userImage;
	elNote.querySelector(".userName").innerText=json.userName;
	elNote.querySelector(".userId").innerText=json.userId;
	elNote.querySelector(".note-date").innerText=json.targetDate;
	elNote.querySelector(".noteText").innerHTML=new markdownToHtml(json.noteText).getHtmlText();
	elNote.querySelector(".fa-comments").innerText=" "+json.commentCount;
}
