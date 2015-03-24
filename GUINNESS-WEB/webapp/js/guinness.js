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