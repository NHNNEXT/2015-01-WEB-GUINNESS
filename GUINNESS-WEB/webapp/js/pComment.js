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

pComment.appendPComment = function (json) {
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
        .replace("createDate", json.pCommentCreateDate)
        .replace("selectedText", json.selectedText);
    pCommentList.insertAdjacentHTML("beforeend", elPComment);
    var PCommentCard = document.body.querySelector(".pCommentList #pCId" + json.pCommentId);
    PCommentCard.addEventListener('mouseover', pComment.highlite, false);
    PCommentCard.addEventListener('mouseleave', function (e) {
        var info = e.target.closest("li").querySelector("input[type=hidden]");
        var p = document.body.querySelector('#pId-' + info.getAttribute('ptagid'));
        var highlighted = p.querySelector('.highlighted');
        if (null !== highlighted) {
            p.innerHTML = p.innerHTML.replace(/<span class="highlighted">.+<\/span>/, highlighted.innerHTML);
        }
    }, false);
    pCommentList.scrollTop = pCommentList.scrollHeight;
    pComment.countByP(document.querySelector('.hiddenNoteId').value);
}

pComment.highlite = function (e) {
    var info = e.target.closest("li").querySelector("input[type=hidden]");
    var pId = info.getAttribute('ptagid');
    var sameSenCount = info.getAttribute('samecount');
    var sameSenIndex = Number(info.getAttribute('sameindex'));
    var selectedText = info.getAttribute('selecttext');
    var p = document.body.querySelector('#pId-' + pId);
    var cloneSeletedText = selectedText;
    cloneSeletedText = cloneSeletedText.replace(/^<strong class="attention">/, "");
    cloneSeletedText = cloneSeletedText.replace(/^<strong class="question">/, "");
    cloneSeletedText = cloneSeletedText.replace(/<\/strong>$/, "");
    var count = 0;
    var index = 0;
    do {
        count++;
        index = p.innerHTML.indexOf(cloneSeletedText);
    } while (index !== -1 && count < sameSenIndex);

    if (p.innerHTML.search('<span class="highlighted">') < 0 && index !== -1) {
        p.innerHTML = p.innerHTML.slice(0, index) + "<span class='highlighted'>"
            + cloneSeletedText + "</span>" + p.innerHTML.slice(index + cloneSeletedText.length);
    }
}

pComment.countByP = function (noteId) {
    guinness.ajax({
        method: "get",
        url: "/pComments/readCountByP?noteId=" + noteId,
        success: function (req) {
            var result = JSON.parse(req.responseText);
            if (result.success !== true) {
                return false;
            }
            pComment.countByP.createBulbBtn(result.mapValues);
        }
    });
}

pComment.countByP.createBulbBtn = function (json) {
    for (var index in json) {
        var pCommentCount = (json[index])['count(1)'];
        var showBtn = pComment.countByP.createBulbBtn.getShowBtnByPId(json[index].pId);
        if (showBtn === false) { return false; }
        showBtn.style.display = "block";
        showBtn.addEventListener('mouseup', function(e) {
            e.preventDefault;
            var noteId = document.body.querySelector(".hiddenNoteId").value;
            var pOrPreId = e.target.closest('P') !== null ? e.target.closest('P').id : e.target.closest('PRE').id;
            var noteContent = document.querySelector('.note-content');
            createPCommentListBox(pOrPreId, noteContent, noteId);
        }, false);
    }
}

pComment.countByP.createBulbBtn.getShowBtnByPId = function (pId) {
    var showBtns = document.body.querySelectorAll(".showPComment");
    if (showBtns.length <= 0) { return false; }
    for (var index=0; index < showBtns.length; index++) {
        if (showBtns[index] === null) { break }
        var pOrPreId = showBtns[index].closest('P') !== null ? showBtns[index].closest('P').id : showBtns[index].closest('PRE').id;
        if (pOrPreId === "pId-"+pId) {
            return showBtns[index];
        }
    }
    return false;
}

pComment.selectText = function () {
    var range = document.getSelection().getRangeAt(0);
    var content = range.cloneContents();
    var elTemp = document.createElement('SPAN');
    elTemp.appendChild(content);
    var selectedText = elTemp.innerHTML;
    if (selectedText.length > 0) {
        return selectedText.replace(/^<strong class="ShowPComment">.{1,}<\/strong>/, "");
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
}

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
                if (event.target.className !== "fa fa-lightbulb-o" && event.target.className !== "ShowPComment") {
                    refresh();
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
        document.body.querySelector(".selected").className = "none";
        refresh();
        pComment.listRemover();
    }, false);
}

function refresh() {
    var noteContent = document.body.querySelector(".note-content");
    noteContent.innerHTML = document.body.querySelector(".hidden-note-content").value;
    pComment.countByP(document.querySelector('.hiddenNoteId').value);
}

function createPCommentListBox(pId, noteContent, noteId) {
    var regacyBox = document.body.querySelector(".pCommentListBox");
    if (regacyBox !== null) {
        regacyBox.remove();
    }
    var noteContent = document.body.querySelector(".markdown-body .note-content");
    noteContent.style.float = "left";
    var pCommentList = document.querySelector(".pCommentListTemplate").text;
    noteContent.insertAdjacentHTML("afterend", pCommentList);
    document.body.querySelector("#pCommentBoxCancel").addEventListener('click', pComment.listRemover, false);
    guinness.ajax({
        method: "GET",
        url: "/pComments?pId=" + pId + "&noteId=" + noteId,
        success: function (req) {
            var result = JSON.parse(req.responseText);
            if (result.success !== true) {
                return;
            }
            var pCommentList = document.body.querySelector(".pCommentList");
            pCommentList.innerHTML = "";
            for (var index in result.objectValues) {
                pComment.appendPComment(result.objectValues[index]);
            }
            var pCommentList = document.body.querySelector(".pCommentList");
            pCommentList.scrollTop = 0;
        }
    });
}

pComment.listRemover = function () {
    var pCommentListBox = document.body.querySelector(".pCommentListBox");
    if (pCommentListBox !== null) {
        pCommentListBox.remove();
    }
    var noteContent = document.body.querySelector(".markdown-body .note-content");
    noteContent.style.float = "";
}

pComment.createPComment = function () {
    document.body.querySelector(".pCommentBox").style.display = "none";
    var inputP = document.body.querySelector(".inputP");
    pComment.pCommentText = inputP.innerText;
    inputP.innerText = "";
    document.body.querySelector(".selected").className = "none";
    refresh();
    if (pComment.pCommentText.length < 1) {
        return false;
    }
    var pId = pComment.pId.replace("pId-", "");
    guinness.ajax({
        method: "post",
        url: "/pComments",
        param: "pId=" + pId + "&sameSenCount=" + pComment.sameSenCount + "&sameSenIndex=" + pComment.sameSenIndex
        + "&pCommentText=" + pComment.pCommentText + "&selectedText=" + pComment.selectedText
        + "&noteId=" + pComment.noteId,
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
        var selectedText = pComment.selectText();
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

function getPid(selectedElClass) {
    while (selectedElClass.tagName !== "P" && selectedElClass.tagName !== "PRE") {
        selectedElClass = selectedElClass.parentNode;
    }
    return selectedElClass.id;
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
