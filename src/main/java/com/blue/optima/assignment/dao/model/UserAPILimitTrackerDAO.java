package com.blue.optima.assignment.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity	
@Table(name = "USER_API_LIMIT_TRACKER")
public class UserAPILimitTrackerDAO extends UserAPILimitDAO {
	
	@Column(name = "CURRENT_SECOND")
	private Long currentSecond;
	
	@Column(name = "CURRENT_SECOND_RATE")
	private Long currentSecondRate;

	public Long getCurrentSecondRate() {
		return currentSecondRate;
	}

	public void setCurrentSecondRate(Long currentSecondRate) {
		this.currentSecondRate = currentSecondRate;
	}
	
	public Long getCurrentSecond() {
		return currentSecond;
	}

	public void setCurrentSecond(Long currentSecond) {
		this.currentSecond = currentSecond;
	}
}
