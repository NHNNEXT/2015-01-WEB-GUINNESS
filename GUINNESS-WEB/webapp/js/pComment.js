var mousePosition = {};
mousePosition.downPoint = {
    x: 0,
    y: 0
};
mousePosition.upPoint = {
    x: 0,
    y: 0
};

var pComment = {
    pCommentText: null,
    selectedText: null,
    sameSenCount: 0,
    sameSenIndex: 0,
    userId: null,
    pId: null,
    noteId: null
};

function updatePComment(pCommentId, commentText) {
    guinness.restAjax({
            method: "put",
            url: "/pComments/" + pCommentId,
            param: "commentText=" + commentText,
	        statusCode: {
	    		201 : function (res) {	//수정 성공
	                var result = JSON.parse(res);
	                var el = document.getElementById("pCId"+pCommentId);
	                el.querySelector('.pComment-text').innerHTML = result.pCommentText.replace(/\n/g, '<br/>');
	                el.querySelector('.pCommentCreateDate').innerHTML = result.pCommentCreateDate;
	                el.querySelector('.pComment-text').setAttribute('contentEditable', false);
	                el.querySelectorAll('.comment-update').remove();
	                el.querySelector('.update').style.display="inline-block";
	                el.querySelector('.delete').style.display="inline-block";
	            },
            	412 : function(res) {	// 수정 실패
            		return;
            	}
	    	}
        });
}

pComment.reloadCountByP = function (pId, noteId) {
    guinness.restAjax({
        method: "get",
        url: "/pComments/readCountByP?noteId=" + noteId,
    	statusCode: {
    		200 : function (res) {
                var objs = JSON.parse(res);
                objs.forEach(function (obj) {
                    if (obj.pId === pId*1) {
                        pComment.reloadCountByP.refreshBulbBtn(pId, obj['count(1)']); 
                    }
                });
            }
    	}
    });
};

pComment.reloadCountByP.refreshBulbBtn = function (pId, count) {
    var showBtn = document.body.querySelector(".showPComment[pid='pId-"+pId+"']");
    if (showBtn.querySelector('i').innerHTML === "") {
        showBtn.addEventListener('mouseup', function(e) {
            var noteId = document.body.querySelector(".hiddenNoteId").value;
            var pOrPreId = e.target.closest('.showPComment').getAttribute('pid');
            var noteContent = document.querySelector('.note-content');
            createPCommentListBox(pOrPreId, noteContent, noteId);
        }, false);
    }
    if (count*1 > 0) {
        if (showBtn.style.display === "none" || showBtn.style.display === "") {
            showBtn.style.display = "block";
        }
        showBtn.querySelector("i").innerHTML = count;
    }
    return false;
};

pComment.appendPComment = function (json, userId) {
    var date = guinness.util.koreaDate(Number(new Date(json.pCommentCreateDate)));
    var pCommentList = document.body.querySelector(".pCommentList");
    var elPComment = document.querySelector(".aPCommentTemplate").text;
    elPComment = elPComment.replace("pId", json.pId)
        .replace("pCommentId", "pCId" + json.pCommentId)
        .replace("sameSenCount", json.sameSenCount)
        .replace("sameSenIndex", json.sameSenIndex)
        .replace("userImage", "/img/profile/" + json.sessionUser.userImage)
        .replace("userId", "(" + json.sessionUser.userId + ")")
        .replace("userName", json.sessionUser.userName)
        .replace("pCommentText", json.pCommentText)
        .replace("createDate", date)
        .replace("selectedText", json.selectedText)
        .replace("deletePComment()", 'pComment.deletePComment('+json.pCommentId+')');
    pCommentList.insertAdjacentHTML("beforeend", elPComment);
    var PCommentCard = document.body.querySelector(".pCommentList #pCId" + json.pCommentId);
    if (json.sessionUser.userId === userId) {
        PCommentCard.querySelector(".controll").style.display = "block";
    }
    PCommentCard.addEventListener('mouseover', pComment.highlight, false);
    PCommentCard.addEventListener('mouseleave', pComment.clearHighlight, false);
    pCommentList.scrollTop = pCommentList.scrollHeight;
    pComment.reloadCountByP(json.pId, json.note.noteId);

    document.getElementById("pCId"+json.pCommentId).querySelector(".update").addEventListener("click", function(e) {
    	var el = e.target.parentElement.parentElement;
    	var pCommentText = el.querySelector('.pComment-text').innerHTML;
    	var pCommnetId = el.id.slice(4);
    	el.querySelector('.update').hide();
    	el.querySelector('.delete').hide();
    	el.querySelector('.pComment-text').setAttribute('contentEditable', true);
    	
        var updateButton = guinness.createElement({
            name: "a",
            attrs: {
                'class': "comment-update"
            },
            content: "확인"
        });
        var cancelButton = guinness.createElement({
            name: "a",
            attrs: {
                'class': "comment-update"
            },
            content: "취소"
        });
        
        updateButton.addEventListener('click', function(e) {
        	var el = e.target.parentElement.parentElement;
        	var commentText = el.querySelector('.pComment-text').innerHTML;
        	var pCommnetId = el.id.slice(4);
            updatePComment(pCommnetId, commentText);
        }, false);
        cancelButton.addEventListener('click', function(e) {
        	var el = e.target.parentElement.parentElement;
        	el.querySelector('.pComment-text').innerHTML = pCommentText.replace(/\n/g, '<br/>');
        	el.querySelector('.pComment-text').setAttribute('contentEditable', false);
        	el.querySelectorAll('.comment-update').remove();
            el.querySelector('.update').style.display="inline-block";
            el.querySelector('.delete').style.display="inline-block";        	
        }, false);
        el.querySelector('.controll').appendChild(updateButton);
        el.querySelector('.controll').appendChild(cancelButton);
    }, false);
};

pComment.clearHighlight = function (e) {
    var info = e.target.closest("li").querySelector("input[type=hidden]");
    var p = document.body.querySelector('#pId-' + info.getAttribute('ptagid'));
    var highlighted = p.querySelector('.highlighted');
    pComment.refresh.removeHighlighting(highlighted, p);
};

pComment.highlight = function (e) {
    var info = e.target.closest("li").querySelector("input[type=hidden]");
    var pId = info.getAttribute('ptagid');
    var sameSenCount = info.getAttribute('samecount');
    var sameSenIndex = Number(info.getAttribute('sameindex'));
    var selectedText = info.getAttribute('selecttext');
    var p = document.body.querySelector('#pId-' + pId);
    var cloneSeletedText = selectedText;
    cloneSeletedText = cloneSeletedText.replace(/^<strong class="attention">/, "")
        .replace(/^<strong class="question">/, "")
        .replace(/^<strong>/, "").replace(/^<em>/, "").replace(/<\/em>$/, "").replace(/<\/strong>$/, "");
    var count = 0;
    var index = 0;
    index = p.innerHTML.indexOf(cloneSeletedText);
    while (index !== -1) {
        count++;
        if ( sameSenIndex <= count ) {
            break;
        }
        index = p.innerHTML.indexOf(cloneSeletedText, index+cloneSeletedText.length);
    }

    if (p.innerHTML.search('<span class="highlighted">') < 0) {
        p.innerHTML = p.innerHTML.slice(0, index) + "<span class='highlighted'>"
            + cloneSeletedText + "</span>" + p.innerHTML.slice(index + cloneSeletedText.length);
    }
};

pComment.countByP = function (noteId) {
    guinness.restAjax({
        method: "get",
        url: "/pComments/readCountByP?noteId=" + noteId,
    	statusCode: {
    		200 : function (res) {
                var result = JSON.parse(res);
                pComment.countByP.createBulbBtn(result);
            }
    	}
    });
};

pComment.countByP.createBulbBtn = function (json) {
	var sumOfpCommentCount = 0;
    for (var index in json) {
        var pCommentCount = (json[index])['count(1)'];
        var showBtn = pComment.countByP.createBulbBtn.getShowBtnByPId(json[index].pId);
        if (showBtn === false) {
            return false;
        }
        showBtn.style.display = "block";
        showBtn.querySelector("i").textContent = pCommentCount;
        pComment.countByP.setShowBtnEvent(showBtn);
        sumOfpCommentCount = sumOfpCommentCount + pCommentCount;
    }
};

pComment.countByP.setShowBtnEvent = function (showBtn) {
    showBtn.addEventListener('mouseup', function (e) {
        var noteId = document.body.querySelector(".hiddenNoteId").value;
        var pOrPreId = e.target.closest('.showPComment').getAttribute('pid');
        var noteContent = document.querySelector('.note-content');
        createPCommentListBox(pOrPreId, noteContent, noteId);
    }, false);
};

pComment.countByP.createBulbBtn.getShowBtnByPId = function (pId) {
    var showBtns = document.body.querySelectorAll(".showPComment");
    if (showBtns.length <= 0) {
        return false;
    }
    for (var index = 0; index < showBtns.length; index++) {
        if (showBtns[index] === null) {
            break
        }
        var pOrPreId = showBtns[index].getAttribute('pid');
        if (pOrPreId === "pId-" + pId) {
            return showBtns[index];
        }
    }
    return false;
};

pComment.getPid = function (selectedEl) {
    if (selectedEl === null) {
        console.error("nullError");
        return false;
    }
    return selectedEl.closest('P') !== null ? selectedEl.closest('P').id : selectedEl.closest('PRE').id;
};

pComment.selectText = function () {
    var range = document.getSelection().getRangeAt(0);
    if (range.endContainer.className === "fa fa-lightbulb-o" ||  range.endContainer.className === "showPComment") {
        return false;
    }
    var content = range.cloneContents();
    var elTemp = document.createElement('SPAN');
    elTemp.appendChild(content);
    var selectedText = elTemp.innerHTML;
    if (selectedText.length > 0) {
        return selectedText.replace(/^<strong class="ShowPComment">.{1,}<\/strong>/, "");
    }
    return false;
};

pComment.createPopupPCommentBtn = function () {
    var templatePopupBtn = document.querySelector("#popupCommentBtnTemplate").text;
    document.body.insertAdjacentHTML("beforeend", templatePopupBtn);
    _createPCommentBox();
    var popupCommentBtn = document.querySelector(".popupCommentBtn");
    mutateObserver(popupCommentBtn);
    popupCommentBtn.addEventListener('click', function (e) {
        pComment.listRemover();
        e.target.style.display = "none";
        var pCommentBox = document.querySelector(".pCommentBox");
        pCommentBox.style.display = "block";
        pCommentBox.style.top = e.target.style.top;
        pCommentBox.style.left = e.target.style.left;
        pCommentBox.querySelector(".inputP").focus();
        pCommentBox.addEventListener('dragend', dragEnd, false);
        var noteContent = document.body.querySelector(".note-content");
        createPCommentListBox(pComment.pId, noteContent, pComment.noteId);
    }, false);
};

function mutateObserver(popupCommentBtn) {
    var target = popupCommentBtn;
    var observer = new MutationObserver(function (mutations) {
        mutations.forEach(function (mutation) {
            if (mutation.type !== "attributes" || mutation.attributeName !== "style") {
                return;
            }
            if (mutation.target.style.display !== "none") {
                return;
            }
            var pCommentBoxDisplay = document.body.querySelector(".pCommentBox").style.display;
            if (pCommentBoxDisplay === "" || pCommentBoxDisplay === "none") {
                if (event.target.className !== "fa fa-lightbulb-o" && event.target.className !== "showPComment") {
                    pComment.refresh();
                }
            }
        });
    });
    var config = {attributes: true, childList: true, characterData: true};
    observer.observe(target, config);
}

function _createPCommentBox() {
    var pCommentTemplate = document.querySelector(".pCommentTemplate").text;
    document.body.insertAdjacentHTML("beforeend", pCommentTemplate);
    var pCommentBox = document.body.querySelector(".pCommentBox");
    pCommentBox.querySelector(".setUp").addEventListener("click", pComment.createPComment, false);
    pCommentBox.querySelector("#pCommentCancel").addEventListener("click", function (e) {
        e.target.parentElement.parentElement.style.display = "none";
        document.body.querySelector(".inputP").innerText = "";
        pComment.refresh();
        pComment.listRemover();
    }, false);
}

pComment.refresh = function () {
    var noteContent = document.body.querySelector(".note-content");
    var highlighteds = noteContent.querySelectorAll(".selected");
    for (var index in highlighteds) {
        if (index === "length") {
            return;
        }
        index = index*1;
        var pAndPre= [].slice.call(noteContent.querySelectorAll("p, pre"));
        if (pAndPre.length > 0) {
            pAndPre.forEach(function(elPOrPre){
                pComment.refresh.removeHighlighting(highlighteds[index], elPOrPre);
            });
        }
    }
};

pComment.refresh.removeHighlighting = function (element, targetContent) {
    if (undefined !== element && element !== null) {
        window.getSelection().removeAllRanges();
        targetContent.innerHTML = targetContent.innerHTML.replace(element.outerHTML, element.innerHTML);
    }
};

function createPCommentListBox(pId, noteContent, noteId) {
    var regacyBox = document.body.querySelector(".pCommentListBox");
    if (regacyBox !== null) {
        regacyBox.remove();
    }
    var noteContent = document.body.querySelector(".markdown-body .note-content");
    noteContent.style.float = "left";
    document.querySelector("form#commentForm").style.float = "left";
    document.querySelector("#commentListUl").style.float = "left";
    var pCommentListTemplate = document.querySelector(".pCommentListTemplate").text;
    noteContent.insertAdjacentHTML("afterend", pCommentListTemplate);
    setPositionPCommentListBox(noteContent, pId);
    document.body.querySelector("#pCommentBoxCancel").addEventListener('click', pComment.listRemover, false);
    guinness.restAjax({
  		method: "get",
  		url: "/pComments?pId=" + pId + "&noteId=" + noteId,
  		statusCode: {
  			200: function(res) {	// 생성 성공 
  				var result = JSON.parse(res);
  				var pCommentList = document.body.querySelector(".pCommentList");
  				pCommentList.innerHTML = "";
  				var userId = document.body.querySelector("#sessionUserId").value
  				var length = result.length;
  				for (var index = 0; index < length; index++) {
  					pComment.appendPComment(result[index], userId);
                  }
  				var pCommentList = document.body.querySelector(".pCommentList");
  				pCommentList.scrollTop = 0;
  			}
  		}
  	});
}

function setPositionPCommentListBox (noteContent, pId) {
    var pCommentListBox = document.body.querySelector(".pCommentListBox");
    var pRect = noteContent.querySelector("#"+pId).getBoundingClientRect();
    var markdownBodyRect = noteContent.parentNode.getBoundingClientRect();
    pCommentListBox.style.top = pRect.top - markdownBodyRect.top + "px";
}

pComment.listRemover = function () {
    var pCommentListBox = document.body.querySelector(".pCommentListBox");
    if (pCommentListBox !== null) {
        pCommentListBox.remove();
    }
    var noteContent = document.body.querySelector(".markdown-body .note-content");
    noteContent.style.float = "";
    document.querySelector("form#commentForm").style.float = "";
    document.querySelector("#commentListUl").style.float = "";
};

pComment.createPComment = function () {
    document.body.querySelector(".pCommentBox").style.display = "none";
    var inputP = document.body.querySelector(".inputP");
    pComment.pCommentText = inputP.innerText;
    inputP.innerText = "";
    document.body.querySelector(".selected").className = "none";
    pComment.refresh();
    if (pComment.pCommentText.length < 1) {
        return false;
    }
    var pId = pComment.pId.replace("pId-", "");
    guinness.restAjax({
  		method: "post",
  		url: "/pComments",
  		param: "pId=" + pId + "&sameSenCount=" + pComment.sameSenCount + "&sameSenIndex=" + pComment.sameSenIndex
        + "&pCommentText=" + pComment.pCommentText + "&selectedText=" + pComment.selectedText
        + "&noteId=" + pComment.noteId,
  		statusCode: {
  			201: function(res) {	// 생성 성공 
  				var result = JSON.parse(res);
  	            var userId =  document.body.querySelector("#sessionUserId").value;
  	            pComment.appendPComment(result, document.body.querySelector("#sessionUserId").value);
  			},
  			412: function(res) {	// 유효성 통과 못함
  				return;
  			}
  		}
  	});
};

pComment.deletePComment = function(pCommentId) {
	guinness.restAjax({
		method : "delete",
		url : "/pComments/" + pCommentId,
		statusCode : {
			204 : function (res) {
				var pid = "pId-" + document.querySelector("#pCId"+pCommentId+" input").getAttribute("ptagid");
	            document.querySelector(".showPComment[pid='"+pid+"'] i").innerText--;
	            document.querySelector("#pCId"+pCommentId).remove();
	            if(document.querySelector(".showPComment[pid='"+pid+"'] i").innerText === "0") {
	            	document.querySelector(".showPComment[pid='"+pid+"']").style.display = "none";
	            	document.querySelector(".pCommentListBox").remove();
	                pComment.listRemover();
	            }
			},
			412 : function (res) {
				return;
			}
		}
	});
};

function dragEnd(e) {
    e.preventDefault();
    var elTarget = document.querySelector(".pCommentBox");
    elTarget.style.left = e.clientX + "px";
    elTarget.style.top = e.clientY - e.target.clientHeight + "px";
}

function setPopupPCommentBtn() {
    var elNoteText = document.body.querySelector(".note-content");

    elNoteText.addEventListener('mousedown', function (e) {
        pComment.refresh();
        mousePosition.downPoint.x = e.clientX;
        mousePosition.downPoint.y = e.clientY;
    }, false);

    elNoteText.addEventListener('mouseup', function (e) {
        mousePosition.upPoint.x = e.clientX;
        mousePosition.upPoint.y = e.clientY;

        var left = mousePosition.upPoint.x < mousePosition.downPoint.x ? mousePosition.upPoint.x : mousePosition.downPoint.x;
        left += Math.abs(mousePosition.upPoint.x - mousePosition.downPoint.x);
        var top = mousePosition.upPoint.y;

        var elPopupBtn = document.querySelector(".popupCommentBtn");
        var selectedText = pComment.selectText();
        if (selectedText !== false) {
            var selectedEl = window.getSelection().getRangeAt(0).commonAncestorContainer;
            if (selectedText && selectedEl.className !== "note-content") {
                elPopupBtn.style.top = top + "px";
                elPopupBtn.style.left = left + "px";
                elPopupBtn.style.display = "block";
                pComment.selectedText = selectedText;
                pComment.pId = selectedEl.tagName === 'P' || selectedEl.tagName === 'PRE' ? selectedEl.id : pComment.getPid(selectedEl.parentElement);
                getSameSentence(pComment, selectedText, window.getSelection());
                getNoteInfo();
            } else {
                elPopupBtn.style.display = "none";
            }
        }
        if (selectedText === false) {
        	elPopupBtn.style.display = "none";
        }
    }, false);
}

function getNoteInfo() {
    pComment.userId = document.querySelector(".hiddenUserId").value;
    pComment.noteId = document.querySelector(".hiddenNoteId").value;
}

function getSameSentence(pComment, selectedText, selection) {
    var selectRange = selection.getRangeAt(0);
    var pId = pComment.pId;
    var pText = document.body.querySelector("#" + pId).innerText;
    var sameIndex = 1;
    var sameTexts = new Array();
    var sameText = pText.indexOf(selectedText);
    selectRange.insertNode(document.createTextNode("`'`ran"));
    var tempText = document.body.querySelector("#" + pId).innerText;
    var searchPrefix = tempText.indexOf("`'`ran");
    selectRange.deleteContents();
    selectRange.insertNode(document.createTextNode(selectedText));
    if (sameText === searchPrefix) {
        pComment.sameSenIndex = sameIndex;
    }
    while (sameText !== -1) {
        sameIndex += 1;
        sameTexts.push(sameText);
        sameText = pText.indexOf(selectedText, sameText + selectedText.length);
        if (sameText === searchPrefix) {
            pComment.sameSenIndex = sameIndex;
        }
    }
    pComment.sameSenCount = sameTexts.length;

    var span = document.createElement("SPAN");
    span.innerHTML = getSelection();
    span.className = "selected";
    selectRange.deleteContents();
    selectRange.insertNode(span);
}

//하이라이팅 none 관련 수정 중인 코드.
//function getSameSentence(pComment, selectedText, selection) {
//    var selectRange = selection.getRangeAt(0);
//    var pId = pComment.pId;
//    var pText = document.body.querySelector("#" + pId).innerText;
//    var sameIndex = 1;
//    var sameTexts = new Array();
//    var indexSelectedText = selectedText.replace(/^<strong class="attention">/, "")
//        .replace(/^<strong class="question">/, "")
//        .replace(/^<strong>/, "").replace(/^<em>/, "").replace(/<\/em>$/, "").replace(/<\/strong>$/, "");
//    var sameText = pText.indexOf(indexSelectedText);
//    selectRange.insertNode(document.createTextNode("`'`ran"));
//    var tempText = document.body.querySelector("#" + pId).innerText;
//    var searchPrefix = tempText.indexOf("`'`ran");
//    selectRange.deleteContents();
//    selectRange.insertNode(document.createTextNode(selectedText));
//    if (sameText === searchPrefix) {
//        pComment.sameSenIndex = sameIndex;
//    }
//    while (sameText !== -1) {
//        sameIndex += 1;
//        sameTexts.push(sameText);
//        sameText = pText.indexOf(indexSelectedText, sameText + indexSelectedText.length);
//        if (sameText === searchPrefix) {
//            pComment.sameSenIndex = sameIndex;
//        }
//    }
//    pComment.sameSenCount = sameTexts.length;
//
//    var span = document.createElement("SPAN");
//    span.innerHTML = getSelection();
//    span.className = "selected";
//    selectRange.deleteContents();
//    selectRange.insertNode(span);
//}
