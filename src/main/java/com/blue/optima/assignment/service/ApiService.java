package com.blue.optima.assignment.service;

import com.blue.optima.assignment.controller.model.RecordDTO;

public interface ApiService {

	RecordDTO getRecord(String id);

	String createRecord(RecordDTO recordDTORequestBody);

	void deleteRecord(String id);

	void updateRecord(String id, RecordDTO recordDTORequestBody);

}
