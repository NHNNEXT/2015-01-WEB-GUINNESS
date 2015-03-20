/**
 * 
 */

var el = document.getElementById("switchForm");

el.addEventListener("click", function (){
	
	var el = document.getElementById("signup-form");
	if(el.style.display === "block") {
		el.style.display="none";
		el = document.getElementById("login-form");
		el.style.display="block";
	
		el = document.getElementById("label-login");
		el.style.display="none";
		el = document.getElementById("label-signUp");
		el.style.display="block";
	} else {
		el.style.display="block";
		el = document.getElementById("login-form");
		el.style.display="none";
	
		el = document.getElementById("label-login");
		el.style.display="block";
		el = document.getElementById("label-signUp");
		el.style.display="none";
	}
}, false);


function loginCheck() {
  	var req = new XMLHttpRequest();
  	var userId = document.getElementById('login-userId').value;
  	var password = document.getElementById('login-userPassword').value;
  	var param = "userId="+userId+"&userPassword="+password;

  	req.open("post", "/users/login", true);
  	req.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
  	req.setParameter
  	var res;
  	var el = document.getElementById("login-error-message");
  	el.innerHTML = "";
  	req.onreadystatechange = function() {
  		if (req.status === 200 && req.readyState === 4) {
  			res = req.responseText;
  			if (res === "loginFailed") { el.innerHTML = "로그인 실패!"; }
  			else { window.location.href=res; }
  		}
  	};
  	
  	req.send(param);
}