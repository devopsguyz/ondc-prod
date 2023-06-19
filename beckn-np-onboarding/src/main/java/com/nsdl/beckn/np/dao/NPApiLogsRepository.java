package com.nsdl.beckn.np.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nsdl.beckn.np.model.NPApiLogs;
 
 
@Repository
public interface NPApiLogsRepository extends JpaRepository<NPApiLogs, UUID>{
	  
}
