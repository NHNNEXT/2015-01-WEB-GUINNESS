var guinness = {};
guinness.util = {};

/* 
 * 현재 날짜를 반환하는 Function
 * 문자열형태의 explode를 인자로 넣으면 해당 문자열을 구분자로 반환
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
	if (explode != undefined) {
		today = year + explode + month + explode + day;
	} else {
		today = year + "년" + month + "월" + day + "일";
	}
	return today;
}

/*
 * modal에 사용하는 Function
 */
guinness.util.showModal = function() {
	var modalCover = document.getElementsByClassName('modal-cover')[0];
	if (modalCover.style.display === "none") {
		modalCover.style.display = "block";
		document.body.style.overflow = "hidden";
	}

	modalCover.addEventListener('mouseup', function(e) {
		if (e.target.className === 'modal-cover' || e.target.parentElement.className === 'modal-close-btn') {
			guinness.util.closeModal();
			this.removeEventListener('mouseup', guinness.util.closeModal, false);
		}
	}, false);
	document.body.addEventListener('keydown', function(e) {
		if (e.keyCode === 27) {
			guinness.util.closeModal();
			this.removeEventListener('mouseup', guinness.util.closeModal, false);
		}
	}, false);
}

guinness.util.closeModal = function() {
	var modalCover = document.getElementsByClassName('modal-cover')[0];
	if (modalCover.style.display === "block") {
		modalCover.style.display = "none";
		document.body.style.overflow = "";
	}
	selectedInitVal = "init";
}

/* 
 * alert 메세지를 생성해준다.
 */
guinness.util.alert = function(header, message, agreeFunc, disagreeFunc) {
	var body = document.body;
	var el = document.getElementById("guinness-alert-window");
	if (el == undefined) {
		el = document.createElement("div");
		el.setAttribute("id", "guinness-alert-window");
		el.setAttribute("class", "alert-window-cover");
		var innerEl = document.createElement("div");
		innerEl.setAttribute("class", "alert-window");
		if (agreeFunc == null && disagreeFunc == null) {
			innerEl.innerHTML += "<div class='panel'><div class='panel-header warn'>"
					+ header
					+ "</div><div class='panel-body'>"
					+ message
					+ "<br/><div class='btn-group'><button class='btn' onclick='guinness.util.alert.choose()' >확인</button></div></div></div>";
		} else {
			innerEl.innerHTML += "<div class='panel'><div class='panel-header warn'>"
					+ header
					+ "</div><div class='panel-body'>"
					+ message
					+ "<br/><div class='btn-group'><button class='btn' onclick='guinness.util.alert.choose(true)' >예</button><button class='btn' onclick='guinness.util.alert.choose(false)'>아니오</button></div></div></div>";
		}
		body.appendChild(el);
		el.appendChild(innerEl);
		guinness.util.alert.agree = agreeFunc;
		guinness.util.alert.disagree = disagreeFunc;
	}
}
guinness.util.alert.choose = function(c) {
	var el = document.getElementById("guinness-alert-window");
	el.parentElement.removeChild(el);
	if (c == undefined) {
		return;
	}
	if (c) {
		guinness.util.alert.agree();
		return;
	}
	guinness.util.alert.disagree();
}
guinness.util.alert.agree = function() {
};
guinness.util.alert.disagree = function() {
};

guinness.ajax = function(o) {
  if(o.method === undefined || o.url === undefined || o.success === undefined) {
	console.log("ajax Exception");
  }
  var req = new XMLHttpRequest();
  req.onreadystatechange = function() {
	if (req.readyState == 4) {
	  if (req.status == 200) {
		o.success(req);
		} else {
		  window.location.href = "/exception.jsp"
      }
	}
  };
  req.open(o.method, o.url, true);
  if (o.method == "post") {
	req.setRequestHeader("Content-type", "application/x-www-form-urlencoded");	
  }
  req.send(o.param);
}



