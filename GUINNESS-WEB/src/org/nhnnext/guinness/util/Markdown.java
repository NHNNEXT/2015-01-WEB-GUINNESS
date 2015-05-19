package org.nhnnext.guinness.util;

import java.io.IOException;

import org.markdown4j.Markdown4jProcessor;

public class Markdown {
	public String toHTML(String markdownText) throws IOException {
		return new Markdown4jProcessor().process(markdownText);
	}
}
