package com.blue.optima.assignment.dao.model;

import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@MappedSuperclass
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAPILimitDAO {
	
	@Id
	@Column(name = "ID")
	protected String id;	
	
	@Column(name = "USER_ID")
	@JsonProperty("userID")
	protected String userID;
	
	@Column(name = "API_NAME")
	@JsonProperty("apiName")
	protected String apiName;
	
	@Transient
	@JsonProperty("rateLimitPerSecond")
	private Long rateLimitPerSecond;
	
	@Transient
	private String apiMethod;
	
	@Transient
	private String apiUrlRegex;  

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apiMethod == null) ? 0 : apiMethod.hashCode());
		result = prime * result + ((userID == null) ? 0 : userID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {		
		UserAPILimitDAO userAPILimitDAO = (UserAPILimitDAO)obj;
		Boolean patternMatched = Pattern.matches(userAPILimitDAO.getApiUrlRegex(),this.getApiUrlRegex()); //Pattern matching done to handle GET /records/{id} kind of request
		
		if(this.getUserID().equals(userAPILimitDAO.getUserID()) 
				&& this.getApiMethod().equals(userAPILimitDAO.getApiMethod())
				&& patternMatched) {
			return true;
		}
		
		return false;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String getApiName() {
		return apiName;
	}

	@JsonProperty("apiName")
	public void setApiName(String apiName) throws Exception {
		this.apiName = apiName;
		
		String[] apiNameArray = apiName.split(" ");
		
		if(apiNameArray.length != 2) {
			throw new Exception("apiName format improper, it should be '<METHOD> <URL_REGEX>'");
		}
		
		this.apiMethod = apiNameArray[0];
		this.apiUrlRegex = apiNameArray[1];
	}

	public Long getRateLimitPerSecond() {
		return rateLimitPerSecond;
	}

	public void setRateLimitPerSecond(Long rateLimitPerSecond) {
		this.rateLimitPerSecond = rateLimitPerSecond;
	}

	public String getApiMethod() {
		return apiMethod;
	}

	public void setApiMethod(String apiMethod) {
		this.apiMethod = apiMethod;
	}

	public String getApiUrlRegex() {
		return apiUrlRegex;
	}

	public void setApiUrlRegex(String apiUrlRegex) {
		this.apiUrlRegex = apiUrlRegex;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
