package com.blue.optima.assignment.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "USER_API_LIMIT_MASTER_DATA")
public class UserAPILimitMasterDataDAO extends UserAPILimitDAO {

	@Column(name = "USER_API_RATE_LIMIT")
	private Long userAPIRateLimit;

	public Long getUserAPIRateLimit() {
		return userAPIRateLimit;
	}

	public void setUserAPIRateLimit(Long userAPIRateLimit) {
		this.userAPIRateLimit = userAPIRateLimit;
	}
	
	
}
