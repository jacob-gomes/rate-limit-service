package com.blue.optima.assignment.controller.intercept.service;

import javax.servlet.http.HttpServletRequest;

public abstract class RateLimitHandler {

	private Long defaultRequestRateLimit;
	
	private Long resolutionOfTimeInSeconds;
	
	public void setDefaultRequestRateLimit(Long defaultRequestRateLimit) {
		this.defaultRequestRateLimit = defaultRequestRateLimit;
	}
	
	protected Long getDefaultRequestRateLimit() {
		return this.defaultRequestRateLimit;
	}
	

	public long getResolutionOfTimeInSeconds() {
		return resolutionOfTimeInSeconds;
	}
	
	public void setResolutionOfTimeInSeconds(Long resolutionOfTimeInSeconds) {
		this.resolutionOfTimeInSeconds = resolutionOfTimeInSeconds;
	}

	
	public abstract Boolean checkIfRequestAllowedAndIncrementRequestCount(HttpServletRequest request);


	public abstract void decrementCountOfFinishedRequest(HttpServletRequest httpServletRequest);

}
