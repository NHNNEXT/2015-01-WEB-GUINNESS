window.addEventListener('load', function() {
	guinness.ajax({
		method : "get",
		url : "/api/groups",
		success : function(req) { appendGroups(JSON.parse(req.responseText)); }
	});
	document.querySelector('#create-new').addEventListener('mouseup', createGroup, false);
}, false);

function createGroup() {
	var bodyTemplate = document.importNode(document.querySelector("#create-group-template").content, true);
	guinness.util.modal({
		header : "새 그룹 만들기",
		body: bodyTemplate,
		defaultCloseEvent: false
	});

	document.querySelector('.modal-close-btn').addEventListener('click', function(e){
		cancelGroupCreate();
	}, false);
	
	document.querySelector('.modal-cover').addEventListener('click', function(e){
		if (e.target.className==='modal-cover') {
			cancelGroupCreate();
		}
	}, false);
	
	document.querySelector('.modal-cover').setAttribute('tabindex',0);
	document.querySelector('.modal-cover').addEventListener('keydown',function(e){
		if(e.keyCode === 27){
			console.log('key');
			cancelGroupCreate();
		}
	},false);

	document.querySelector('#create-group-form').addEventListener('submit', function(e){
		e.preventDefault();
		var form = document.querySelector('#create-group-form');

		if(document.querySelector('.modal-cover input[name="groupName"]').value != ""){
			var param = "groupName="+form.groupName.value+"&isPublic="+form.isPublic.value;
			guinness.ajax({
				method : "post",
				url : "/group/create",
				param: param,
				success : function(req) { 
					appendGroups(JSON.parse(req.responseText)); 
				}
			});
			document.querySelector('.modal-cover').remove();
			return;
		}
		guinness.util.alert("경고!","그룹 이름을 입력하세요!");
	}, false);
}

function cancelGroupCreate() {
	if (document.querySelector(".modal-cover input[name='groupName']").value != "") {
		guinness.util.alert("취소","그룹 만들기를 취소하시겠습니까?", function(){ document.querySelector('.modal-cover').remove(); }, function(){});
		return;
	}
	document.querySelector('.modal-cover').remove();
}

function appendGroups(json) {
	var el = document.querySelector('#group-container');
	var obj = null;
	var template = document.querySelector("#group-card-template").content;
	var newEl;
	for (var i = 0; i < json.length; i++) {
		obj = json[i];
		var groupName = (obj.groupName.replace(/</g, "&lt;")).replace(/>/g, "&gt;");
		document.cookie = obj.groupId + "=" + encodeURI(obj.groupName);
		newEl = document.importNode(template, true);
		newEl.querySelector(".group-card").setAttribute("href", "/g/" + obj.groupId);
		newEl.querySelector(".group-name").innerHTML = groupName;
		newEl.querySelector('.deleteGroup-btn').addEventListener("mousedown",
			function(e) {
				e.preventDefault();
				var groupId = e.currentTarget.parentElement.parentElement.getAttribute("href").split("/")[2];
				var groupName = e.currentTarget.parentElement.querySelector(".group-name").innerHTML;
				confirmDelete(groupId, groupName);
			}, false);
		if (obj.isPublic === 'T') {
			newEl.querySelector('.fa-lock').setAttribute('class','fa fa-unlock');
		}
		newEl.querySelector('input').setAttribute("value", obj.groupId);
		el.appendChild(newEl);
	}
}

function confirmDelete(groupId, groupName) {
	groupName = (groupName.replace(/</g, "&lt;")).replace(/>/g, "&gt;");
	var message = "그룹을 삭제하시겠습니까?";
	guinness.util.alert(groupName, message,
		function() {
			document.body.style.overflow = "auto";
			location.href = "/group/delete?groupId=" + groupId;
		},
		function() {
			document.body.style.overflow = "auto";
            return;
		}
	);
}
