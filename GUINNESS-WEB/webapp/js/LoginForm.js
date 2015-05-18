/**
 * Login 및 회원가입을 위한 script
 * required::guinness.js
 */
window.addEventListener('load', function() {
	if(document.querySelector('.errorMessage').innerHTML) {
		switchForm(false);
	}
})

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
  		url: "/login",
  		param: param,
  		success: function(req) {
  				   if (JSON.parse(req.responseText) === false) { 
  					   document.querySelector("#login-error-message").innerHTML = "로그인 실패!"; 
  				   }
  				   else { window.location.href = "/groups"}
  		}
  	});
}