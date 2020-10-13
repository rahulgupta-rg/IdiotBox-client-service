package com.idiotBox.pojos;

public class Feedback 
{
	private Integer feedbackId;
	private Integer userId;
	private String feedbackContent;
	private String flag;
	private String feedbackSubject;
	private String senderEmail;
	
	

	public String getSenderEmail() {
		return senderEmail;
	}
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}
	public String getFeedbackSubject() 
	{
		return feedbackSubject;
	}
	public void setFeedbackSubject(String feedbackSubject) {
		this.feedbackSubject = feedbackSubject;
	}
		
	public Integer getFeedbackId() {
		return feedbackId;
	}
	public void setFeedbackId(Integer feedbackId) {
		this.feedbackId = feedbackId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getFeedbackContent() {
		return feedbackContent;
	}
	public void setFeedbackContent(String feedbackContent) {
		this.feedbackContent = feedbackContent;
	}
		
	public String getFlag() {
		return flag;
	}
	
	public void setFlag(String flag) {
		this.flag = flag;
	}
		
		
	public Feedback() {
		super();
		System.out.println("Feedback constructor called");
	}
		
	public Feedback(Integer feedbackId, Integer userId, String feedbackContent, String flag) {
		super();
		this.feedbackId = feedbackId;
		this.userId = userId;
		this.feedbackContent = feedbackContent;
		this.flag =flag;
	}
		
	public Feedback(Integer userId, String feedbackContent, String flag, String feedbackSubject,String senderEmail) {
		super();
		this.userId = userId;
		this.feedbackContent = feedbackContent;
		this.flag = flag;
		this.feedbackSubject = feedbackSubject;
		this.senderEmail = senderEmail;
	}
	
	@Override
	public String toString() {
		return "Feedback [feedbackId=" + feedbackId + ", userId=" + userId + ", feedbackContent=" + feedbackContent
				+ ", flag=" + flag + ", feedbackSubject=" + feedbackSubject + ", senderEmail=" + senderEmail + "]";
	}
}
