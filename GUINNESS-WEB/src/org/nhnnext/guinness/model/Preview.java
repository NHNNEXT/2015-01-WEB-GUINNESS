package org.nhnnext.guinness.model;


public class Preview {
	private Note note;
	private User user;
	private Group group;
	private String attentionList;
	private String questionList;
	
	public Preview(Note note, User user, Group group, String attentionList, String questionList) {
		this.note = note;
		this.user = user;
		this.group = group;
		this.attentionList = attentionList;
		this.questionList = questionList;
	}
	
	public Note getNote() {
		return note;
	}
	public User getUser() {
		return user;
	}
	public Group getGroup() {
		return group;
	}
	public String getAttentionList() {
		return attentionList;
	}
	public String getQuestionList() {
		return questionList;
	}
	public void setNote(Note note) {
		this.note = note;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public void setAttentionList(String attentionList) {
		this.attentionList = attentionList;
	}
	public void setQuestionList(String questionList) {
		this.questionList = questionList;
	}
	
		
}
