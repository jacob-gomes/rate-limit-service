package com.blue.optima.assignment.controller.intercept.factory;

import java.io.IOException;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blue.optima.assignment.controller.intercept.service.RateLimitHandler;
import com.blue.optima.assignment.controller.intercept.service.impl.RateLimitHandlerCacheImpl;
import com.blue.optima.assignment.controller.intercept.service.impl.RateLimitHandlerDBImpl;

@Component
public class RateLimitHandlerFactory {

	private static final Logger logger = LoggerFactory.getLogger(RateLimitHandlerFactory.class);
	
	private RateLimitHandlerCacheImpl rateLimitHandlerCacheImpl;
	
	private RateLimitHandlerDBImpl rateLimitHandlerDBImpl;
	
	@Autowired
	public RateLimitHandlerFactory(RateLimitHandlerCacheImpl rateLimitHandlerCacheImpl,
			RateLimitHandlerDBImpl rateLimitHandlerDBImpl) {
		this.rateLimitHandlerCacheImpl = rateLimitHandlerCacheImpl;
		this.rateLimitHandlerDBImpl = rateLimitHandlerDBImpl;
	}
	
	public RateLimitHandler createHandler(String rateLimitHandlerType) {

		logger.info("Entered createHandler(String rateLimitHandlerType)..");
		
		if("CACHE".equals(rateLimitHandlerType)) {
			logger.info("Using rateLimitHandlerCacheImpl");
			try {
				rateLimitHandlerCacheImpl.initiate();
			} catch (JSONException | IOException e) {
				logger.error("Unable to initiate bean", e);
				return null;
			}
			return rateLimitHandlerCacheImpl;
		} else if("DB".equals(rateLimitHandlerType)) {
			logger.info("Using rateLimitHandlerDBImpl");
			return rateLimitHandlerDBImpl;
		}else {
			return null;
		}
	}

}
