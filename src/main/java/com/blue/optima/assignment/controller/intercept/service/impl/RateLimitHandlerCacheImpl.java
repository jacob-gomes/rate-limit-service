package com.blue.optima.assignment.controller.intercept.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.blue.optima.assignment.controller.intercept.service.RateLimitHandler;
import com.blue.optima.assignment.dao.model.UserAPILimitDAO;
import com.blue.optima.assignment.service.constants.ApiConstants;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@Primary
@Component
public class RateLimitHandlerCacheImpl extends RateLimitHandler{

	private static final Logger logger = LoggerFactory.getLogger(RateLimitHandlerCacheImpl.class);
	
	private String cacheUserApiLimitFilePath;
	
	private Map<UserAPILimitDAO,Long> mapOfUserAPILimitDAO;
	
	private Map<Long, Map<UserAPILimitDAO, Long>> cacheUserAndApiRequestPerSecondCountMap;
	
	
	@Autowired
	public RateLimitHandlerCacheImpl(@Value("${cache.user-api.limit.file.path.json:/}") String cacheUserApiLimitFilePath,
			Map<UserAPILimitDAO,Long> mapOfUserAPILimitDAO) {
		super();
		this.cacheUserApiLimitFilePath = cacheUserApiLimitFilePath;
		this.mapOfUserAPILimitDAO = mapOfUserAPILimitDAO;
	}

	public void initiate() throws IOException, JSONException {
		this.initiateListUserAPILimitDAO();
		
		this.refreshCacheUserAndApiRequestPerSecondCountMap();
	}
	
	private void refreshCacheUserAndApiRequestPerSecondCountMap() {
			cacheUserAndApiRequestPerSecondCountMap = new ConcurrentHashMap<>(); 
	}

	private void initiateListUserAPILimitDAO() throws IOException, JSONException {
		//Initiating listUserAPILimitDAO using a file mentioned in cacheUserApiLimitFilePath
		String jsonArrayAsString;
		JSONArray jsonArray;
		ObjectMapper mapper;
		
		if(!"/".equals(cacheUserApiLimitFilePath)) {
			
			try(BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(cacheUserApiLimitFilePath)))) {
				jsonArrayAsString = bufferedReader.lines().collect(Collectors.joining("\n"));
			} catch (IOException e) {
				logger.error("Error while reading json from File", e);
				throw e;
			}
			
			try {
				jsonArray = new JSONArray(jsonArrayAsString);
			    mapper = new ObjectMapper();
			    mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			    
			    for(int jsonCounter = 0; jsonCounter < jsonArray.length(); jsonCounter++) {
			    	JSONObject jsonObject = jsonArray.optJSONObject(jsonCounter);
			    	UserAPILimitDAO userAPILimitDAO = mapper.readValue(jsonObject.toString(), UserAPILimitDAO.class);
			    			
			    	mapOfUserAPILimitDAO.put(userAPILimitDAO, userAPILimitDAO.getRateLimitPerSecond());
			    }
			}catch(JSONException e) {
				logger.error("Error while parsing JSONArray into bean", e);
				throw e;
			}
		}
	}
	
	@Override
	public Boolean checkIfRequestAllowedAndIncrementRequestCount(HttpServletRequest request) {
		logger.info("Using Cache method to check if this request is allowed");

		Map<UserAPILimitDAO, Long> userAndApiRequestInThisSecondMap;
		Long requestFromTheUserThisSecond;
		Long secondsCount;
		String userID = "";
		Long requestLimitForUser;
		UserAPILimitDAO userAPILimitDAO;


		secondsCount = System.currentTimeMillis() / (1000L * getResolutionOfTimeInSeconds());

		if(null == cacheUserAndApiRequestPerSecondCountMap.get(secondsCount)) {
			// cleaning the cache as the map has become stale
			refreshCacheUserAndApiRequestPerSecondCountMap();
		}

		userAndApiRequestInThisSecondMap = cacheUserAndApiRequestPerSecondCountMap.get(secondsCount);

		if(null == userAndApiRequestInThisSecondMap) {
			// purposely not using a Concurrent hashmap as there are multiple get and put
			// and concurrentHashmap does not ensure synchronzation between consecutive get and put calls
			userAndApiRequestInThisSecondMap = new HashMap<>();
			cacheUserAndApiRequestPerSecondCountMap.put(secondsCount, userAndApiRequestInThisSecondMap);
		}

		userID = request.getHeader(ApiConstants.HEADER_USED_ID);		

		userAPILimitDAO = new UserAPILimitDAO();

		userAPILimitDAO.setApiMethod(request.getMethod());
		userAPILimitDAO.setUserID(userID);
		userAPILimitDAO.setApiUrlRegex(request.getRequestURI());
		requestLimitForUser = mapOfUserAPILimitDAO.get(userAPILimitDAO);

		if(null == requestLimitForUser) { // using default value when no explicit value is mentioned in json
			requestLimitForUser = super.getDefaultRequestRateLimit();
		}

		synchronized (userAndApiRequestInThisSecondMap) {
			requestFromTheUserThisSecond = userAndApiRequestInThisSecondMap.get(userAPILimitDAO);

			if(null == requestFromTheUserThisSecond) {
				requestFromTheUserThisSecond = 0L;
				userAndApiRequestInThisSecondMap.put(userAPILimitDAO, requestFromTheUserThisSecond);
			}

			if(requestFromTheUserThisSecond >= requestLimitForUser ) {
				return false;
			}

			userAndApiRequestInThisSecondMap.put(userAPILimitDAO, ++requestFromTheUserThisSecond); // incrementing count for an allowed request

			return true;
		}

	}

	@Override
	public void decrementCountOfFinishedRequest(HttpServletRequest httpServletRequest) {
		Map<UserAPILimitDAO, Long> userAndApiRequestInThisSecondMap;
		Long secondsCount;
		UserAPILimitDAO userAPILimitDAO;
		Long requestFromTheUserThisSecond;

		secondsCount = System.currentTimeMillis() / (1000L * getResolutionOfTimeInSeconds());

		if(null == cacheUserAndApiRequestPerSecondCountMap.get(secondsCount)) {
			// cleaning the cache as the map has become stale
			refreshCacheUserAndApiRequestPerSecondCountMap();
		}

		userAndApiRequestInThisSecondMap = cacheUserAndApiRequestPerSecondCountMap.get(secondsCount);

		if(null == userAndApiRequestInThisSecondMap) {
			return;
		}

		userAPILimitDAO = new UserAPILimitDAO();

		userAPILimitDAO.setApiMethod(httpServletRequest.getMethod());
		userAPILimitDAO.setUserID(httpServletRequest.getHeader(ApiConstants.HEADER_USED_ID));
		userAPILimitDAO.setApiUrlRegex(httpServletRequest.getRequestURI());		

		synchronized (userAndApiRequestInThisSecondMap) {
			requestFromTheUserThisSecond = userAndApiRequestInThisSecondMap.get(userAPILimitDAO);

			if(requestFromTheUserThisSecond != 0L) {
				requestFromTheUserThisSecond--;
			}

			userAndApiRequestInThisSecondMap.put(userAPILimitDAO, requestFromTheUserThisSecond);
		}

	}

}
