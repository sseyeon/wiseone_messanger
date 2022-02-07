package com.messanger.engine.uc.dao;

import java.util.Map;
import java.util.Set;

/**
 * 복수의 그룹사를 이용해야 할 경우 복수의 Dao를 관리하는 클래스
 * dao name을 키로 하여 ICommonDao 구현 instance 저장
 * @author skoh
 *
 */
public class CommonDaos {
	
	private Map<String,ICommonDao> daos;

	public Map<String, ICommonDao> getDaos() {
		return daos;
	}

	public void setDaos(Map<String, ICommonDao> daos) {
		this.daos = daos;
	}
	
	public Set<String> keySet() {
		return this.daos.keySet();
	}
	
	public ICommonDao get(String key) {
		return this.daos.get(key);
	}
}
