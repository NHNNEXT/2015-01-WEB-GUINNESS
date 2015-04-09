/**
 * Login 및 회원가입을 위한 script
 */

var el = document.querySelector("#switchForm");
el.addEventListener("click", switchForm, false);

document.querySelector("#login-form").addEventListener("submit", function(e) { e.preventDefault(); loginCheck(); }, false);

Object.prototype.show = function(){
	this.style.display = "block";
}

Object.prototype.hide = function(){
	this.style.display = "none";
}

function switchForm() {
	var el;
	el = document.querySelector(".errorMessage");
	for (var i = 0; i < el.length; i++) {
	  el[i].innerHTML = "";
	}
	el = document.querySelector("#signup-form");
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

function loginCheck() {
  	var param = "userId="+document.querySelector('#login-userId').value+"&userPassword="+document.querySelector('#login-userPassword').value;
  	guinness.ajax({
  		method: "post", 
  		url: "/user/login", 
  		param: param, 
  		success: function(req) {
  				   var res = req.responseText;
  				   if(res == "loginFailed") { document.querySelector("#login-error-message").innerHTML = "로그인 실패!"; }
  				   else { window.location.href=res; }
  				 }
  	});
}