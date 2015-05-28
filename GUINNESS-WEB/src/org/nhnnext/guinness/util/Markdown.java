package org.nhnnext.guinness.util;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.markdown4j.Markdown4jProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Markdown {
	
	private static final Logger logger = LoggerFactory.getLogger(Markdown.class);
	
	public String toHTML(String markdownText) throws IOException {
		markdownText = attention(markdownText);
		markdownText = question(markdownText);
		return pIdNumbering(new Markdown4jProcessor().process(markdownText));
	}
	
	private String pIdNumbering(String textHtml) {
		textHtml = headerAndListContainPre(textHtml);
		int idNumber = 1;
		int pIndex=0, preIndex=0;
		String bulbBtn = "";
		while(true) {
			bulbBtn = "<strong class='showPComment' pId='pId-"+idNumber+"'><i class='fa fa-lightbulb-o'></i></strong>";
			pIndex = textHtml.indexOf("<p>");
			preIndex = textHtml.indexOf("<pre>");
			if( pIndex == -1 && preIndex == -1 ) {
				return textHtml;
			}
			if( pIndex == -1 && preIndex >= 0){
				textHtml = textHtml.replaceFirst("<pre>", bulbBtn+"<pre id='pId-"+idNumber+"' class='pCommentText'>");
			}
			if( pIndex >= 0 && preIndex == -1 ){
				textHtml = textHtml.replaceFirst("<p>", bulbBtn+"<p id='pId-"+idNumber+"' class='pCommentText'>");
			}
			if( pIndex >= 0 && preIndex >= 0 ){
				if( pIndex < preIndex ){
					textHtml = textHtml.replaceFirst("<p>", bulbBtn+"<p id='pId-"+idNumber+"' class='pCommentText'>");
				}
				else {
					textHtml = textHtml.replaceFirst("<pre>", bulbBtn+"<pre id='pId-"+idNumber+"' class='pCommentText'>");
				}
			}
			idNumber++;
		}
	}
	
	private String headerAndListContainPre(String textHtml) {
		return textHtml.replaceAll("<ul>", "<pre><ul>").replaceAll("</ul>", "</ul></pre>")
				.replaceAll("<ol>", "<pre><ol>").replaceAll("</ol>", "</ol></pre>")
				.replaceAll("<h1>", "<pre><h1>").replaceAll("</h1>", "</h1></pre>")
				.replaceAll("<h2>", "<pre><h2>").replaceAll("</h2>", "</h2></pre>")
				.replaceAll("<h3>", "<pre><h3>").replaceAll("</h3>", "</h3></pre>")
				.replaceAll("<h4>", "<pre><h4>").replaceAll("</h4>", "</h4></pre>")
				.replaceAll("<h5>", "<pre><h5>").replaceAll("</h5>", "</h5></pre>")
				.replaceAll("<h6>", "<pre><h6>").replaceAll("</h6>", "</h6></pre>");
	}

	private String attention(String markdownText) {
		Pattern pattern = Pattern.compile("!{3}[^\n]{1,}!{3}");
		Matcher matcher = pattern.matcher(markdownText);
		while (matcher.find()) {
			String change = matcher.group().replaceAll("^!!!", "<strong class='attention'>")
					.replaceAll("!!!$", "</strong>");
			markdownText = markdownText.replace(matcher.group(), change);
		}
		return markdownText;
	}
	
	private String question(String markdownText) {
		Pattern pattern = Pattern.compile("\\?{3}[^\n]{1,}\\?{3}");
		Matcher matcher = pattern.matcher(markdownText);
		while (matcher.find()) {
			String change = matcher.group().replaceAll("^\\?\\?\\?", "<strong class='question'>")
					.replaceAll("\\?\\?\\?$", "</strong>");
			markdownText = markdownText.replace(matcher.group(), change);
		}
		return markdownText;
	}
	
	
	
}
