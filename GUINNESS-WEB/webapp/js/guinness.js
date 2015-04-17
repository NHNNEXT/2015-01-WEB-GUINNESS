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
	// REVIEW: undefined 객체하고 비교할 때는 null 등과 헷갈리지 않도록 세 개를 쓰는 편이 좋습니다.
	// 아래쪽에도 등호 두 개로 undefined와 비교하는 코드가 있는데 가능하다면 ===를 사용하도록 습관을 들이세요.
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
		// REVIEW: 위에서 정의한 this[i].remove()를 사용했으면 더 깔끔하게 정리가 될 것입니다.
		// 그리고 parentElement가 있는지 체크하는 부분은 위의 remove에 있었어야 할 듯 합니다.
        if(this[i] && this[i].parentElement) {
            this[i].parentElement.removeChild(this[i]);
        }
    }
}

/*
 * modal에 사용하는 Function
 */
guinness.util.modal = function(o) {
	if(document.querySelector('.modal-cover') != undefined) return false;
	// REVIEW: 반복되는 생성 후 속성 설정 부분을 함수로 추가하면 어떨까요?
	// 생각하는 함수 형태는 modal 함수 마지막에 추가해두었습니다. 원하는 대로 수정해서 사용하세요.
	var modalCover = document.createElement("div");
	modalCover.setAttribute("class","modal-cover");
	var modalContainer = document.createElement("div");
	modalContainer.setAttribute("class","modal-container");
	var modalHeader = document.createElement("div");
	modalHeader.setAttribute("class","modal-header");
	modalHeader.innerHTML = "<div class='modal-title'>"+o.header+"</div>";
	var modalCloseBtn = document.createElement("div")
	modalCloseBtn.setAttribute("class","modal-close-btn");
	modalCloseBtn.innerHTML = "<i class='fa fa-remove'></i>";
	if (o.defaultCloseEvent || o.defaultCloseEvent === undefined) {
		modalCloseBtn.addEventListener('click',function(){document.querySelector(".modal-cover").remove();},false);
		modalCover.addEventListener('click',function(e){if(e.target.className==="modal-cover"){document.querySelector(".modal-cover").remove();}},false);
	}
	var modalBody = document.createElement("div");
	modalBody.setAttribute("class","modal-body");
	modalBody.appendChild(o.body);
	modalHeader.appendChild(modalCloseBtn);
	modalContainer.appendChild(modalHeader);
	modalContainer.appendChild(modalBody);
	modalCover.appendChild(modalContainer);
	var currentScrollTop = document.body.scrollTop;
	document.body.appendChild(modalCover);
	document.querySelector('.modal-cover').focus();

	/**
	 * 엘리먼트를 생성하고 속성 설정, 컨텐츠까지 추가하는 함수
	 * @param String name 엘리먼트 이름(=태그 이름)
	 * @param Object attrs 속성 이름과 값의 쌍
	 * @param String content 엘리먼트 내부에 설정할 컨텐츠. innerHTML과 같다.
	 *
	 * function createElement(name, attrs, content) {
	 *   var elem = document.createElement(name);
	 *
	 *   // TODO: attrs는 undefined 일 때의 처리
	 *
	 *   for (var name in attrs) {
	 *     if (attrs.hasOwnProperty(name)) {
	 *        elem.setAttribute(name, attrs[name]);
	 *     }
	 *   }
	 *
	 *   if (content) {
	 *      elem.innerHTML = content;
	 *   }
	 *
	 *   return elem;
	 * }
	 }
	 */
}

/*
 * alert 메세지를 생성해준다.
 */
window.addEventListener("keydown",function(e){
	if(e.keyCode === 13){
		// REVIEW: 선택한 엘리먼트가 없을 때 반환되는 값은 정확하게는 null입니다.
		if(document.querySelector("#guinness-alert-window.isAlert") != undefined){
			e.preventDefault();
			document.querySelector("#guinness-alert-window").remove();
		}
	}
},false);

guinness.util.alert = function(header, message, agreeFunc, disagreeFunc) {
	var body = document.body;
	var el = document.querySelector("#guinness-alert-window");
	if (el == undefined) {
		el = document.createElement("div");
		el.setAttribute("id", "guinness-alert-window");
		var innerEl = document.createElement("div");
		innerEl.setAttribute("class", "alert-window");
		if (agreeFunc == null && disagreeFunc == null) {
			el.setAttribute("class", "alert-window-cover isAlert");
			// REVIEW: HTML 코드를 완전히 분리하는 게 어렵다면 최소한 HTML 작성하는 부분을 한 곳에서만 담당하도록 하세요.
			// 이런 식으로 작성하면 나중에 HTML을 변경해야 할 때 두 곳을 손봐야 할 것입니다.
			innerEl.innerHTML += "<div class='panel'><div class='panel-header warn'>"
					+ header
					+ "</div><div class='panel-body'>"
					+ message
					+ "<br/><div class='btn-group'><button class='btn'>확인</button></div></div></div>";
			el.appendChild(innerEl);
			body.appendChild(el);
			// REVIEW: 위에서 작성한 el을 재활용해서 el.querySelector로 했어도 될 것 같은데 굳이 이렇게 작성한 이유가 있나요?
			document.querySelector("#guinness-alert-window .btn:first-child").addEventListener("click", function(){guinness.util.alert.choose()}, false);
		} else {
			el.setAttribute("class", "alert-window-cover");
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
	// REVIEW: 이왕 에러 메시지를 보여주기로 했다면 메시지를 더 정확하게 작성하세요.
	// 'insufficient parameters supplied 쯤이 추가되면 좋지 않을까요?
	// 그런데, 파라미터가 충분하지 않은 상황인데 아래 코드는 계속 실행되도록 되어 있습니다.
	console.log("ajax Exception");
  }
  var req = new XMLHttpRequest();
  req.onreadystatechange = function() {
	if (req.readyState == 4) {
	  if (req.status == 200) {
		o.success(req);
		} else {
		  // REVIEW: Exception이 발생하면 무조건 exception.jsp로 이동하도록 되어있는데
		  // 1. exception.jsp 페이지의 정상 작동은 보장되는 건가요?
		  // 2. 사용자는 무슨 일을 겪고 있는 건지 알게 되는 건가요?
		  window.location.href = "/exception.jsp"
      }
	}
  };
  req.open(o.method, o.url, true);
  // REVIEW: 사용자가 반드시 소문자로 보낸다는 보장이 없다면 o.method.toLowerCase() 등으로 한 번 처리해주는 편이 안전합니다.
  if (o.method == "post") {
	req.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
  }
  req.send(o.param);
}



