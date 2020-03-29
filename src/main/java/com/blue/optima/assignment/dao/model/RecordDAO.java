package com.blue.optima.assignment.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RECORDS")
public class RecordDAO {
	
	@Id
	@Column(name = "ID")
	private String id;
	
	@Column(name = "USER_NAME")
	private String userName;
	
	@Column(name = "CITY")
	private String city;
	
	@Column(name = "AGE")
	private Integer age;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
	
}
