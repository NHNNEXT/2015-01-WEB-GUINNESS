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
	joinCheck.setEmailValidation("join-userId", "join-userId-message");
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
	joinCheck.ckeckEmailValidation(document.querySelector("#join-userId").value, "join-userId-message");
	joinCheck.checkNameValidation(document.querySelector("#join-userName").value, "join-userName-message");
	joinCheck.checkPasswordValidation(document.querySelector("#join-userPassword").value, "join-userPassword-message");
}

function loginCheck() {
    var userId = document.querySelector('#login-userId').value.trim();
    var userPassword = document.querySelector('#login-userPassword').value.trim();
  	var param = "userId="+userId+"&userPassword="+userPassword;
  	guinness.restAjax({
  		method: "post",
  		url: "/user/login",
  		param: param,
  		statusCode: {
  			202: function(res) {	// 로그인 성공 
  				window.location.href = "/groups/form";
  			},
  			406: function(res) {	// 로그인 실패
  				document.querySelector("#login-error-message").innerHTML = "로그인 실패!"; 
  			}
  		}
  	});
}

function sendJoinRequest() {
	var userId = document.querySelector('#join-userId').value.trim();
    var userName = document.querySelector('#join-userName').value.trim();
    var userPassword = document.querySelector('#join-userPassword').value.trim();
  	var param = "userId="+userId+"&userName="+userName+"&userPassword="+userPassword;
  	guinness.restAjax({
  		method: "post",
  		url: "/user",
  		param: param,
  		statusCode: {
  			201: function(res) {	// 생성 성공 
  				document.body.innerHTML = res;
  			},
  			409: function(res) {	// 이미 존재하는 아이디 
  				document.querySelector(".errorMessage").innerHTML = res; 
  			},
  			412: function(res) {	// 유효성 통과 못함
  				var errorList = JSON.parse(res);
  				var errorListLength = errorList.length;
  				for(var i=0; i<errorListLength; i++) {
  					joinCheck.setErrorMessage({
  	  					element: "join-"+errorList[i].id,
  	  					status: "block",
  	  					message: errorList[i].message
  	  			  	});
  				}
  			}
  		}
  	});
}