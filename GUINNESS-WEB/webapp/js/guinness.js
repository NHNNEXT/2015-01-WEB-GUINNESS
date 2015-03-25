var guinness = {};
    guinness.util = {};
    //현재 날짜를 반환하는 Function
    //문자열형태의 explode를 인자로 넣으면 해당 문자열을 구분자로 반환
	guinness.util.today = function(explode) {
	  var today = new Date();
	  var day = today.getDate();
	  var month = today.getMonth()+1;
	  var year = today.getFullYear();
	  if (day < 10) {
		day = '0'+day;  
	  }
	  if (month < 10) {
		month = '0'+month;  
	  }
	  if (explode != null) {
		today = year+explode+month+explode+day;	
	  } else {
		today = year+"년"+month+"월"+day+"일";  
	  }
	  return today;
	}
	
	//alert 메세지를 생성해준다.
	//
	guinness.util.alert = function(header, message, agreeFunc, disagreeFunc) {
		var body = document.body;
		var el = document.getElementById("guinness-alert-window");
		if (el == null) {
			el = document.createElement("div");
			el.setAttribute("id","guinness-alert-window");
			el.setAttribute("class","alert-window");
			el.innerHTML = "";
			if (agreeFunc == null && disagreeFunc == null) {
				el.innerHTML += "<div class='panel'><div class='panel-header warn'>"+header+"</div><div class='panel-body'>"+message+"<br/><button class='btn' onclick='guinness.util.alert.choose()' >확인</button></div></div>";
			} else {
				el.innerHTML += "<div class='panel'><div class='panel-header warn'>"+header+"</div><div class='panel-body'>"+message+"<br/><button class='btn' onclick='guinness.util.alert.choose(true)' >예</button><button class='btn' onclick='guinness.util.alert.choose(false)'>아니오</button></div></div>";
			}
			body.appendChild(el);
			guinness.util.alert.agree = agreeFunc;
			guinness.util.alert.disagree = disagreeFunc;
		}
	}
	guinness.util.alert.choose = function(c) {
		var el = document.getElementById("guinness-alert-window");
		el.outerHTML = "";
		delete el;
		if (c == null) {
			return true;
		}
		if (c) {
			guinness.util.alert.agree();
			return true;
		}
		guinness.util.alert.disagree();
	}
	guinness.util.alert.agree = function(){};
	guinness.util.alert.disagree = function(){};
	
	