package com.blue.optima.assignment.dao.service;

import javax.servlet.http.HttpServletRequest;

public interface RateLimitHandlerDaoService {

	Boolean checkIfRequestAllowedAndIncrementRequestCount(HttpServletRequest request, Long defaultRateLimit, Long resolutionOfTimeInSeconds);

	void decrementCountOfFinishedRequest(HttpServletRequest httpServletRequest, Long resolutionOfTimeInSeconds);

}
