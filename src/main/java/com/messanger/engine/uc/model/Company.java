package com.messanger.engine.uc.model;

import com.messanger.engine.uc.Constants;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 조직도의 회사 정보를 저장하는 클래스
 * @author skoh
 *
 */
@XStreamAlias("company")
public class Company {

    @XStreamAlias("companyCode")
    private String companyCode;

    @XStreamAlias("companyName")
    private String companyName;

    @XStreamAlias("scompCode")
    private String scompCode;

    @XStreamAlias("domainName")
    private String domainName;
    
    public String getCompanyCode() {
        return companyCode;
    }
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getScompCode() {
        return scompCode;
    }
    public void setScompCode(String scompCode) {
        this.scompCode = scompCode;
    }
    public String getDomainName() {
        return domainName;
    }
    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }
    public String toString() {
        StringBuilder buff = new StringBuilder();
        buff.append("C").append(Constants.COL_DELIM)
            .append(this.companyCode).append(Constants.COL_DELIM)
            .append(this.companyName).append(Constants.COL_DELIM)
            .append(this.scompCode).append(Constants.COL_DELIM)
            .append(this.domainName).append(Constants.ROW_DELIM);
    
        return buff.toString();
    }
}
