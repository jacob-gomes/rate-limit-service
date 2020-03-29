package com.blue.optima.assignment.service.util;

import java.util.UUID;

import com.blue.optima.assignment.controller.model.RecordDTO;
import com.blue.optima.assignment.dao.model.RecordDAO;

public interface ApiServiceUtil {

	public static RecordDTO convertDaoToDto(RecordDAO recordDAO) {
		RecordDTO recordDTO = new RecordDTO();
		
		recordDTO.setAge(recordDAO.getAge());
		recordDTO.setCity(recordDAO.getCity());
		recordDTO.setId(recordDAO.getId());
		recordDTO.setUserName(recordDAO.getUserName());
		
		return recordDTO;
	}

	public static RecordDAO converDtoToDao(RecordDTO recordDTORequestBody, UUID uuid) {
		RecordDAO recordDAO = new RecordDAO();
		
		recordDAO.setAge(recordDTORequestBody.getAge());
		recordDAO.setCity(recordDTORequestBody.getCity());
		recordDAO.setId(uuid.toString());
		recordDAO.setUserName(recordDTORequestBody.getUserName());
		
		return recordDAO;
	}

}
