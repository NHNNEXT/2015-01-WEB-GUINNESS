/**
 * Login 및 회원가입을 위한 script
 * required::guinness.js
 */

var el = document.querySelector("#switchForm");
el.addEventListener("click", switchForm, false);

document.querySelector("#login-form").addEventListener("submit", function(e) { e.preventDefault(); loginCheck(); }, false);

function switchForm() {
	var el;
	el = document.querySelectorAll(".errorMessage");
	for (var i = 0; i < el.length; i++) {
	  el[i].innerHTML = "";
	}
	el = document.querySelector("#signup-form");
	if (el.style.display === "block") {
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
    var userId = document.querySelector('#login-userId').value.trim();
    var userPassword = document.querySelector('#login-userPassword').value.trim();
  	var param = "userId="+userId+"&userPassword="+userPassword;
  	guinness.ajax({
  		method: "post",
  		url: "/user/login",
  		param: param,
  		success: function(req) {
  				   if (req.responseText === "loginFailed") { document.querySelector("#login-error-message").innerHTML = "로그인 실패!"; }
  				   else { window.location.href = req.responseText; }
  				 }
  	});
}