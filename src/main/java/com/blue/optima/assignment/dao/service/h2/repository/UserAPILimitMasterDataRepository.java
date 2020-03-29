package com.blue.optima.assignment.dao.service.h2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.blue.optima.assignment.dao.model.UserAPILimitMasterDataDAO;

@Repository
public interface UserAPILimitMasterDataRepository  extends JpaRepository<UserAPILimitMasterDataDAO, String> {

	String MASTER_DATA_SEARCH_QUERY = "select p from UserAPILimitMasterDataDAO p where p.userID = :userId and p.apiName like :method";
	
	@Query(MASTER_DATA_SEARCH_QUERY)
	List<UserAPILimitMasterDataDAO> getAllMatchFromMasterData(@Param("userId")String userId, @Param("method") String method);

}
