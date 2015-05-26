function searchForm(elSearch, searchResultBox) {
  this.elSearch = (elSearch!==undefined) ? elSearch : undefined ;
  this.searchResultBox = (searchResultBox!==undefined) ? searchResultBox : undefined ;
}

searchForm.prototype.init = function() {
  this._createResultBox();
  this._setPosition();

  this.elSearch.addEventListener("focus", function(e) {
    e.target.parentElement.className = "onSearchForm";
    var elResult = document.querySelector(".searchResult");
    elResult.style.display="block";
    var elIcon = document.querySelector(".onSearchForm i");
    elIcon.className = "fa fa-external-link";
  }, true);

  this.elSearch.addEventListener("focusout", function(e) {
	  focusOut(e);
  }, false);
}


searchForm.prototype._createResultBox = function() {
	  document.body.insertAdjacentHTML("beforeend", this.searchResultBox);
	  this.elResult = document.querySelector("body > section:last-child");
	  this.elResult.className = "searchResult";
}

searchForm.prototype._setPosition = function() {
	  var elInputBox = document.querySelector("#searchText");
	  var rect = elInputBox.getBoundingClientRect();
	  var elResult = document.querySelector(".searchResult");
	  elResult.style.top = rect.bottom+"px";
	  elResult.style.left = rect.left+"px";
}

var focusOut = function(e) {
    if(e.relatedTarget===null) {
      document.querySelector(".onSearchForm").className = "searchForm";
      var elResult = document.querySelector(".searchResult");
//      elResult.style.display="none";
//      document.querySelector(".searchForm > input").value="";
//      document.querySelector(".searchResult").innerHTML="<div></div>";
      var elIcon = document.querySelector(".searchForm i");
      elIcon.className = "fa fa-search";
    }
}


window.addEventListener('resize', function() {
  new searchForm()._setPosition();
}, false);

window.addEventListener('load', function() {
  var elSearch = document.querySelector(".searchForm");
    var searchResultBox = document.querySelector(".searchFormTemplate").text;
  new searchForm(elSearch, searchResultBox).init();

  elSearch.addEventListener("keyup", function(ev) {
    var sText = document.querySelector("#searchText").value;
    document.querySelectorAll(".searchResultBody").remove();
    document.querySelector("#search-notes-container").style.display="none"; 
    document.querySelector("#search-groups-container").style.display="none";
    if (sText.replace(/\s/gm, "").length>0) {
      sText = sText.replace(/^\s{1,}|\s{1,}$/, "");
      guinness.ajax({
          method:"get",
          url : "/search?words=" + sText,
          success : function(req) {  
            json = JSON.parse(req.responseText);
            searchResult(json);
          }
      });
    }
  }, false);
}, false);

function searchResult(json){
  var noteTemplate = document.querySelector("#searchResultTemplate").content;
  var groupTemplate = document.querySelector("#groupResultTemplate").content;
  var elDiv;
  for (var i = 0; i < json.listValues.groups.length; i++) {
	var group = json.listValues.groups[i];
	document.querySelector("#search-groups-container").style.display="table";
	elDiv = document.importNode(groupTemplate, true);
	elDiv.querySelector(".searchResultBody").id = "searchResultGroup-"+group.groupId;
	elDiv.querySelector(".searchResultName").innerHTML = group.groupName;
	elDiv.querySelector(".searchResultBody").addEventListener("click",function(){
		location.href="/g/"+this.id.split("-")[1];
	},false);
	document.querySelector(".search-groups").appendChild(elDiv);
  }
  
  for (var i = 0; i < json.listValues.notes.length; i++) {
	var note = json.listValues.notes[i];
	document.querySelector("#search-notes-container").style.display="table";  
	elDiv = document.importNode(noteTemplate, true);
    elDiv.querySelector(".searchResultBody").id = "searchResultNoteId-" + note.noteId;
    elDiv.querySelector(".searchResultText").innerHTML = note.noteText;
    elDiv.querySelector(".searchResultName").innerHTML = note.userName;
    elDiv.querySelector(".searchResultDate").innerHTML = guinness.util.koreaDate(note.noteTargetDate);
    elDiv.querySelector(".searchResultGroup").innerHTML = note.groupName;
    elDiv.querySelector(".searchResultBody").addEventListener("click", function(e) {
    	readNoteContents(this.id.split("-")[1]);
    }, false)
    document.querySelector('.search-notes').appendChild(elDiv);
  }
}
