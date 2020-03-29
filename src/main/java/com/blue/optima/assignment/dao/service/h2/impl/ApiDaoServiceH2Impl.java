package com.blue.optima.assignment.dao.service.h2.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.blue.optima.assignment.dao.model.RecordDAO;
import com.blue.optima.assignment.dao.service.ApiDaoService;
import com.blue.optima.assignment.dao.service.h2.repository.ApiRecordTableRepository;
import com.blue.optima.assignment.exception.RecordNotFoundException;

@Component
@Primary
public class ApiDaoServiceH2Impl implements ApiDaoService {

	private static final Logger logger = LoggerFactory.getLogger(ApiDaoServiceH2Impl.class);
	
	private ApiRecordTableRepository recordTableRepository;
		
	@Autowired
	public ApiDaoServiceH2Impl(ApiRecordTableRepository recordTableRepository) {
		super();
		this.recordTableRepository = recordTableRepository;
	}

	@Override
	public RecordDAO getRecord(String id) {
		logger.info("Entered  getRecord(String id)");
		
		Optional<RecordDAO> optionalRecordDAO = recordTableRepository.findById(id);
		
		optionalRecordDAO.orElseThrow(RecordNotFoundException::new);
		
		return optionalRecordDAO.get();
	}

	@Override
	public void saveRecord(RecordDAO recordDAO) {
		logger.info("Entered saveRecord(RecordDAO recordDAO)");
		
		recordTableRepository.save(recordDAO);
	}

	@Override
	@Transactional
	public void deleteRecord(String id) {
		logger.info("Entered deleteRecord(String id)");
		getRecord(id);
		recordTableRepository.deleteById(id);
	}

	@Override
	@Transactional
	public void updateRecord(RecordDAO recordDAO) {
		logger.info("Entered updateRecord(RecordDAO recordDAO)");
		
		getRecord(recordDAO.getId());
		recordTableRepository.save(recordDAO);
	}

}
