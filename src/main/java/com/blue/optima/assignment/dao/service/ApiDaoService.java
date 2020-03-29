package com.blue.optima.assignment.dao.service;

import com.blue.optima.assignment.dao.model.RecordDAO;

public interface ApiDaoService {

	RecordDAO getRecord(String id);

	void saveRecord(RecordDAO recordDAO);

	void deleteRecord(String id);

	void updateRecord(RecordDAO recordDAO);

}
