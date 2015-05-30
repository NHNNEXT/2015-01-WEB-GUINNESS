/**
 * Login 및 회원가입을 위한 script
 * required::guinness.js
 */
window.addEventListener('load', function() {
	//회원가입 유효성 이벤트 등록
	setEventListener();	
	
});

var setEventListener = function() {
	// Emanl //
	joinCheck.setEmailValidation("join-userEmail", "join-userEmail-message");
	// Name //
	joinCheck.setNameValidation("join-userName", "join-userName-message");
	// Password //
	joinCheck.setPasswordValidation("join-userPassword", "join-userPassword-message");
	
	// 회원가입/로그인 폼 전환 이벤트 등록
	document.querySelector(".switchForm").addEventListener("click", function() { switchForm(true)}, false);
	// 로그인 이벤트 등록 
	document.querySelector("#login-form").addEventListener("submit", function(e) {e.preventDefault(); loginCheck(); }, false);
	// 회원가입 이벤트 등록 
	document.querySelector("#join-submit").addEventListener("click", function() {sendJoinRequest();}, false);

}

function switchForm(flag) {
	var el;
	if(flag) {
		el = document.querySelectorAll(".errorMessage");
		var length = el.length;
		for (var i = 0; i < length; i++) {
		  el[i].innerHTML = "";
		}
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
	joinCheck.ckeckEmailValidation(document.querySelector("#join-userEmail").value, "join-userEmail-message");
	joinCheck.checkNameValidation(document.querySelector("#join-userName").value, "join-userName-message");
	joinCheck.checkPasswordValidation(document.querySelector("#join-userPassword").value, "join-userPassword-message");
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
  				   if (JSON.parse(req.responseText) === false) { 
  					   document.querySelector("#login-error-message").innerHTML = "로그인 실패!"; 
  				   }
  				   else { window.location.href = "/groups/form"}
  		}
  	});
}

function sendJoinRequest() {
	var userId = document.querySelector('#join-userEmail').value.trim();
    var userName = document.querySelector('#join-userName').value.trim();
    var userPassword = document.querySelector('#join-userPassword').value.trim();
  	var param = "userId="+userId+"&userName="+userName+"&userPassword="+userPassword;
  	guinness.ajax({
  		method: "post",
  		url: "/user",
  		param: param,
  		success: function(req) {
  			var result = JSON.parse(req.responseText)
  			if (result.success === false) { 
  				document.querySelector(".errorMessage").innerHTML = result.json.message; 
  			} else { 
  				window.location.href = result.location;
  			}
		}
  	});
}