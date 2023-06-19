package com.nsdl.beckn.lm.audit.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.nsdl.beckn.lm.audit.config.ApiAuditDBTypeEnum;
import com.nsdl.beckn.lm.audit.model.FileBo;
import com.nsdl.beckn.lm.audit.model.Graph;
import com.nsdl.beckn.lm.audit.model.projection.ApiAuditEntityBuyerProjection;
import com.nsdl.beckn.lm.audit.model.projection.ApiAuditEntitySellerProjection;
import com.nsdl.beckn.lm.audit.model.projection.CountSellerBuyerProjection;
import com.nsdl.beckn.lm.audit.model.response.TransactionMapResponse;

public interface ApiAuditService {
	/**
	 * Finds transaction details by data base and id.
	 * @param db It is a database server.
	 * @param id It is unique id.
	 * @return Map object based on the database and id.
	 */
	Map<String, List<Map<String, Object>>> findTransactionById(ApiAuditDBTypeEnum db,String id);
	/**
	 * Finds transaction details by database, start and end local date time.
	 * @param db It is a database server.
	 * @param start It is local date time.
	 * @param end It is local date time.
	 * @param page It is a pagination.
	 * @return Transaction map response based on the database, start and end local date time.
	 */
	TransactionMapResponse findTransactionByDate(ApiAuditDBTypeEnum db,String start, String end, Integer page);
	
	//LookupResponse findLookupByType (String type, String end ,Integer page );
	/**
	 * Gets distinct id by database server.
	 * @param db It is a database server.
	 * @return List of the distinct id based on database server.
	 */
	List<String> getDistinctId(ApiAuditDBTypeEnum db);
	
 
	/**
	 * Gets dashboard list by database server and type.
	 * @param db It is a database server.
	 * @param type It is String type.
	 * @return List of the dashboard based on the database server and type.
	 */
	List<Graph<Integer>> getDashBoard(ApiAuditDBTypeEnum db,String type);

	// List<Map<String, Object>> findByBuyerSellerTransactionByDate(String type,
	// String start, String end);
	/**
	 * This method converts summary report to excel by database, start and end local date time.
	 * @param db It is a database server.
	 * @param start It is a local date time.
	 * @param end It is a local date time.
	 * @return Converts summary report to excel based on the database server, start and end local date time.
	 */
	String summaryReportExportToExcel(ApiAuditDBTypeEnum db,String start, String end);
	/**
	 * Gets dashboard buyer id by database, type, start and end local date time.
	 * @param db It is a database server.
	 * @param select It is a String.
	 * @param type It is type.
	 * @param start It is a local date time.
	 * @param end It is a local date time.
	 * @param flag It is a boolean value.
	 * @return List of the dashboard based on the database, type, start and end local date time.
	 */
	List<Graph<Integer>> getDashBoardBuyerId(ApiAuditDBTypeEnum db,String select, String type, String start, String end, boolean flag);
	
	/**
	 * Gets dashboard buyer all list by database, start and end local date time.
	 * @param db It is a database server.
	 * @param start It is a local date time.
	 * @param end It is a local date time.
	 * @param flag It is a boolean value.
	 * @return List of the ApiAuditEntityBuyerProjection based on the ddatabase server , start and end local date time.
	 */
	List<ApiAuditEntityBuyerProjection> getDashBoardBuyerAllList(ApiAuditDBTypeEnum db,String start, String end, boolean flag);
	/**
	 * Gets dashboard seller id details by database, type, start and end local date time.
	 * @param db It is a database server.
	 * @param select It is a String.
	 * @param type It is a String.
	 * @param start It is a local date time.
	 * @param end It is a local date time.
	 * @param flag It is a boolean value.
	 * @return List of the seller based on the database, type, start and  end local date time.
	 */
	List<Graph<Integer>> getDashBoardSellerId(ApiAuditDBTypeEnum db,String select, String type, String start, String end, boolean flag);
	
	/**
	 * Gets dashboard all seller list by database server, start and end local date time.
	 * @param db It is a database server.
	 * @param start It is a local date time.
	 * @param end It is a local date time.
	 * @param flag It is a boolean value.
	 * @return List of the ApiAuditEntitySellerProjection based on the database server, start and end local date time.
	 */
	List<ApiAuditEntitySellerProjection> getDashBoardSellerAllList(ApiAuditDBTypeEnum db,String start, String end, boolean flag);

	/**
	 * Gets transaction details by database, jsonId, errorId. 
	 * @param db It is database.
	 * @param jsonId It is String.
	 * @param errorId It is a String.
	 * @return List of the transaction based on the database, jsonId and errorId.
	 */
	List<String> getTransationDtl(ApiAuditDBTypeEnum db,String jsonId, String errorId);
	
	/**
	 * Gets dashboard by database, type, start and end local date time.
	 * @param db It is database server.
	 * @param select It is a String.
	 * @param type It is a String.
	 * @param start It is a local date time.
	 * @param end It is a local date time.
	 * @param force It is a boolean value.
	 * @return List of the dashboard based on the type, start and end local date time. 
	 */
	List<Graph<Integer>> getDashBoard(ApiAuditDBTypeEnum db,String select, String type, String start, String end, boolean force);
	
	/**
	 * Finds buyer seller transaction details by database, type, start and end local date time.
	 * @param db It is a database server.
	 * @param type It is a type.
	 * @param start It is a local date time.
	 * @param end It is a local date time.
	 * @param flag It is a boolean value.
	 * @return List of the buyer seller transaction based on the database, type, start and end local date time.
	 */
	List<Map<String, Object>> findByBuyerSellerTransactionByDate(ApiAuditDBTypeEnum db,String type, String start, String end, boolean flag);
	
	/**
	 * Gets all files by database and filename.
	 * @param db It is a database. 
	 * @param fileName It is a String.
	 * @return List of the file based on the database and file name.
	 */
	Map<String,List<String>> getAllFiles(ApiAuditDBTypeEnum db,String fileName);
	/**
	 * Downloads files by database, directory name, and file name.
	 * @param db It is a database server.
	 * @param dirName It is a directory name.
	 * @param fileName It is a file name.
	 * @return Downloads files based on the database, directory name and file name
	 */
	FileBo getDownloadFilesByName(ApiAuditDBTypeEnum db,String dirName, String fileName);
 
	void apiAuditinsertSeller(ApiAuditDBTypeEnum db, boolean force);
	void apiAuditinsertBuyer(ApiAuditDBTypeEnum db, boolean force);
	List<CountSellerBuyerProjection> getBuyerList(ApiAuditDBTypeEnum db, LocalDateTime start, LocalDateTime end);
	List<CountSellerBuyerProjection> getSellerList(ApiAuditDBTypeEnum db, LocalDateTime start, LocalDateTime end);
	 
 
}
