/**
 * Login 및 회원가입을 위한 script
 */

var el = document.querySelector("#switchForm");
el.addEventListener("click", switchForm, false);

document.querySelector("#login-form").addEventListener("submit", function(e) { e.preventDefault(); loginCheck(); }, false);

// REVIEW: 어째서 Element가 아니라 Object 인가요?
// 작성하신 게 Number, String, Array에도 모두 필요한 기능인가요?
Object.prototype.show = function(){
	this.style.display = "block";
}

Object.prototype.hide = function(){
	this.style.display = "none";
}

function switchForm() {
	var el;
	el = document.querySelector(".errorMessage");
	// REVIEW: 작성하신 코드대로라면 el은 HTMLElement라서 아래 코드는 항상 실행되지 않습니다.
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
	// REVIEW: 사용자가 입력한 값을 sanitize하지는 않더라도 최소한 trim을 사용해 공백 정도는 없애주는 것이 좋습니다.
	// 공백은 실수로 굉장히 입력되기 쉬운 값입니다.
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