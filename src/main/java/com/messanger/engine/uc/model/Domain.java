package com.messanger.engine.uc.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 조직도 저장시 도메인 정보를 담고 있는 클래스
 * @author skoh
 *
 */
@Getter
@Setter
@NoArgsConstructor
@XStreamAlias("domain")
public class Domain {
    @XStreamAlias("schema")
    private String schema;

    @XStreamAlias("domainName")
    private String domain;

    @XStreamAlias("daoName")
    private String daoName;

    @XStreamAlias("companyCode")
    private String companyCode;

//
//    public String getSchema() {
//        return schema;
//    }
//
//    public void setSchema(String schema) {
//        this.schema = schema;
//    }
//
//    public String getDomain() {
//        return domain;
//    }
//
//    public void setDomain(String domain) {
//        this.domain = domain;
//    }
//
//	public String getDaoName() {
//		return daoName;
//	}
//
//	public void setDaoName(String daoName) {
//		this.daoName = daoName;
//	}
//
//	public String getCompanyCode() {
//		return companyCode;
//	}
//
//	public void setCompanyCode(String companyCode) {
//		this.companyCode = companyCode;
//	}

	
}
