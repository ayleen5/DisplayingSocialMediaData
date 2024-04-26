package com.tsfn.models;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class NotificatioTwilio {
	
	private String notification_text;
	private String recipint;
    private String email;
    private String subject;

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getNotification_text() {
		return notification_text;
	}
	public void setNotification_text(String notification_text) {
		this.notification_text = notification_text;
	}
	public String getRecipint() {
		return recipint;
	}
	public void setRecipint(String recipint) {
		this.recipint = recipint;
	}
	public NotificatioTwilio(String notification_text, String recipint) {
		super();
		this.notification_text = notification_text;
		this.recipint = recipint;
	}
	

}
