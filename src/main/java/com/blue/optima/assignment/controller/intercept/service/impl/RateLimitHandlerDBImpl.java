package com.blue.optima.assignment.controller.intercept.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blue.optima.assignment.controller.intercept.service.RateLimitHandler;
import com.blue.optima.assignment.dao.service.RateLimitHandlerDaoService;

@Component
public class RateLimitHandlerDBImpl extends RateLimitHandler{

	private RateLimitHandlerDaoService rateLimitHandlerDaoService;
	
	@Autowired
	public RateLimitHandlerDBImpl(RateLimitHandlerDaoService rateLimitHandlerDaoService) {
		this.rateLimitHandlerDaoService = rateLimitHandlerDaoService;
	}
	
	@Override
	public Boolean checkIfRequestAllowedAndIncrementRequestCount(HttpServletRequest request) {
		
		return rateLimitHandlerDaoService.checkIfRequestAllowedAndIncrementRequestCount(request, getDefaultRequestRateLimit(), getResolutionOfTimeInSeconds());
	}

	@Override
	public void decrementCountOfFinishedRequest(HttpServletRequest httpServletRequest) {
		rateLimitHandlerDaoService.decrementCountOfFinishedRequest(httpServletRequest, getResolutionOfTimeInSeconds());
	}

}
