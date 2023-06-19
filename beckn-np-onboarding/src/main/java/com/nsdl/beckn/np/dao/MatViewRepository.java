package com.nsdl.beckn.np.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nsdl.beckn.np.model.MatView;
import com.nsdl.beckn.np.model.response.MatViewResponse;

@Repository
public interface MatViewRepository extends JpaRepository<MatView, String>, JpaSpecificationExecutor {

    
    
    @Query(value = "select id AS ID, citycode AS CITYCODE, ecitycode AS ECITYCODE ,sellercitycode AS SELLERCITYCODE, \r\n"
            + "    callback AS CALLBACK, encrypt AS ENCRYPT, country AS COUNTRY, signing AS SIGNING, \r\n"
            + "    status AS STATUS, subscriberid AS SUBSCRIBERID, uniquekeyid AS UNIQUEKEYID, domain AS DOMAIN,validfrom AS VALIDFROM, validuntil AS VALIDUNTIL, created AS CREATED, \r\n"
            + "    updated AS UPDATED,sellerencryption AS SELLERENCRYPTION,sellersigning AS SELLERSIGNING,\r\n"
            + "    selleruniquekeyid AS SELLERUNIQUEKEYID,sellervalidfrom AS SELLERVALIDFROM,sellervaliduntil AS SELLERVALIDUNTIL,subscriberurl AS SUBSCRIBERURL,msn AS MSN,type AS TYPE FROM mv_np_sor_dtls \r\n"
            + "    where  status = 'SUBSCRIBED' \r\n"
            + " and case when :domain='' then true else domain=:domain end \r\n"
            + " and case when :country='' then true else country=:country end \r\n"
            + " and case when :type='' then true else type=:type end \r\n"
            + " and case when :subscriberId='' then true else subscriberId=:subscriberId end \r\n"
            + " and case when :ukid='' then true else ( uniquekeyid = :ukid or selleruniquekeyid = :ukid) end " , nativeQuery = true)
    List<MatViewResponse> findByAll(String domain,String country, String type,String subscriberId, String ukid);
}