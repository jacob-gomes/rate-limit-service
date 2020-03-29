package com.blue.optima.assignment.controller.intercept;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.assertj.core.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.blue.optima.assignment.controller.intercept.factory.RateLimitHandlerFactory;
import com.blue.optima.assignment.controller.intercept.service.RateLimitHandler;
import com.blue.optima.assignment.service.validator.ApiValidator;

/**
 * 
 * @author Jacob Gomes
 *
 */
@Component
public class ControllerFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(ControllerFilter.class);
		
	private RateLimitHandler rateLimitHandler;
	
	private String rateLimitHandlerType;
	
	private RateLimitHandlerFactory rateLimitHandlerFactory;
	
	private Long defaultRequestRateLimitPerSecond;
	
	private Long resolutionOfTimeInSeconds; //can increase or decrease resolution
	
	private Boolean checkOnlyActiveThread;
	
	private String urlsToBeExempted;
	
	private String[] urlsToBeExemptedArray;
	
	@Autowired
	public ControllerFilter(RateLimitHandler rateLimitHandler,
			@Value("${request.rate.limit.handler.type:CACHE}") String rateLimitHandlerType,
			RateLimitHandlerFactory rateLimitHandlerFactory,
			@Value("${request.default.rate.limit.per.second:10}") Long defaultRequestRateLimitPerSecond,
			@Value("${request.resolution.of.time.in.second:1}")  Long resolutionOfTimeInSeconds,
			@Value("${check.only.active.thread:false}")  Boolean checkOnlyActiveThread,
			@Value("${request.url.exempted}")  String urlsToBeExempted) {
		super();
		this.rateLimitHandler = rateLimitHandler;
		this.rateLimitHandlerType = rateLimitHandlerType;
		this.rateLimitHandlerFactory = rateLimitHandlerFactory;	
		this.defaultRequestRateLimitPerSecond = defaultRequestRateLimitPerSecond;
		this.resolutionOfTimeInSeconds = resolutionOfTimeInSeconds;
		this.checkOnlyActiveThread = checkOnlyActiveThread;
		this.urlsToBeExempted = urlsToBeExempted;
	}

	@PostConstruct
	public void initiateRateLimitHandler()  {
		rateLimitHandler = rateLimitHandlerFactory.createHandler(rateLimitHandlerType);
		rateLimitHandler.setDefaultRequestRateLimit(defaultRequestRateLimitPerSecond);
		rateLimitHandler.setResolutionOfTimeInSeconds(resolutionOfTimeInSeconds);
		urlsToBeExemptedArray = urlsToBeExempted.split(",");
	}

	@Override
	public void doFilter(ServletRequest request,
			ServletResponse response,
			FilterChain chain) throws IOException, ServletException{
		Boolean isRequestAllowed;
		Long initialTime = System.currentTimeMillis();
		Boolean isUrlExempted;
		logger.info("Checking is this Request allowed...");

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		
		isUrlExempted = isUrlExempted(httpServletRequest,urlsToBeExemptedArray);
		
		if(isUrlExempted) {
			chain.doFilter(httpServletRequest, response);
		}
		
		ApiValidator.validateRequestForUserIDHeader(httpServletRequest);
		
		isRequestAllowed = rateLimitHandler.checkIfRequestAllowedAndIncrementRequestCount(httpServletRequest);
		
		if(!isRequestAllowed) {
			logger.info("Request is not allowed thus sending Too many request Http status.");
			httpServletResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
			return;
		}
		
		logger.info("Request is allowed...");
		chain.doFilter(request, response);
		
		if(checkOnlyActiveThread) {
			// will reduce completed process to check only active thread
			rateLimitHandler.decrementCountOfFinishedRequest(httpServletRequest);
		}
		
		logger.info("Time taken to execute the service: {}", System.currentTimeMillis() - initialTime);
	}

	private Boolean isUrlExempted(HttpServletRequest httpServletRequest , String[] urlsToBeExemptedArray) {
		if(null == urlsToBeExemptedArray || urlsToBeExemptedArray.length == 0) {
			return false;
		}
		
		return Arrays.asList(urlsToBeExemptedArray).contains(httpServletRequest.getRequestURI());
		
	}
	

}
