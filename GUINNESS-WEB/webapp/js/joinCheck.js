var joinCheck = {};

joinCheck.setErrorMessageElement = function(param) {
	var el = document.querySelector("#"+param.element);
	el.style.display=param.status;
	el.innerText=param.message;
}