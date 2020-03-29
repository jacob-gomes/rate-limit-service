package com.blue.optima.assignment.dao.service.h2.impl;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.blue.optima.assignment.dao.model.UserAPILimitMasterDataDAO;
import com.blue.optima.assignment.dao.model.UserAPILimitTrackerDAO;
import com.blue.optima.assignment.dao.service.RateLimitHandlerDaoService;
import com.blue.optima.assignment.dao.service.h2.repository.UserAPILimitMasterDataRepository;
import com.blue.optima.assignment.dao.service.h2.repository.UserAPILimitTrackerRepository;
import com.blue.optima.assignment.service.constants.ApiConstants;

@Component
public class RateLimitHandlerDaoServiceH2Impl implements RateLimitHandlerDaoService {
	
	private static final Logger logger = LoggerFactory.getLogger(RateLimitHandlerDaoServiceH2Impl.class);
		
	private UserAPILimitMasterDataRepository userAPILimitMasterDataRepository;
	
	private UserAPILimitTrackerRepository userAPILimitTrackerRepository;
	
	@Autowired	
	public RateLimitHandlerDaoServiceH2Impl(UserAPILimitMasterDataRepository userAPILimitMasterDataRepository,
			UserAPILimitTrackerRepository userAPILimitTrackerRepository) {
		super();
		this.userAPILimitMasterDataRepository = userAPILimitMasterDataRepository;
		this.userAPILimitTrackerRepository = userAPILimitTrackerRepository;
	}

	@Override
	public Boolean checkIfRequestAllowedAndIncrementRequestCount(HttpServletRequest request,  Long defaultRateLimit, Long resolutionOfTimeInSeconds) {
		String userId;
		String methodType;
		String url;
		List<UserAPILimitMasterDataDAO> listOfUserAPILimitMasterDataDAO;
		UserAPILimitMasterDataDAO matchedRecordOfMasterData = null;
		Boolean isRequestAllowed;
		
		userId = request.getHeader(ApiConstants.HEADER_USED_ID);
		methodType = request.getMethod();
		url = request.getRequestURI();
		listOfUserAPILimitMasterDataDAO = userAPILimitMasterDataRepository.getAllMatchFromMasterData(userId, methodType+"%");
		
		if(null != listOfUserAPILimitMasterDataDAO) {
			//Pattern matching done to handle GET /records/{id} kind of requests
			matchedRecordOfMasterData = listOfUserAPILimitMasterDataDAO.stream().filter(
					(userAPILimitMasterDataDAOTemp) -> {
						try {
							return Pattern.matches(userAPILimitMasterDataDAOTemp.getApiName().split(" ")[1], url);
						}catch(Exception e) {
							logger.error("Improper ApiName format, thus failing");
						}
						return false;
					}
			).findFirst().orElse(null);
		}
		
		if(null == matchedRecordOfMasterData) {
			matchedRecordOfMasterData = new UserAPILimitMasterDataDAO();
			matchedRecordOfMasterData.setUserID(userId);
			matchedRecordOfMasterData.setUserAPIRateLimit(defaultRateLimit);
			try {
				matchedRecordOfMasterData.setApiName(methodType +" "+ url);
			} catch (Exception e) {
				logger.info("Ignoring exception", e);
			}
			
		}
		
		isRequestAllowed = checkIfRequestAllowedAgainstTrackerData(matchedRecordOfMasterData,  resolutionOfTimeInSeconds);
		
		return isRequestAllowed;
	}

	@Transactional
	private Boolean checkIfRequestAllowedAgainstTrackerData(UserAPILimitMasterDataDAO matchedRecordOfMasterData, Long resolutionOfTimeInSeconds) {
		List<UserAPILimitTrackerDAO> listOfUserAPILimitTrackerDAO;
		Long currentSecond = System.currentTimeMillis() / (resolutionOfTimeInSeconds * 1000L);
		UserAPILimitTrackerDAO matchedUserAPILimitTrackerDAO = null;
		
		listOfUserAPILimitTrackerDAO = userAPILimitTrackerRepository.getAllMatchedTrackRecordOfCurrentSecond(currentSecond, 
				matchedRecordOfMasterData.getUserID(), 
				matchedRecordOfMasterData.getApiName());
		
		if(null != listOfUserAPILimitTrackerDAO ) {
			//Pattern matching done to handle GET /records/{id} kind of requests
			matchedUserAPILimitTrackerDAO = listOfUserAPILimitTrackerDAO.stream().filter(
					(userAPILimitTrackerDAOTemp) -> {
						try {
							return Pattern.matches(userAPILimitTrackerDAOTemp.getApiName().split(" ")[1], matchedRecordOfMasterData.getApiName().split(" ")[1]);
						}catch(Exception e) {
							logger.error("Improper ApiName format, thus failing");
						}
						return false;
					}
			).findFirst().orElse(null);
		}
		
		if(null == matchedUserAPILimitTrackerDAO) {
			matchedUserAPILimitTrackerDAO = new UserAPILimitTrackerDAO();
			matchedUserAPILimitTrackerDAO.setId(UUID.randomUUID().toString());
			matchedUserAPILimitTrackerDAO.setCurrentSecond(currentSecond);
			matchedUserAPILimitTrackerDAO.setCurrentSecondRate(0L);
			matchedUserAPILimitTrackerDAO.setUserID(matchedRecordOfMasterData.getUserID());
			try {
				matchedUserAPILimitTrackerDAO.setApiName(matchedRecordOfMasterData.getApiName());
			} catch (Exception e) {
				logger.info("Ignoring exception", e);
			}
		}

		matchedUserAPILimitTrackerDAO.setCurrentSecondRate(matchedUserAPILimitTrackerDAO.getCurrentSecondRate() + 1);
		
		if(matchedUserAPILimitTrackerDAO.getCurrentSecondRate() > matchedRecordOfMasterData.getUserAPIRateLimit()) {
			return false;
		}
		
		userAPILimitTrackerRepository.save(matchedUserAPILimitTrackerDAO);
		return true;
	}

	@Override
	@Transactional
	public void decrementCountOfFinishedRequest(HttpServletRequest httpServletRequest, Long resolutionOfTimeInSeconds) {
		List<UserAPILimitTrackerDAO> listOfUserAPILimitTrackerDAO;
		Long currentSecond = System.currentTimeMillis() / (resolutionOfTimeInSeconds * 1000L);
		UserAPILimitTrackerDAO matchedUserAPILimitTrackerDAO = null;
		Long currentSecondRate;
		
		listOfUserAPILimitTrackerDAO = userAPILimitTrackerRepository.getAllMatchedTrackRecordOfCurrentSecond(currentSecond, 
				httpServletRequest.getHeader(ApiConstants.HEADER_USED_ID), 
				httpServletRequest.getMethod()+" "+httpServletRequest.getRequestURI());
		
		if(null != listOfUserAPILimitTrackerDAO ) {
			//Pattern matching done to handle GET /records/{id} kind of requests
			matchedUserAPILimitTrackerDAO = listOfUserAPILimitTrackerDAO.stream().filter(
					(userAPILimitTrackerDAOTemp) -> {
						try {
							return Pattern.matches(userAPILimitTrackerDAOTemp.getApiName().split(" ")[1], httpServletRequest.getRequestURI());
						}catch(Exception e) {
							logger.error("Improper ApiName format, thus failing");
						}
						return false;
					}
			).findFirst().orElse(null);
		}
		
		if(null != matchedUserAPILimitTrackerDAO) {
		
			currentSecondRate = matchedUserAPILimitTrackerDAO.getCurrentSecondRate();
			
			if(null != currentSecondRate && currentSecondRate <= 0) {
				currentSecondRate--;

				matchedUserAPILimitTrackerDAO.setCurrentSecondRate(currentSecondRate);
				userAPILimitTrackerRepository.save(matchedUserAPILimitTrackerDAO);
			}
			
		}
		
	}

}
