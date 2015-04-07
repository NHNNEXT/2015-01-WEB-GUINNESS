/**
 * Login 및 회원가입을 위한 script
 */

var el = document.getElementById("switchForm");
el.addEventListener("click", switchForm, false);

document.querySelector("#login-form").addEventListener("submit",function(e){
	e.preventDefault();
	loginCheck();
},false);

Object.prototype.show = function(){
	this.style.display = "block";
}

Object.prototype.hide = function(){
	this.style.display = "none";
}

function switchForm() {
	var el;
	el = document.getElementsByClassName("errorMessage");
	for (var i = 0; i < el.length; i++) {
	  el[i].innerHTML = "";
	}
	el = document.getElementById("signup-form");
	if(el.style.display === "block") {
		document.querySelector("#signup-form").hide();
		document.querySelector("#label-login").hide();
		document.querySelector("#login-form").show();
		document.querySelector("#label-signUp").show();
	} else {
		document.querySelector("#signup-form").show();
		document.querySelector("#label-login").show();
		document.querySelector("#login-form").hide();
		document.querySelector("#label-signUp").hide();
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