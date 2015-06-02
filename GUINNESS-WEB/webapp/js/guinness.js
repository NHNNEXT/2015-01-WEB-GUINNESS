var guinness = {};
guinness.util = {};
/*
 * 현재 날짜를 반환하는 Function 문자열형태의 explode를 인자로 넣으면 해당 문자열을 구분자로 반환
 */
guinness.util.today = function(explode) {
	var today = new Date();
	var day = today.getDate();
	var month = today.getMonth() + 1;
	var year = today.getFullYear();
	if (day < 10) {
		day = '0' + day;
	}
	if (month < 10) {
		month = '0' + month;
	}
	if (explode !== undefined) {
		today = year + explode + month + explode + day;
	} else {
		today = year + "년" + month + "월" + day + "일";
	}
	return today;
};

Element.prototype.remove = function() {
	if (this.parentElement !== null)
		this.parentElement.removeChild(this);
};

Element.prototype.show = function() {
	this.style.display = "block";
};

Element.prototype.hide = function() {
	this.style.display = "none";
};

NodeList.prototype.remove = HTMLCollection.prototype.remove = function() {
	for (var i = 0, len = this.length; i < len; i++) {
		this[i].remove();
	}
};

guinness.util.koreaDate = function (date) {
	var distance = new Date() - date;
	if (distance >= 1000 * 60 * 60) {
		var week = new Array('일', '월', '화', '수', '목', '금', '토');
		var date = new Date(date);
		var result = date.getFullYear() + "년 " + (date.getMonth()+1) + "월 "
		+ date.getDate() + "일(" + week[date.getDay()] + ") ";
		var hour = date.getHours();
		if (hour > 12) {
			result = result + "PM " + (hour - 12);
		} else {
			result = result + "AM " + hour;
		}
		var minute = date.getMinutes();
		if (minute > 9) {
			result = result + ":" + minute;
		} else {
			result = result + ":0" + minute;
		}
		return result;
	} else if (distance >= 1000 * 60) {
		return new Date(distance).getMinutes() + "분 전";
	} else {
		return "방금 전"; 
	}
}

/*
 * modal에 사용하는 Function
 */
guinness.util.modal = function(o) {
	if (document.querySelector('.modal-cover') !== null)
		return false;
	var modalCover = guinness.createElement({
		name : "div",
		attrs : {
			class : "modal-cover"
		}
	});
	var modalContainer = guinness.createElement({
		name : "div",
		attrs : {
			class : "modal-container"
		}
	});
	var modalHeader = guinness.createElement({
		name : "div",
		attrs : {
			class : "modal-header"
		},
		content : "<div class='modal-title'>" + o.header + "</div>"
	});
	var modalCloseBtn = guinness.createElement({
		name : "div",
		attrs : {
			class : "modal-close-btn"
		},
		content : "<i class='fa fa-remove'></i>"
	});
	if (o.defaultCloseEvent || o.defaultCloseEvent === undefined) {
		modalCloseBtn.addEventListener('click', function() {
			document.querySelector(".modal-cover").remove();
		}, false);
		modalCover.addEventListener('click', function(e) {
			if (e.target.className === "modal-cover") {
				document.querySelector(".modal-cover").remove();
			}
		}, false);
	}
	if (o.defaultCloseEvent === false && o.whenCloseEvent !== undefined) {
		modalCloseBtn.addEventListener('click', function() {
			document.querySelector(".modal-cover").remove();
			o.whenCloseEvent();
		}, false);
		modalCover.addEventListener('click', function(e) {
			if (e.target.className === "modal-cover") {
				document.querySelector(".modal-cover").remove();
				o.whenCloseEvent();
			}
		}, false);
	}
    
    if (typeof(o.defaultCloseEvent) === "function" ){
        o.defaultCloseEvent();
    }
    
	var modalBody = guinness.createElement({
		name : "div",
		attrs : {
			class : "modal-body"
		}
	});
	modalBody.appendChild(o.body);
	modalHeader.appendChild(modalCloseBtn);
	modalContainer.appendChild(modalHeader);
	modalContainer.appendChild(modalBody);
	modalCover.appendChild(modalContainer);
	document.body.appendChild(modalCover);
	document.querySelector('.modal-cover').focus();
};

guinness.util.setModalPosition = function () {
    var modal = document.querySelector('.modal-container');
    var viewportHeight = window.innerHeight;
    var modalHeight = modal.offsetHeight;
    if (modalHeight > viewportHeight) {
        modal.style.top = "5px";
    }
    else {
        modal.style.top = (viewportHeight - modalHeight)/2 + "px";
    }
}

guinness.createElement = function(o) {
	var el = document.createElement(o.name);
	if (o.attrs !== undefined) {
		for ( var attr in o.attrs) {
			if (o.attrs.hasOwnProperty(attr)) {
				el.setAttribute(attr, o.attrs[attr]);
			}
		}
	}
	if (o.content) {
		el.innerHTML = o.content;
	}
	return el;
};

/*
 * alert 메세지를 생성해준다.
 */
window.addEventListener("keydown", function(e) {
	if (e.keyCode === 13) {
		if (document.querySelector("#guinness-alert-window.isAlert") !== null) {
			e.preventDefault();
			document.querySelector("#guinness-alert-window").remove();
		}
	}
}, false);
guinness.util.alert = function(header, message, agreeFunc, disagreeFunc) {
	var el = document.querySelector("#guinness-alert-window");
	if (el !== null) {
		console.log("warning: guinness-alert aleady exist");
		return;
	}
	el = guinness.createElement({
		name : "div",
		attrs : {
			id : "guinness-alert-window"
		}
	});
	var innerEl = guinness.createElement({
		name : "div",
		attrs : {
			class : "alert-window"
		}
	});
	var body = document.body;
	if (agreeFunc === undefined && disagreeFunc === undefined) {
		el.setAttribute("class", "alert-window-cover isAlert");
		innerEl.innerHTML += "<div class='panel'><div class='panel-header warn'>"
				+ header
				+ "</div><div class='panel-body'>"
				+ "<span>" + message + "</span>"
				+ "<br/><div class='btn-group'><button class='btn'>확인</button></div></div></div>";
		el.appendChild(innerEl);
		el.querySelector(".btn:first-child").addEventListener("click",
				function() {
					guinness.util.alert.choose()
				}, false);
		body.appendChild(el);
	} else {
		el.setAttribute("class", "alert-window-cover");
		innerEl.innerHTML += "<div class='panel'><div class='panel-header warn'>"
				+ header
				+ "</div><div class='panel-body'>"
				+ "<span>" + message + "</span>"
				+ "<br/><div class='btn-group'><button class='btn'>예</button><button class='btn'>아니오</button></div></div></div>";
		el.appendChild(innerEl);
		el.querySelector(".btn:first-child").addEventListener("click",
				function() {
					guinness.util.alert.choose(agreeFunc);
				}, false);
		el.querySelector("#guinness-alert-window .btn:last-child")
				.addEventListener("click", function() {
					guinness.util.alert.choose(disagreeFunc);
				}, false);
		body.appendChild(el);
	}
};

guinness.util.alert.choose = function(c) {
	document.querySelector("#guinness-alert-window").remove();
	if (c == undefined) {
		return;
	}
	c();
};

guinness.confirmLeave = function(groupId, groupName, location) {
	groupName = (groupName.replace(/</g, "&lt;")).replace(/>/g, "&gt;");
	var message = "그룹을 탈퇴하시겠습니까?";
	guinness.util.alert(groupName + " 탈퇴 확인", message,
		function() {
			document.body.style.overflow = "auto";
			var sessionUserId = document.getElementById("sessionUserId").value;
			guinness.leaveGroup(sessionUserId, groupId, location);
		},
		function() {
			document.body.style.overflow = "auto";
            return;
		}
	);
}

guinness.leaveGroup = function(sessionUserId, groupId, location) {
	var param = "sessionUserId="+sessionUserId+"&groupId="+groupId;
	guinness.restAjax({
		method:"post",
		url:"/groups/members/leave",
		param: param,
		statusCode: {
			200: function(res) {
				if(location !== undefined){
					window.location.href = "/groups/form";
				}
				var groupCard = document.querySelector('#' + groupId);
				if(groupCard === null) { 
					window.location.href = "/";
					return;
				}
					groupCard.remove();
			}, 
  			406: function(res) {	// 멤버 추가 실패 
  				guinness.util.alert('경고', res);
  			}
		}
	});
}

guinness.confirmDeleteUser = function(userId, userName) {
	var message = "<strong>" + userName + "</strong>님을 멤버에서 제외 하시겠습니까?";
	guinness.util.alert("멤버 제외", message,
		function() {
			document.body.style.overflow = "auto";
			var sessionUserId = document.getElementById("sessionUserId").value;
			guinness.deleteMember(sessionUserId, userId, groupId);
		},
		function() {
			document.body.style.overflow = "auto";
            return;
		}
	);
}

guinness.deleteMember = function(sessionUserId, userId, groupId) {
	var param = "sessionUserId="+sessionUserId+"&userId="+userId+"&groupId="+groupId;
	guinness.restAjax({
		method:"post",
		url:"/groups/members/delete",
		param: param,
		statusCode: {
			200: function(res) {
				(document.getElementById(userId)).parentElement.remove()
			}, 
			406: function(res) {
				guinness.util.alert('경고', res);
			}
		}
	});
}

guinness.ajax = function(o) {
	if (o.method === undefined || o.url === undefined || o.success === undefined) {
		return;
	}
	var req = new XMLHttpRequest();
	req.onreadystatechange = function() {
		if (req.readyState == 4) {
			if (req.status == 200) {
				o.success(req);
			} else {
				document.body.innerHTML = req.responseText;
			}
		}
	};
	if (o.async === undefined) o.async = true;
	req.open(o.method, o.url, o.async);
	if (o.method.toLowerCase() == "post" || o.method.toLowerCase() == "put") {
		req.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	}
	req.send(o.param);
};
/**
 * method
 * url
 * statusCode{}
 */
guinness.restAjax = function(o) {
	if (o.method === undefined || o.url === undefined || o.statusCode === undefined) {
		return;
	}
	var req = new XMLHttpRequest();
	req.onreadystatechange = function() {
		if (req.readyState == 4) {
			o.statusCode[req.status](req.responseText);
		}
	};
	if (o.async === undefined) o.async = true;
	req.open(o.method, o.url, o.async);
	if (o.method.toLowerCase() == "post" || o.method.toLowerCase() == "put") {
		req.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	}
	req.send(o.param);
};