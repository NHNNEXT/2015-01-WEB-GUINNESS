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

function selectText() {
    var select = "";
    if (document.getSelection) {
        select = document.getSelection();
    } else if (document.selection) {
        select = document.selection.createRange().text;
    }
    var selectedText = select.toString();
    if (selectedText.length > 0) {
        return selectedText;
    }
    return false;
}

function createPopupPCommentBtn() {
    var templatePopupBtn = document.querySelector("#popupCommentBtnTemplate").text;
    document.body.insertAdjacentHTML("afterend", templatePopupBtn);

    var popupCommentBtn = document.querySelector(".popupCommentBtn");
    popupCommentBtn.addEventListener('click', function (e) {
        e.target.style.display = "none";
        // 코멘트 입력 창을 나타나게 하고
        // 이 코멘트 입력창에서 pComment 객체를 가져다가 서버와 통신하게 할 것.

    }, false);
}

function setPopupPCommentBtn() {
    var elNoteText = document.body.querySelector(".note-content");

    //elNoteText.addEventListener('mousedown', function (e) {
    //    mousePosition.downPoint.x = e.clientX;
    //    mousePosition.downPoint.y = e.clientY;
    //}, false);

    elNoteText.addEventListener('mouseup', function (e) {
        //mousePosition.upPoint.x = e.clientX;
        //mousePosition.upPoint.y = e.clientY;

        //var left = mousePosition.upPoint.x < mousePosition.downPoint.x ? mousePosition.upPoint.x : mousePosition.downPoint.x;
        //left += Math.abs(mousePosition.upPoint.x - mousePosition.downPoint.x);
        //var top = mousePosition.upPoint.y;

        var elPopupBtn = document.querySelector(".popupCommentBtn");
        var selectedText = selectText();
        var selectedRect = window.getSelection().getRangeAt(0).getBoundingClientRect();
        var selectedElClass = window.getSelection().getRangeAt(0).commonAncestorContainer;
        if (selectedText && selectedElClass.className !== "note-content") {
            elPopupBtn.style.top = (selectedRect.top-30) + "px";
            elPopupBtn.style.left = ((selectedRect.left+selectedRect.right)/2)-31 + "px";
            elPopupBtn.style.display = "block";
            pComment.selectedText = selectedText;
            pComment.pId = getPid(selectedElClass);
            getSameSentence(pComment, selectedText, window.getSelection());
            getNoteInfo();
        } else {
            elPopupBtn.style.display = "none";
        }
    }, false);

    // <span class="highlighted"></span> 를 document.body.innerText.search("") 를
    // 사용해 찾고, 각 뒤, 앞 순서로 삽입한다.
    // 이후, 해당 클래스에 색을 없게 주고, 코멘트에 hover 시, 색이 있게 한다.
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
}