package com.blue.optima.assignment.dao.service.h2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blue.optima.assignment.dao.model.RecordDAO;

@Repository
public interface ApiRecordTableRepository  extends JpaRepository<RecordDAO, String> {

}
