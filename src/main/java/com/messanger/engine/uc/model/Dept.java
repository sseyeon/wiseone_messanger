package com.messanger.engine.uc.model;

import com.messanger.engine.uc.Constants;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 조직도의 부서 정보를 저장하는 클래스
 * @author skoh
 *
 */
@XStreamAlias("dept")
public class Dept {

    @XStreamAlias("companyCode")
    private String companyCode;
    
    @XStreamAlias("pdeptCode")
    private String pdeptCode;
    
    @XStreamAlias("deptCode")
    private String deptCode;
    
    @XStreamAlias("deptName")
    private String deptName;

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getPdeptCode() {
        return pdeptCode;
    }

    public void setPdeptCode(String pdeptCode) {
        this.pdeptCode = pdeptCode;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    public String toString() {
        StringBuilder buff = new StringBuilder();
        buff.append("D").append(Constants.COL_DELIM)
            .append(this.companyCode).append(Constants.COL_DELIM)
            .append(this.pdeptCode).append(Constants.COL_DELIM)
            .append(this.deptCode).append(Constants.COL_DELIM)
            .append(this.deptName).append(Constants.ROW_DELIM);
    
        return buff.toString();
    }
    
}
