package com.nsdl.beckn.lm.audit.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nsdl.beckn.lm.audit.config.ApiAuditDBTypeEnum;
import com.nsdl.beckn.lm.audit.model.FileBo;
import com.nsdl.beckn.lm.audit.model.Graph;
import com.nsdl.beckn.lm.audit.model.Select;
import com.nsdl.beckn.lm.audit.model.projection.ApiAuditEntityBuyerProjection;
import com.nsdl.beckn.lm.audit.model.projection.ApiAuditEntitySellerProjection;
import com.nsdl.beckn.lm.audit.model.projection.CountSellerBuyerProjection;
import com.nsdl.beckn.lm.audit.model.response.Response;
import com.nsdl.beckn.lm.audit.model.response.TransactionMapResponse;
import com.nsdl.beckn.lm.audit.service.ApiAuditService;
import com.nsdl.beckn.lm.audit.utl.CommonUtl;
import com.nsdl.beckn.lm.audit.utl.Constants;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api")
public class ApiAuditController {
	@Autowired
	ApiAuditService apiAuditService;
	
 
//	@Value("${page.size.transation}")
//	Integer pageSize;
    /**
     * This method is return Database server details by DB
     * @return Its returns the database details.
     */
	@GetMapping("/get/db")
	public ResponseEntity<Response<List<Select>>> getDB() {
		return Response.ok(Constants.db);
	}

	 /**
     * This Method is return the audit transaction details by transaction id
     * @param   String Id it is a unique transaction id.
     * @return  Its return the Map object containing the audit transaction details based on the type.
     *          
     */
	@GetMapping("/get/transaction/{id}")
	public ResponseEntity<Response<Map<String, List<Map<String, Object>>>>> findTransationId(@PathVariable String id,
			@RequestParam(name = "db", required = true) String db) {
		return Response.ok(this.apiAuditService.findTransactionById(ApiAuditDBTypeEnum.valueOf(db),id));
	}
	
	/**
     * This Method is return the audit transaction details by date and used page for pagination.
     * @param   String It is a unique transaction id.
     * 			Integer It is a pagination. 
     * @return  Its return the Map object containing the audit transaction details based on the type .
     *          
     */
	@GetMapping("/get/transaction/by/date/{start}/{end}/{page}")
	public ResponseEntity<Response<TransactionMapResponse>> findTransationIdByDate(@RequestParam(name = "db", required = true) String db,
			@PathVariable String start, @PathVariable String end, @PathVariable Integer page) {
 
		return Response.ok( this.apiAuditService.findTransactionByDate(ApiAuditDBTypeEnum.valueOf(db),start, end, page));
	}
	
	/**
     * Returns the API audit database server details by distinctTransationIds.
     * @param   String It is a database.
     * @return  Its return DB server based on distinctTransationIds start and end date.
     *          
     */
	@GetMapping("/all/transaction")
	public ResponseEntity<Response<List<String>>> distinctTransationIds(@RequestParam(name = "db", required = true) String db) {
		return Response.ok(this.apiAuditService.getDistinctId(ApiAuditDBTypeEnum.valueOf(db)));
	}
	
	/**
     * Returns the API audit database server details by type start and end date.
     * @param   String It is a DB type start and end date.
     * @return  Its return transaction details based on type start and end date.
     *          
     */
	@GetMapping("/dashboard/{select}/{type}/{start}/{end}")
	public ResponseEntity<Response<List<Graph<Integer>>>> getDashBoard(@RequestParam(name = "db", required = true) String db,@PathVariable String select,
			@PathVariable String type, @PathVariable String start, @PathVariable String end) {
		return Response.ok(this.apiAuditService.getDashBoard(ApiAuditDBTypeEnum.valueOf(db),select, type, start, end, false));
	}
	/**
     * Returns the API audit seller transaction details by type start and end date.
     * @param   String It is a DB type start and end date.
     * @return  Its return seller transaction details based on type start and end date.
     *          
     */

	@GetMapping("/dashboard/seller/{select}/{type}/{start}/{end}")
	public ResponseEntity<Response<List<Graph<Integer>>>> getDashBoardSeller(@RequestParam(name = "db", required = true) String db,@PathVariable String select,
			@PathVariable String type, @PathVariable String start, @PathVariable String end) {
		return Response.ok(this.apiAuditService.getDashBoardSellerId(ApiAuditDBTypeEnum.valueOf(db),select, type, start, end, false));
	}
	/**
     * Returns the API audit buyer transaction details by type start and end date.
     * @param   String It is a DB type start and end date.
     * @return  Its return buyer transaction details based on type start and end date.
     *          
     */

	@GetMapping("/dashboard/buyer/{select}/{type}/{start}/{end}")
	public ResponseEntity<Response<List<Graph<Integer>>>> getDashBoardBuyer(@RequestParam(name = "db", required = true) String db,@PathVariable String select,
			@PathVariable String type, @PathVariable String start, @PathVariable String end) {
		return Response.ok(this.apiAuditService.getDashBoardBuyerId(ApiAuditDBTypeEnum.valueOf(db),select, type, start, end, false));
	}
	/**
     * Returns the API audit seller transaction details by type start and end date.
     * @param   String It is a DB type start and end date.
     * @return  Its return buyer List of ApiAuditEntityBuyerProjection based on start and end date.
     *          
     */

	@GetMapping("/dashboard/grid/buyer/{start}/{end}")
	public ResponseEntity<Response<List<ApiAuditEntityBuyerProjection>>> getDashBoardBuyer(@RequestParam(name = "db", required = true) String db,@PathVariable String start,
			@PathVariable String end) {
		return Response.ok(this.apiAuditService.getDashBoardBuyerAllList(ApiAuditDBTypeEnum.valueOf(db),start, end, false));
	}
	
	/**
     * Returns the API audit seller transaction details by type,start and end date.
     * @param   String It is a DB type start and end date.
     * @return  Its return seller List of ApiAuditEntityBuyerProjection based on start and end date.
     *          
     */
	@GetMapping("/dashboard/grid/seller/{start}/{end}")
	public ResponseEntity<Response<List<ApiAuditEntitySellerProjection>>> getDashBoardSeller(@RequestParam(name = "db", required = true) String db,@PathVariable String start,
			@PathVariable String end) {
		return Response.ok(this.apiAuditService.getDashBoardSellerAllList(ApiAuditDBTypeEnum.valueOf(db),start, end, false));
	}
	/**
     * Returns the API audit transaction count by type,start and end date.
     * @param   String It is a DB type start and end date.
     * @return  Its return buyer and seller transaction count of based on type,start and end date.
     *          
     */
	@GetMapping("/dashboard/count/{type}/{start}/{end}")
	public ResponseEntity<Response<List<Map<String, Object>>>> getDashBoardBuyerSeller(@RequestParam(name = "db", required = true) String db,@PathVariable String type,
			@PathVariable String start, @PathVariable String end) {
		return Response.ok(this.apiAuditService.findByBuyerSellerTransactionByDate(ApiAuditDBTypeEnum.valueOf(db),type, start, end, false));
	}
	
	/**
     * This method export summary report to excel saving excel in ZIP folder and returns ZIP folder..
     * @param   String It is a start and end date.
     * @return  Its return summary report excel ZIP folder based on start and end date. .
     *          
     */
	// get summary report api
	@GetMapping("/dashboard/summary/report/{start}/{end}")
	public ResponseEntity<Response<String>> getDashBoard(@RequestParam(name = "db", required = true) String db,@PathVariable String start, @PathVariable String end) {
		return Response.ok(this.apiAuditService.summaryReportExportToExcel(ApiAuditDBTypeEnum.valueOf(db),start, end));
	}

	/**
     * Returns audit transaction details by jsonID and ErrorID.
     * @param   String It is a jsonId and ErrorID.
     * @return  Its return transaction details based on jsonId and ErrorID.
     *          
     */
	@GetMapping("/transaction/get/{jsonId}/{errorId}")
	public ResponseEntity<Response<List<String>>> getTransactionDtl(@RequestParam(name = "db", required = true) String db,@PathVariable String jsonId,@PathVariable String errorId) {
		return Response.ok(this.apiAuditService.getTransationDtl(ApiAuditDBTypeEnum.valueOf(db),jsonId,errorId));
	}
	
	/**
     * This Method downloads summary report by directory Name and file Name. 
     * @param   String It is a DirName and FileName.
     * @return  Its return summary report based on directory name and file name.
     *          
     */
	@GetMapping(value ="/dashboard/summary/report/downloads/{dirName}/{fileName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<FileBo>> getDownloadFilesByName(@RequestParam(name = "db", required = true) String db,
			@PathVariable String dirName ,@PathVariable String fileName) {
		return Response.ok(this.apiAuditService.getDownloadFilesByName(ApiAuditDBTypeEnum.valueOf(db),dirName,fileName));
	}
	
	/**
     * This Method return summary file. 
     * @param   String It is a FileName.
     * @return  Its return the Map object containing the audit summary file  based on the filename..
     *          
     */
	@GetMapping("/dashboard/summary/report/{fileName}")
	public ResponseEntity<Response<Map<String,List<String>>>> getAllFiles(@RequestParam(name = "db", required = true) String db,@PathVariable String fileName) {
		return Response.ok(this.apiAuditService.getAllFiles(ApiAuditDBTypeEnum.valueOf(db),fileName));
	}
	 
	 
	@GetMapping("/seller/grid/{start}/{end}")
	public ResponseEntity<Response<List<CountSellerBuyerProjection>>> getListSeller(@RequestParam(name = "db", required = true) String db,@PathVariable String start,
			@PathVariable String end) {
		LocalDateTime startDt = CommonUtl.convertStartStringtoDate(start);
		LocalDateTime endDt = CommonUtl.convertEndStringtoDate(end);
		return Response.ok(this.apiAuditService.getSellerList(ApiAuditDBTypeEnum.valueOf(db),startDt, endDt));
	}
	
	@GetMapping("/buyer/grid/{start}/{end}")
	public ResponseEntity<Response<List<CountSellerBuyerProjection>>> getListBuyer(@RequestParam(name = "db", required = true) String db,@PathVariable String start,
			@PathVariable String end) {
		LocalDateTime startDt = CommonUtl.convertStartStringtoDate(start);
		LocalDateTime endDt = CommonUtl.convertEndStringtoDate(end);
		return Response.ok(this.apiAuditService.getBuyerList(ApiAuditDBTypeEnum.valueOf(db),startDt, endDt));
	}
}
