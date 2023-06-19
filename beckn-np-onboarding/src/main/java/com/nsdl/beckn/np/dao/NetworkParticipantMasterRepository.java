package com.nsdl.beckn.np.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.nsdl.beckn.np.model.NetworkParticipantMaster;
 
 
@Repository
public interface NetworkParticipantMasterRepository extends JpaRepository<NetworkParticipantMaster, String> , JpaSpecificationExecutor {
	  List<NetworkParticipantMaster> findBySubscriberId(String subscriberId);

}
