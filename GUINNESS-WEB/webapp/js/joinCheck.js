var joinCheck = {};

joinCheck.setEmailValidation = function(element, errorElement) {
	joinCheck.clearErrorMessage(element);
	document.querySelector("#"+element).addEventListener("focusout", function(e) {
		var value = e.target.value;
		var regex=/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{1,6}(?:\.[a-z]{2})?)$/;
		if(regex.test(value) === false) {
			joinCheck.setErrorMessage({
				element: errorElement,
				status: "block",
				message: "잘못된 이메일 형식입니다."
		  	});
		}
		if(value.length > 50) {
			joinCheck.setErrorMessage({
				element: errorElement,
				status: "block",
				message: "이메일은 50 글자 이하만 사용 가능합니다."
		  	});
		}
	}, true);
}

joinCheck.setNameValidation = function(element, errorElement) {
	joinCheck.clearErrorMessage(element);
	document.querySelector("#"+element).addEventListener("focusout", function(e) {
		var value = e.target.value;
		var regex=/^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣]+$/;
		if(regex.test(value) === false) {
			joinCheck.setErrorMessage({
				element: errorElement,
				status: "block",
				message: "이름은 한글, 영문, 숫자만 가능합니다."
		  	});
		}
		if(value.length > 25) {
			joinCheck.setErrorMessage({
				element: errorElement,
				status: "block",
				message: "이름은 25 글자 이하만 사용 가능합니다."
		  	});
		}
	}, true);
}
joinCheck.setPasswordValidation = function(element, errorElement) {
	joinCheck.clearErrorMessage(element);
	document.querySelector("#"+element).addEventListener("focusout", function(e) {
		var value = e.target.value;
		var regex=/^.*(?=.*\d)(?=.*[a-zA-Z])(?=.*[0-9]).*$/;
		if(regex.test(value) === false) {
			joinCheck.setErrorMessage({
				element: errorElement,
				status: "block",
				message: "비밀번호는 영어 대소문자와 숫자를 포함해야합니다."
		  	});
		}
		if(value.length < 8 || value.length >= 16) {
			joinCheck.setErrorMessage({
				element: errorElement,
				status: "block",
				message: "비밀번호는 8자리 이상, 16자리 이하로 사용해야합니다."
		  	});
		}
	});
}

joinCheck.clearErrorMessage = function(elementName) {
	var element = document.querySelector("#"+elementName);
	element.addEventListener("focus", function(e) {
		joinCheck.setErrorMessage({
			element: elementName+"-message",
			status: "none",
			message: ""});
		}, true);
}

joinCheck.setErrorMessage = function(param) {
	var el = document.querySelector("#"+param.element);
	el.style.display=param.status;
	el.innerText=param.message;
	joinCheck.checkJoinFieldsStatus();
}

joinCheck.checkJoinFieldsStatus = function() {
	var errorMessageFiles = document.querySelectorAll(".errorMessage");
	var lengthOfFields = errorMessageFiles.length;
	for(var i=0; i<lengthOfFields; i++) {
		if(errorMessageFiles[i].style.cssText.indexOf("block") !== -1) {
			document.querySelector("#join-submit").disabled = true;
			return;
		}
	}
	document.querySelector("#join-submit").disabled = false;
}