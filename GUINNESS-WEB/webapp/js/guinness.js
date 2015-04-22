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

/*
 * modal에 사용하는 Function
 */
guinness.util.modal = function(o) {
  if (document.querySelector('.modal-cover') !== null) return false;
  var modalCover = guinness.createElement({
    name: "div",
    attrs: {
      class: "modal-cover"
    }
  });
	var modalContainer = guinness.createElement({
    name: "div",
    attrs: {
      class: "modal-container"
    }
  });
	var modalHeader = guinness.createElement({
    name: "div",
    attrs: {
      class: "modal-header"
    },
    content: "<div class='modal-title'>"+ o.header + "</div>"
  });
	var modalCloseBtn = guinness.createElement({
    name: "div",
    attrs: {
      class: "modal-close-btn"
    },
    content: "<i class='fa fa-remove'></i>"
  });
	if (o.defaultCloseEvent || o.defaultCloseEvent === undefined) {
		modalCloseBtn.addEventListener('click',function(){document.querySelector(".modal-cover").remove();},false);
		modalCover.addEventListener('click',function(e){if(e.target.className==="modal-cover"){document.querySelector(".modal-cover").remove();}},false);
	}
	var modalBody = guinness.createElement({
    name: "div",
    attrs: {
      class: "modal-body"
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

guinness.createElement = function(o) {
  var el = document.createElement(o.name);
  if (o.attrs !== undefined) {
    for (var attr in o.attrs) {
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
		if (document.querySelector("#guinness-alert-window.isAlert") !== null){
			e.preventDefault();
			document.querySelector("#guinness-alert-window").remove();
		}
	}
},false);
guinness.util.alert = function(header, message, agreeFunc, disagreeFunc) {
	var el = document.querySelector("#guinness-alert-window");
	if (el !== null) {
    console.log("warning: guinness-alert aleady exist");
    return;
  }
	el = guinness.createElement({
    name: "div",
    attrs: {
      id: "guinness-alert-window"
    }
  });
	var innerEl = guinness.createElement({
    name: "div",
    attrs: {
      class: "alert-window"
    }
  });
  var body = document.body;
  if (agreeFunc === undefined && disagreeFunc === undefined) {
		el.setAttribute("class", "alert-window-cover isAlert");
		innerEl.innerHTML += "<div class='panel'><div class='panel-header warn'>"
					+ header
					+ "</div><div class='panel-body'>"
					+ message
					+ "<br/><div class='btn-group'><button class='btn'>확인</button></div></div></div>";
		el.appendChild(innerEl);
    el.querySelector(".btn:first-child").addEventListener("click", function(){guinness.util.alert.choose()}, false);
		body.appendChild(el);
	} else {
		el.setAttribute("class", "alert-window-cover");
		innerEl.innerHTML += "<div class='panel'><div class='panel-header warn'>"
					+ header
					+ "</div><div class='panel-body'>"
					+ message
					+ "<br/><div class='btn-group'><button class='btn'>예</button><button class='btn'>아니오</button></div></div></div>";
		el.appendChild(innerEl);
    el.querySelector(".btn:first-child").addEventListener("click", function(){guinness.util.alert.choose(agreeFunc);}, false);
    el.querySelector("#guinness-alert-window .btn:last-child").addEventListener("click", function(){guinness.util.alert.choose(disagreeFunc);}, false);
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

guinness.ajax = function(o) {
  if(o.method === undefined || o.url === undefined || o.success === undefined) {
	  console.log("error: insufficient parameters supplied");
    return;
  }
  var req = new XMLHttpRequest();
  req.onreadystatechange = function() {
	  if (req.readyState == 4) {
	    if (req.status == 200) {
		  o.success(req);
		  } else {
		    window.location.href = "/exception";
      }
	  }
  };
  req.open(o.method, o.url, true);
  if (o.method.toLowerCase() == "post") {
	  req.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
  }
  req.send(o.param);
};