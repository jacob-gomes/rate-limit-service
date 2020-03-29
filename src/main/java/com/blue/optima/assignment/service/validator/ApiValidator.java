package com.blue.optima.assignment.service.validator;

import javax.servlet.http.HttpServletRequest;

import com.blue.optima.assignment.exception.BadHttpRequestException;
import com.blue.optima.assignment.service.constants.ApiConstants;

public interface ApiValidator {
	static void validateRequestForUserIDHeader(HttpServletRequest request) {
		String userID;
		
		userID = request.getHeader(ApiConstants.HEADER_USED_ID);
		
		if(null == userID || userID.isEmpty()) {
			throw new BadHttpRequestException(ApiConstants.HEADER_USED_ID + ": is mandatory");
		}
	}
}
