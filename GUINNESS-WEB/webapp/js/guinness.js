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

Element.prototype.remove = function() {
    this.parentElement.removeChild(this);
}
NodeList.prototype.remove = HTMLCollection.prototype.remove = function() {
    for(var i = 0, len = this.length; i < len; i++) {
        if(this[i] && this[i].parentElement) {
            this[i].parentElement.removeChild(this[i]);
        }
    }
}

/*
 * modal에 사용하는 Function
 */
guinness.util.showModal = function() {
	var modalCover = document.querySelector('.modal-cover');
	if (modalCover.style.display === "none") {
		modalCover.style.display = "block";
		document.body.style.overflow = "hidden";
	}

	modalCover.addEventListener('mouseup', function(e) {
		var noteText = document.querySelector("#noteText");
		if ((e.target.className==="modal-cover" || e.target.className==='modal-close-btn') && noteText!==null && noteText.value!=="") {
			guinness.util.alert('경고', '텍스트 작성 중입니다! 정말 삭제하시겠습니까?', function(){closeModalFunction(e)}, function(){});
		} else { closeModalFunction(e); }
	}, false);
		
	document.body.addEventListener('keydown', function(e) {
		var noteText = document.querySelector("#noteText");
		if (e.keyCode === 27) {
			if (noteText!==null && noteText.value!=="") {
				guinness.util.alert('경고', '텍스트 작성 중입니다! 정말 삭제하시겠습니까?', function(){closeModalFunction(e)}, function(){});
			} else { closeModalFunction(e); }
		}
	}, false);
	
	function closeModalFunction(e) {
		if (e.target.className === 'modal-cover' || e.target.parentElement.className === 'modal-close-btn' || e.keyCode === 27) {
			guinness.util.closeModal();
			this.removeEventListener('mouseup', guinness.util.closeModal, false);
		}
	}
}

guinness.util.closeModal = function() {
	var modalCover = document.querySelector('.modal-cover');
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
	var el = document.querySelector("#guinness-alert-window");
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
					+ "<br/><div class='btn-group'><button class='btn'>확인</button></div></div></div>";
			el.appendChild(innerEl);
			body.appendChild(el);
			document.querySelector("#guinness-alert-window .btn:first-child").addEventListener("click", function(){guinness.util.alert.choose()}, false);
		} else {
			innerEl.innerHTML += "<div class='panel'><div class='panel-header warn'>"
					+ header
					+ "</div><div class='panel-body'>"
					+ message
					+ "<br/><div class='btn-group'><button class='btn'>예</button><button class='btn'>아니오</button></div></div></div>";
			el.appendChild(innerEl);
			body.appendChild(el);
			document.querySelector("#guinness-alert-window .btn:first-child").addEventListener("click", function(){guinness.util.alert.choose(agreeFunc);}, false);
			document.querySelector("#guinness-alert-window .btn:last-child").addEventListener("click", function(){guinness.util.alert.choose(disagreeFunc);}, false);
		}
	}
}
guinness.util.alert.choose = function(c) {
	document.querySelector("#guinness-alert-window").remove();
	if (c == undefined) {
		return;
	}
	c();
}

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



