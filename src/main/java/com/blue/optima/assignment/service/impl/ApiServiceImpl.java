package com.blue.optima.assignment.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blue.optima.assignment.controller.model.RecordDTO;
import com.blue.optima.assignment.dao.model.RecordDAO;
import com.blue.optima.assignment.dao.service.ApiDaoService;
import com.blue.optima.assignment.service.ApiService;
import com.blue.optima.assignment.service.util.ApiServiceUtil;

@Service
public class ApiServiceImpl implements ApiService{

    private static final Logger logger = LoggerFactory.getLogger(ApiServiceImpl.class);
     
	private ApiDaoService apiDaoService;
	
	@Autowired
	public ApiServiceImpl(ApiDaoService apiDaoService) {
		super();
		this.apiDaoService = apiDaoService;
	}

	@Override
	public RecordDTO getRecord(String id) {
		logger.info("Entered getRecord(String id) ");
		RecordDAO recordDAO;
		RecordDTO recordDTO;
		
		recordDAO = apiDaoService.getRecord(id);
		recordDTO = ApiServiceUtil.convertDaoToDto(recordDAO);
		
		logger.info("Record fetched");
		
		return recordDTO;
	}

	@Override
	public String createRecord(RecordDTO recordDTORequestBody) {
		logger.info("Entered createRecord(RecordDTO recordDTORequestBody)");
		RecordDAO recordDAO;
		UUID uuid = UUID.randomUUID();
		
		recordDAO = ApiServiceUtil.converDtoToDao(recordDTORequestBody,uuid);
		apiDaoService.saveRecord(recordDAO);
		
		logger.info("Record Created");
		
		return uuid.toString();
	}

	@Override
	public void deleteRecord(String id) {
		logger.info("Entered deleteRecord(String id)");
		
		apiDaoService.deleteRecord(id);
		
		logger.info("Record Deleted");
		
	}

	@Override
	public void updateRecord(String id, RecordDTO recordDTORequestBody) {
		logger.info("Entered updateRecord(String id, RecordDTO recordDTORequestBody)");
		RecordDAO recordDAO;
		
		recordDAO = ApiServiceUtil.converDtoToDao(recordDTORequestBody,null);
		recordDAO.setId(id);
		apiDaoService.updateRecord(recordDAO);
		
		logger.info("Record updated");
		
	}

}
