package org.nhnnext.guinness.model;

import java.util.ArrayList;


public class Preview {
	private Note note;
	private Group group;
	private ArrayList<String> attentionList;
	private ArrayList<String> questionList;

	public Preview(Note note, Group group, ArrayList<String> attentionText, ArrayList<String> questionText) {
		this.note = note;
		this.group = group;
		this.attentionList = attentionText;
		this.questionList = questionText;
	}

	@Override
	public String toString() {
		return "Preview [note=" + note + ", group=" + group + ", attentionText=" + attentionList + ", questionText="
				+ questionList + "]";
	}
}
