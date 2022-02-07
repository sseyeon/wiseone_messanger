package com.messanger.engine.uc.model;

/**
 * 결제해야 할 결제정보 조회시 결제 정보를 담고 있는 클래스
 * @author skoh
 *
 */
public class WorkFlow {
   
    private String sancId; 
    private String subject;
    private String createDate;
    private String writer;
    private String formSeq;
    private int formVer;
    
	/**
	 * @return the sancId
	 */
	public String getSancId() {
		return sancId;
	}
	/**
	 * @param sancId the sancId to set
	 */
	public void setSancId(String sancId) {
		this.sancId = sancId;
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
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return the writer
	 */
	public String getWriter() {
		return writer;
	}
	/**
	 * @param writer the writer to set
	 */
	public void setWriter(String writer) {
		this.writer = writer;
	}
	/**
	 * @return the formSeq
	 */
	public String getFormSeq() {
		return formSeq;
	}
	/**
	 * @param formSeq the formSeq to set
	 */
	public void setFormSeq(String formSeq) {
		this.formSeq = formSeq;
	}
	
	public int getFormVer() {
		return formVer;
	}
	public void setFormVer(int formVer) {
		this.formVer = formVer;
	}
	public String toString() {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("SANCID:").append(sancId).append("\n")
		.append("FORMSEQ:").append(formSeq).append("\n")
		.append("SUB:").append(subject).append("\n")
		.append("WRT:").append(writer).append("\n")
		.append("DATE:").append(createDate).append("\n");
		return strBuf.toString();
	}
}
