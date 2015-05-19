/**
 * Login 및 회원가입을 위한 script
 * required::guinness.js
 */
window.addEventListener('load', function() {
	if(document.querySelector('.errorMessage').innerHTML) {
		switchForm(false);
	}
	// 에러메시지 리셋
	document.querySelector("#join-userEmail").addEventListener("focus", function(e) {
		var el = document.querySelector("#join-userEmail-message");
		el.style.display="none";
		el.innerText="";
	}, true);
	document.querySelector("#join-userName").addEventListener("focus", function(e) {
		var el = document.querySelector("#join-userName-message");
		el.style.display="none";
		el.innerText="";
	}, true);
	document.querySelector("#join-userPassword").addEventListener("focus", function(e) {
		var el = document.querySelector("#join-userPassword-message");
		el.style.display="none";
		el.innerText="";
	}, true);
	
	// 포커스 아웃시 유효성 검사
	document.querySelector("#join-userEmail").addEventListener("focusout", function(e) {
		var value = e.target.value;
		var regex=/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{1,6}(?:\.[a-z]{2})?)$/;
		var el = document.querySelector("#join-userEmail-message");
		if(regex.test(value) === false) {
			el.style.display="block";
			el.innerText="잘못된 이메일 형식입니다.";
		}
		if(value.length > 50) {
			el.style.display="block";
			el.innerText="이메일은 50 글자 이하만 사용 가능합니다.";
		}
	}, true);
	
	// 포커스 아웃시 유효성 검사
	document.querySelector("#join-userName").addEventListener("focusout", function(e) {
		var value = e.target.value;
		var regex = /^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣]+$/;
		var el = document.querySelector("#join-userName-message");
		if(regex.test(value) === false) {
			el.style.display="block";
			el.innerText="이름은 한글, 영문, 숫자만 가능합니다.";
		}
		if(value.length > 25) {
			el.style.display="block";
			el.innerText="이름은 25 글자 이하만 사용 가능합니다.";
		}
	}, true);
	
	// 포커스 아웃시 유효성 검사
	document.querySelector("#join-userPassword").addEventListener("focusout", function(e) {
		var value = e.target.value;
		var regex= /^.*(?=.*\d)(?=.*[a-zA-Z])(?=.*[0-9]).*$/;
		var el = document.querySelector("#join-userPassword-message");
		if(regex.test(value) === false) {
			el.style.display="block";
			el.innerText="비밀번호는 영어 대소문자와 숫자를 포함해야합니다.";
		}
		if(value.length < 8 || value.length >= 16) {
			el.style.display="block";
			el.innerText="비밀번호는 8자리 이상, 16자리 이하로 사용해야합니다.";
		}
	}, true);
});

var el = document.querySelector(".switchForm");
el.addEventListener("click", function() { 
	switchForm(true)}, false);

document.querySelector("#login-form").addEventListener("submit", function(e) { e.preventDefault(); loginCheck(); }, false);

function switchForm(flag) {
	var el;
	if(flag) {
		el = document.querySelectorAll(".errorMessage");
		for (var i = 0; i < el.length; i++) {
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
