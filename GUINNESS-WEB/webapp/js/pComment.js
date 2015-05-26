var mousePosition = {};
mousePosition.downPoint = {
    x: 0,
    y: 0
};
mousePosition.upPoint = {
    x: 0,
    y: 0
};

dragBox = {
    top : 0,
    left: 0
}

var pComment = {
    pCommentText: null,
    selectedText: null,
    sameSenCount: 0,
    sameSenIndex: 0,
    userId: null,
    pId: null,
    noteId: null
};

pComment.appendPComment = function (json) {
    var date = guinness.util.koreaDate(Number(new Date(json.pCommentCreateDate)));
    var pCommentList = document.body.querySelector(".pCommentList");
    var elPComment = document.querySelector(".aPCommentTemplate").text;
    elPComment = elPComment.replace("pId", json.pId)
                .replace("pCommentId", json.pCommentId)
                .replace("sameSenCount", json.sameSenCount)
                .replace("sameSenIndex", json.sameSenIndex)
                .replace("userImage", "/img/profile/"+json.sessionUser.userImage)
                .replace("userId", "("+json.sessionUser.userId+")")
                .replace("userName", json.sessionUser.userName)
                .replace("pCommentText", json.pCommentText)
                .replace("createDate", json.pCommentCreateDate);
    pCommentList.insertAdjacentHTML("beforeend", elPComment);
    pCommentList.scrollTop = pCommentList.scrollHeight;
}

function selectText() {
    var select = document.getSelection();
    var range = select.getRangeAt(0);
    var content = range.cloneContents();
    var span = document.createElement('SPAN');
    span.appendChild(content);
    var selectedText = span.innerHTML;
    if (selectedText.length > 0) {
        return selectedText;
    }
    return false;
}

function createPopupPCommentBtn() {
    var templatePopupBtn = document.querySelector("#popupCommentBtnTemplate").text;
    document.body.insertAdjacentHTML("beforeend", templatePopupBtn);
    _createPCommentBox();
    var popupCommentBtn = document.querySelector(".popupCommentBtn");
    mutateObserver(popupCommentBtn);
    popupCommentBtn.addEventListener('click', function (e) {
        pCommentListRemover();
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
}

function mutateObserver (popupCommentBtn) {
    var target = popupCommentBtn;
    var observer = new MutationObserver(function(mutations) {
        mutations.forEach(function(mutation) {
            if (mutation.type === "attributes" && mutation.attributeName === "style") {
                if (mutation.target.style.display === "none" ) {
                    var pCommentBoxDisplay = document.body.querySelector(".pCommentBox").style.display;
                    if (pCommentBoxDisplay === "" || pCommentBoxDisplay === "none" ) {
                        if (event.target.className !== "fa fa-lightbulb-o" && event.target.className !== "ShowPComment") {
                            refresh();
                        }
                    }
                }
            }
        });
    });
    var config = { attributes: true, childList: true, characterData: true };
    observer.observe(target, config);
}

function _createPCommentBox () {
    var pCommentTemplate = document.querySelector(".pCommentTemplate").text;
    document.body.insertAdjacentHTML("beforeend", pCommentTemplate);
    var pCommentBox = document.body.querySelector(".pCommentBox");
    pCommentBox.querySelector(".setUp").addEventListener("click", createPComment, false);
    pCommentBox.querySelector("#pCommentCancel").addEventListener("click", function (e) {
        e.target.parentElement.parentElement.style.display = "none";
        document.body.querySelector(".inputP").innerText = "";
        document.body.querySelector(".highlighted").className = "none";
        refresh();
        pCommentListRemover();
    }, false);
}

function refresh() {
    var noteContent = document.body.querySelector(".note-content");
    noteContent.innerHTML = document.body.querySelector(".hidden-note-content").value;
    arShowP = noteContent.querySelectorAll(".ShowPComment");
    for(var index in arShowP) {
        if (index === "length") {
            return;
        }
        arShowP[index].innerHTML = "<i class='fa fa-lightbulb-o'></i>";
        
        arShowP[index].addEventListener('click', function (e) {
            e.preventDefault;
            var noteId = document.body.querySelector(".hiddenNoteId").value;
            var pId = e.target.closest("P").id;
            if (pId.indexOf("pId-") === -1) {
                pId = e.target.closest("PRE").id;
            }
            createPCommentListBox(pId, noteContent, noteId);
        }, false);
    }
}

function createPCommentListBox(pId, noteContent, noteId) {
    var regacyBox = document.body.querySelector(".pCommentListBox");
    if (regacyBox !== null ) {
        regacyBox.remove();   
    }
    var noteContent = document.body.querySelector(".markdown-body .note-content");
    noteContent.style.float = "left";
    var pCommentList = document.querySelector(".pCommentListTemplate").text;
    noteContent.insertAdjacentHTML("afterend", pCommentList);
    document.body.querySelector("#pCommentBoxCancel").addEventListener('click', pCommentListRemover, false);
    guinness.ajax({
        method : "GET",
        url : "/pComments?pId="+pId+"&noteId="+noteId,
        success: function (req) {
            var result = JSON.parse(req.responseText);
            if (result.success !== true) {
                return;
            }
            for(var index in result.objectValues ) {
                pComment.appendPComment(result.objectValues[index]);
            }
        }
    });
}

function pCommentListRemover() {
    var pCommentListBox = document.body.querySelector(".pCommentListBox");
    if (pCommentListBox !== null ) {
        pCommentListBox.remove();
    }
    var noteContent = document.body.querySelector(".markdown-body .note-content");
    noteContent.style.float = "";
}

function createPComment () {
    document.body.querySelector(".pCommentBox").style.display = "none";
    var inputP = document.body.querySelector(".inputP");
    pComment.pCommentText = inputP.innerText;
    inputP.innerText = "";
    document.body.querySelector(".highlighted").className = "none";
    refresh();
    if(pComment.pCommentText.length < 1) {
        return false;
    }
    var pId = pComment.pId.replace("pId-", "");
    guinness.ajax({
        method : "post",
        url : "/pComments",
        param : "pId="+pId+"&sameSenCount="+pComment.sameSenCount+"&sameSenIndex="+pComment.sameSenIndex
                +"&pCommentText="+pComment.pCommentText+"&selectedText="+pComment.selectedText
                +"&noteId="+pComment.noteId,
        success: function (req) {
            var result = JSON.parse(req.responseText);
            if (result.success !== true) {
                return;
            }
            pComment.appendPComment(result.object);
        }
    });
}

function dragEnd(e) {
    e.preventDefault();
    var elTarget = document.querySelector(".pCommentBox");
    elTarget.style.left = e.clientX + "px";
    elTarget.style.top = e.clientY - e.target.clientHeight + "px";
}

function setPopupPCommentBtn() {
    var elNoteText = document.body.querySelector(".note-content");

    elNoteText.addEventListener('mousedown', function (e) {
        
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
        var selectedText = selectText();
        var selectedElClass = window.getSelection().getRangeAt(0).commonAncestorContainer;
        if (selectedText && selectedElClass.className !== "note-content") {
            elPopupBtn.style.top = top + "px";
            elPopupBtn.style.left = left + "px";

            elPopupBtn.style.display = "block";
            pComment.selectedText = selectedText;
            pComment.pId = getPid(selectedElClass);
            getSameSentence(pComment, selectedText, window.getSelection());
            getNoteInfo();
        } else {
            elPopupBtn.style.display = "none";
        }
    }, false);
}

function getNoteInfo() {
    pComment.userId = document.querySelector(".hiddenUserId").value;
    pComment.noteId = document.querySelector(".hiddenNoteId").value;
}

function getPid (selectedElClass) {
    while (selectedElClass.tagName !== "P" && selectedElClass.tagName !== "PRE") {
        selectedElClass = selectedElClass.parentNode;
    }
    return selectedElClass.id;
}

function getSameSentence (pComment, selectedText, selection) {
    var selectRange = selection.getRangeAt(0);
    var pId = pComment.pId;
    var pText = document.body.querySelector("#"+pId).innerText;
    var sameIndex = 1;
    var sameTexts = new Array();
    var sameText = pText.indexOf(selectedText);
    selectRange.insertNode(document.createTextNode("`'`ran"));
    var tempText = document.body.querySelector("#"+pId).innerText;
    var searchPrefix = tempText.indexOf("`'`ran");
    selectRange.deleteContents();
    selectRange.insertNode(document.createTextNode(selectedText));
    if (sameText === searchPrefix) {
        pComment.sameSenIndex = sameIndex;
    }
    while(sameText !== -1){
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
    span.className = "highlighted";
    selectRange.deleteContents();
    selectRange.insertNode(span);
}