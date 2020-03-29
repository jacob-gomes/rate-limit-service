package com.blue.optima.assignment.dao.service.h2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.blue.optima.assignment.dao.model.UserAPILimitTrackerDAO;

@Repository
public interface UserAPILimitTrackerRepository  extends JpaRepository<UserAPILimitTrackerDAO, String> {
	
	String GET_RECORDS_FROM_TRACKER = "select p from UserAPILimitTrackerDAO p where p.userID = :userID and p.currentSecond = :currentSecond and p.apiName like :apiName";

	@Query(GET_RECORDS_FROM_TRACKER)
	List<UserAPILimitTrackerDAO> getAllMatchedTrackRecordOfCurrentSecond(@Param("currentSecond")Long currentSecond,@Param("userID") String userID,
			@Param("apiName")String apiName);

}
