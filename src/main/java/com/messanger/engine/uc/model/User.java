package com.messanger.engine.uc.model;

import com.messanger.engine.uc.Constants;
import com.messanger.engine.uc.config.Config;
import com.messanger.engine.uc.ipt.service.IIptService;
import com.messanger.engine.uc.utils.SessionUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 조직도의 사용자 정보를 저장하는 클래스
 * @author skoh
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("user")
public class User {

    @XStreamAlias("userindex")
    private String userindex;
    
    @XStreamAlias("userid")
    private String userid;
    
    @XStreamAlias("password")
    private String password;
    
    @XStreamAlias("userName")
    private String userName;
    
    @XStreamAlias("companyCode")
    private String companyCode;
    
    @XStreamAlias("deptCode")
    private String deptCode;
    
    @XStreamAlias("gradeCode")
    private String gradeCode;
    
    @XStreamAlias("gradeName")
    private String gradeName;
    
    @XStreamAlias("dutyCode")
    private String dutyCode;
    
    @XStreamAlias("dutyName")
    private String dutyName;
    
    @XStreamAlias("sex")
    private String sex;
    
    @XStreamAlias("email")
    private String email;
    
    @XStreamAlias("domainName")
    private String domainName;
    
    @XStreamAlias("compPhoneNumber")
    private String compPhoneNumber;
    
    @XStreamAlias("homePhoneNumber")
    private String homePhoneNumber;
    
    @XStreamAlias("mobileNumber")
    private String mobileNumber;
    
    @XStreamAlias("greetings")
    private String greetings;
    
    @XStreamAlias("onlineId")
    private String onlineId;
    
    @XStreamAlias("empno")
    private String empno;
    
    @XStreamAlias("posName")
    private String posName;
    
    @XStreamAlias("posCode")
    private String posCode;
    
//    public String getPosCode() {
//		return posCode;
//	}
//
//	public void setPosCode(String posCode) {
//		this.posCode = posCode;
//	}
//
//	public String getPosName() {
//		return posName;
//	}
//
//	public void setPosName(String posName) {
//		this.posName = posName;
//	}
//
//	public String getOnlineId() {
//		return onlineId;
//	}
//
//	public void setOnlineId(String onlineId) {
//		this.onlineId = onlineId;
//	}
//
//	public String getGreetings() {
//		return greetings;
//	}
//
//	public void setGreetings(String greetings) {
//		this.greetings = greetings;
//	}
//
//	public String getUserindex() {
//        return userindex;
//    }
//
//    public void setUserindex(String userindex) {
//        this.userindex = userindex;
//    }
//
//    public String getUserid() {
//        return userid;
//    }
//
//    public void setUserid(String userid) {
//        this.userid = userid;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public String getCompanyCode() {
//        return companyCode;
//    }
//
//    public void setCompanyCode(String companyCode) {
//        this.companyCode = companyCode;
//    }
//
//    public String getDeptCode() {
//        return deptCode;
//    }
//
//    public void setDeptCode(String deptCode) {
//        this.deptCode = deptCode;
//    }
//
//    public String getGradeCode() {
//        return gradeCode;
//    }
//
//    public void setGradeCode(String gradeCode) {
//        this.gradeCode = gradeCode;
//    }
//
//    public String getGradeName() {
//        return gradeName;
//    }
//
//    public void setGradeName(String gradeName) {
//        this.gradeName = gradeName;
//    }
//
//    public String getDutyCode() {
//        return dutyCode;
//    }
//
//    public void setDutyCode(String dutyCode) {
//        this.dutyCode = dutyCode;
//    }
//
//    public String getDutyName() {
//        return dutyName;
//    }
//
//    public void setDutyName(String dutyName) {
//        this.dutyName = dutyName;
//    }
//
//    public String getSex() {
//        return sex;
//    }
//
//    public void setSex(String sex) {
//        this.sex = sex;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//	public String getCompPhoneNumber() {
//		return compPhoneNumber;
//	}
//
//	public void setCompPhoneNumber(String compPhoneNumber) {
//		this.compPhoneNumber = compPhoneNumber;
//	}
//
//	public String getHomePhoneNumber() {
//		return homePhoneNumber;
//	}
//
//	public void setHomePhoneNumber(String homePhoneNumber) {
//		this.homePhoneNumber = homePhoneNumber;
//	}
//
//	public String getMobileNumber() {
//		return mobileNumber;
//	}
//
//	public void setMobileNumber(String mobileNumber) {
//		this.mobileNumber = mobileNumber;
//	}
//
//    /**
//	 * @return the domainName
//	 */
//	public String getDomainName() {
//		return domainName;
//	}
//
//	/**
//	 * @param domainName the domainName to set
//	 */
//	public void setDomainName(String domainName) {
//		this.domainName = domainName;
//	}
//
//
//	public String getEmpno() {
//		return empno;
//	}
//
//	public void setEmpno(String empno) {
//		this.empno = empno;
//	}

	public String toString(IIptService iptService, Config config) {
    	StringBuilder strBuf = new StringBuilder();
        strBuf.append("U").append(Constants.COL_DELIM)
            .append(this.userindex).append(Constants.COL_DELIM)
            .append(this.userid).append(Constants.COL_DELIM)
            .append(this.userName).append(Constants.COL_DELIM)
            .append(this.companyCode).append(Constants.COL_DELIM)
            .append(this.deptCode).append(Constants.COL_DELIM)
            .append(this.gradeCode).append(Constants.COL_DELIM)
            .append(this.gradeName).append(Constants.COL_DELIM)
            .append(this.dutyCode).append(Constants.COL_DELIM)
            .append(this.dutyName).append(Constants.COL_DELIM)
            .append(this.posCode).append(Constants.COL_DELIM)
            .append(this.posName).append(Constants.COL_DELIM)
            .append(this.sex).append(Constants.COL_DELIM)
            .append(this.email).append(Constants.COL_DELIM)
            //.append(SessionUtils.getStatus(this.email));
            .append(SessionUtils.getStatus(this.email)).append(Constants.COL_DELIM)            
	        .append(this.compPhoneNumber).append(Constants.COL_DELIM)	        
	        .append(this.mobileNumber).append(Constants.COL_DELIM);
        	
        if( Boolean.valueOf(config.get("user.greetings", "false")))
        	strBuf.append(this.greetings).append(Constants.COL_DELIM);  
        
        if( Boolean.valueOf(config.get("user.onlineId", "false")))
	        strBuf.append(this.onlineId).append(Constants.COL_DELIM);
        
//        if(iptService != null && iptService instanceof ScoreBoard) {
//        	strBuf.append(iptService.getPhoneBook().getStatus(this.email));        	
//        } else {
//        	strBuf.append("04");
//        }  
        if(iptService != null && iptService.getPhoneBook() != null) {
        	strBuf.append(iptService.getPhoneBook().getStatus(this.email));
        } else {
        	strBuf.append("04");
        }
        strBuf.append(Constants.ROW_DELIM);        
        return strBuf.toString();
    }    
}
