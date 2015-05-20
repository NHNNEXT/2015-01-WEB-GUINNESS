package org.nhnnext.guinness.util;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.markdown4j.Markdown4jProcessor;

public class Markdown {
	public String toHTML(String markdownText) throws IOException {
		markdownText = attention(markdownText);
		markdownText = question(markdownText);
		return new Markdown4jProcessor().process(markdownText);
	}
	
	private String attention(String markdownText) {
		Pattern pattern = Pattern.compile("!{3}[^\n]{1,}!{3}");
		Matcher matcher = pattern.matcher(markdownText);
		while (matcher.find()) {
			String change = matcher.group().replaceAll("^!!!", "<span class='attention'><i class='fa fa-exclamation-circle'></i>")
					.replaceAll("!!!$", "</span>");
			markdownText = markdownText.replace(matcher.group(), change);
		}
		return markdownText;
	}
	
	private String question(String markdownText) {
		Pattern pattern = Pattern.compile("\\?{3}[^\n]{1,}\\?{3}");
		Matcher matcher = pattern.matcher(markdownText);
		while (matcher.find()) {
			String change = matcher.group().replaceAll("^\\?\\?\\?", "<span class='question'><i class='fa fa-question-circle'></i>")
					.replaceAll("\\?\\?\\?$", "</span>");
			markdownText = markdownText.replace(matcher.group(), change);
		}
		return markdownText;
	}
}
