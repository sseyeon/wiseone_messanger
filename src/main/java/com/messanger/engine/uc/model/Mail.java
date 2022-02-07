package com.messanger.engine.uc.model;

/**
 * 읽지 않은 메일 조회시 메일 정보를 담고 있는 클래스
 * @author skoh
 *
 */
public class Mail {
	
    private String messageId; 
    private String subject;
    private String sender;
    private String recvDate;
	/**
	 * @return the messageId
	 */
	public String getMessageId() {
		return messageId;
	}
	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}
	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}
	/**
	 * @return the recvDate
	 */
	public String getRecvDate() {
		return recvDate;
	}
	/**
	 * @param recvDate the recvDate to set
	 */
	public void setRecvDate(String recvDate) {
		this.recvDate = recvDate;
	}    
	
	public String toString() {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("MSGID:").append(messageId).append("\n")
		.append("SUB:").append(subject).append("\n")
		.append("SND:").append(sender).append("\n")
		.append("RCVD:").append(recvDate).append("\n");
		return strBuf.toString();
	}
}
