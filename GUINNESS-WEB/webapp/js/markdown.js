markdownToHtml = function(text) {
	this.text = text;
	return this;
}

markdownToHtml.prototype.bold = function () {
	var array = this.text.match(/\*{2}[^\*{2}]{1,}\*{2}|\_{2}[^\_{2}]{1,}\_{2}/g);
	for(var i in array) {
		var htmlText = array[i].replace(/^\*{2}|^\_{2}/, '<span class="bold">').replace(/\*{2}$|\_{2}$/, '</span>');
		this.text = this.text.replace(array[i], htmlText);
	}
	return this;
}

markdownToHtml.prototype.italic = function() {
	var array = this.text.match(/\*[^\*\n]{1,}\*|\_[^\_\n]{1,}\_/g);
	for(var i in array) {
		var htmlText = array[i].replace(/^\*|^\_/, '<span class="italic">').replace(/\*$|\_/, '</span>');
		this.text = this.text.replace(array[i], htmlText);
	}
	return this;
}

markdownToHtml.prototype.image = function() {
	var array = this.text.match(/\!\[[^\n]{0,}\]\([^\n]{1,}\)/g);
	for(var i in array) {
		var htmlText = array[i].replace('![', '<img alt="').replace('](', '" src="').replace(')', '" >');
		this.text = this.text.replace(array[i], htmlText);
	}
	return this;
}

markdownToHtml.prototype.link = function() {
	var array = this.text.match(/\[[^\n]{0,}\]\([^\n]{1,}\)/g);
	for(var i in array) {
		var linkText = array[i].match(/\[.{0,}\]/)[0].replace('[', '').replace(']', '');
		var htmlText = array[i].replace(/\[.{0,}\]\(/, '<a href="')
		if (linkText.length===0) {
			htmlText = htmlText.replace(')', '" />');
		} else {
			htmlText = htmlText.replace(')', '">'+linkText+"</a>");
		}
		this.text = this.text.replace(array[i], htmlText);
	}
	return this;
}

markdownToHtml.prototype.header = function() {
	var array = this.text.match(/#{1,}\s[^\n]{1,}(\n|$)/g);
	for(var i in array) {
		var shop = array[i].split(' ')[0];
		var shopCount = shop.length;
		var htmlText = array[i].replace(shop, "<h"+shopCount+">").replace(/\n|#/,"")+"</h"+shopCount+">\n";
		this.text = this.text.replace(array[i], htmlText);
	}
	return this;
}

markdownToHtml.prototype.tag = function() {
	var array = this.text.match(/#{1,}[^#\n\s]{1,}(\n|\s|$)/g);
	for(var i in array) {
		var htmlText = array[i].replace('#', '<span class="tag">#')+'</span>';
		this.text = this.text.replace(array[i], htmlText);
	}
	return this;
}

markdownToHtml.prototype.attention = function() {
	var array = this.text.match(/!{3,}[^\n]{1,}!{3,}/g);
	for(var i in array) {
		var htmlText = array[i].replace(/^!{3}/, '<span class="attention"><i class="fa fa-exclamation-circle"></i>');
		htmlText = htmlText.replace(/!{3}$/, '</span>');
		this.text = this.text.replace(array[i], htmlText);
	}
	return this;
}

markdownToHtml.prototype.question = function() {
	var array = this.text.match(/\?{3,}[^\n]{1,}\?{3,}/g);
	for(var i in array) {
		var htmlText = array[i].replace(/^\?{3}/, '<span class="question"><i class="fa fa-question-circle"></i>');
		htmlText = htmlText.replace(/\?{3}$/, '\</span>');
		this.text = this.text.replace(array[i], htmlText);
	}
	return this;
}

markdownToHtml.prototype.newline = function() {
	this.text = this.text.replace(/^/, '<p>');
	var array = this.text.match(/\n{2,}|\s{2,}\n/g);
	for(var i in array) {
		var htmlText = array[i].replace(/\n{2,}|\s{2,}\n/g, '</p>\n<p>');
		this.text = this.text.replace(array[i], htmlText);
	}
	this.text = this.text+'</p>';
	return this;
}

markdownToHtml.prototype.code = function () {
    var array = this.text.match(/`{3}(.|[\n]|[\s])+`{3}/g);
    for ( var i in array ) {
        var htmlText = array[i].replace(/\n/g, '<br/>');
        htmlText = htmlText.replace(/`{3}$/, '</p>');
        htmlText = htmlText.replace(/\t/g, '&nbsp;&nbsp;&nbsp;&nbsp;');
        htmlText = htmlText.replace(/\s/g, '&nbsp;');
        htmlText = htmlText.replace(/^`{3}/, '<p class="code">');
        this.text =this.text.replace(array[i], htmlText);
    }
    return this;
}

markdownToHtml.prototype.getHtmlText = function() {
	return this.tag().bold().italic().image().link().header().attention().question().code().newline().text.replace(/<p><\/p>/,"\n");
}