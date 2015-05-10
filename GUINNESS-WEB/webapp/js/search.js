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

  this.elSearch.addEventListener("blur", function(e) {
    focusOut(e)
  }, true);
}

var focusOut = function(e) {
    if(e.relatedTarget===null) {
      document.querySelector(".onSearchForm").className = "searchForm";
      var elResult = document.querySelector(".searchResult");
      elResult.style.display="none";
      document.querySelector(".searchForm > input").value="";
      document.querySelector(".searchResult").innerHTML="<a></a>";
      var elIcon = document.querySelector(".searchForm i");
      elIcon.className = "fa fa-search";
    }
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

window.addEventListener('resize', function() {
  new searchForm()._setPosition();
}, false);

window.addEventListener('load', function() {
  var elSearch = document.querySelector(".searchForm");
    var searchResultBox = document.querySelector(".searchFormTemplate").text;
  new searchForm(elSearch, searchResultBox).init();

  elSearch.addEventListener("keyup", function(ev) {
    var sText = document.querySelector("#searchText").value;
    if (sText.replace(/\s/gm, "").length>0) {
      sText = sText.replace(/^\s{1,}|\s{1,}$/, "");
      guinness.ajax({
          method:"get",
          url : "/search?words=" + sText,
          success : function(req) {
            json = JSON.parse(req.responseText);
            var elSearchResult=document.querySelector(".searchResult");
            elSearchResult.innerHTML="<a></a>"
            searchResult(json);
          }
      });
    }
  }, false);
}, false);

function searchResult(json){
  for(var i = 0; i < json.listValues.length; i++){
    var elSearchResult=document.querySelector(".searchResult > a:last-child");
    var hlSearchResultTemplate = document.querySelector(".searchResultTemplate").text;
    elSearchResult.insertAdjacentHTML("afterend", hlSearchResultTemplate);
    var elDiv = document.querySelector(".searchResultNoteId:last-child");
    jsonList = json.listValues[i];
    elDiv.id = "searchResultNoteId" + jsonList.noteId;
    document.querySelector("#searchResultNoteId" + jsonList.noteId).href="/search/n/"+jsonList.noteId;
    var elsearchResultText = document.querySelector("#searchResultNoteId" + jsonList.noteId+" > .searchResultText");
    if (jsonList.noteText.length > 20) {
      elsearchResultText.innerHTML = jsonList.noteText.slice(0,20)+"...";
    } else {
      elsearchResultText.innerHTML = jsonList.noteText;
    }
    var elsearchResultName = document.querySelector("#searchResultNoteId" + jsonList.noteId+" > .searchResultName");
    elsearchResultName.innerHTML = jsonList.userName;
    var elsearchResultDate = document.querySelector("#searchResultNoteId" + jsonList.noteId+" > .searchResultDate");
    elsearchResultDate.innerHTML = jsonList.targetDate;
    var elsearchResultGroup = document.querySelector("#searchResultNoteId" + jsonList.noteId+ " > .searchResultGroup");
    elsearchResultGroup.innerHTML = jsonList.groupName;
  }
}

