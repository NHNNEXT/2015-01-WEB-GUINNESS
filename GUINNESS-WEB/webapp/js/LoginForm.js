/**
 * Login 및 회원가입을 위한 script
 */

var el = document.getElementById("switchForm");
el.addEventListener("click", switchForm, false);

/* 회원가입과 로그인 Form 을 변경시켜주는 Function */
function switchForm() {
	var el = document.getElementById("signup-form");
	if(el.style.display === "block") {
		Event.hideElement("signup-form");
		Event.hideElement("label-login");
		Event.showElement("login-form");
		Event.showElement("label-signUp");
	} else {
		Event.showElement("signup-form");
		Event.showElement("label-login");
		Event.hideElement("login-form");
		Event.hideElement("label-signUp");
	}
}

/* ajax로 로그인 성공 여부를 받아와 컨트롤 */
function loginCheck() {
  	var req = new XMLHttpRequest();
  	var userId = document.getElementById('login-userId').value;
  	var password = document.getElementById('login-userPassword').value;
  	var param = "userId="+userId+"&userPassword="+password;
  	var res = null;
  	var el = document.getElementById("login-error-message");
  	el.innerHTML = "";
  	req.open("post", "/user/login", true);
  	req.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
  	req.setParameter;
  	req.onreadystatechange = function() {
  		if (req.status === 200 && req.readyState === 4) {
  			res = req.responseText;
  			if (res === "loginFailed") { el.innerHTML = "로그인 실패!"; }
  			else { window.location.href=res; }
  		}
  	};
  	req.send(param);
}

//ToDo 나중에 자주쓰는 Event를 위해 별도 js 로 분류
var Event = {};

/* element의 id를 인자로 받아 해당 element를 display = "block" 시켜준다 */
Event.showElement = function(elementId) {
	var el = document.getElementById(elementId);
	el.style.display = "block";
}

/* element의 id를 인자로 받아 해당 element를 display = "none" 시켜준다 */
Event.hideElement = function(elementId) {
	var el = document.getElementById(elementId);
	el.style.display = "none";
}