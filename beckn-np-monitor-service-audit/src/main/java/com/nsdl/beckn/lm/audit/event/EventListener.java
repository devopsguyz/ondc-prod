package com.nsdl.beckn.lm.audit.event;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
//import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.nsdl.beckn.lm.audit.db.mumbai.ApiAuditMumbaiRepository;
import com.nsdl.beckn.lm.audit.model.ApiAuditParentEntity;
import com.nsdl.beckn.lm.audit.model.FileBo;
import com.nsdl.beckn.lm.audit.utl.CommonUtl;
import com.nsdl.beckn.lm.audit.utl.SecurityUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EventListener {

	@Value("${summary.report.dir}")
	private String PATH;

	@Autowired
	ApiAuditMumbaiRepository apiAuditRepository;

	@Autowired
	SecurityUtils securityUtils;

	@Value("${page.size.transation}")
	private int pageSize;

	private String createFileDir(FileEvent fileEvent, String date) {
		String path = PATH+ "/" + date + "-" + fileEvent.getUserId();
		File directory = new File(path);
		if (!directory.exists()) {
			boolean isDirectory = directory.mkdir();
			if (!isDirectory) {
				throw new RuntimeException("unable to create directory");
			}
		}

		return path;
	}

	@Async
	@org.springframework.context.event.EventListener
	@Transactional(value = "transactionTransactionManager")
	void sendMsgEvent(FileEvent fileEvent) {

		
		
		LocalDateTime startDt = CommonUtl.convertStartStringtoDate(fileEvent.getStartDt());
		LocalDateTime endDt = CommonUtl.convertEndStringtoDate(fileEvent.getEndDt());
		// timestamp
		String date = fileEvent.date;
		FileBo bo = new FileBo();
		Pageable pageable = PageRequest.of(0, pageSize);
		int startval = 1;

		while (true) {
			try {
				Page<ApiAuditParentEntity> page = this.apiAuditRepository.findByCreatedOnBetweenOrderByCreatedOnAsc(startDt,
						endDt, pageable);
				// create Dir
				String path = createFileDir(fileEvent, date);
				int pageNumber = page.getNumber() + 1;
				String filePath = startval + "-" + pageSize * pageNumber + ".xlsx";
				// create zip file
				String zipFileName = path + "/" + startval + "-" + pageSize * pageNumber + ".zip";
				startval = startval + pageSize;
				List<ApiAuditParentEntity> apiAuditEntities = page.getContent();

				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("summaryreport");
				writeHeaderLine(sheet);
				writeDataLines(apiAuditEntities, workbook, sheet);
				ByteArrayOutputStream exportFile = new ByteArrayOutputStream();
				workbook.write(exportFile);

				try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFileName));) {
					ZipEntry zipEntry = new ZipEntry(filePath);
					zos.putNextEntry(zipEntry);
					byte[] buffer = new byte[1024];

					zos.write(exportFile.toByteArray(), 0, exportFile.toByteArray().length);

					zos.closeEntry();
				}
				if (!page.hasNext()) {
					break;
				}
				pageable = page.nextPageable();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private void writeDataLines(List<ApiAuditParentEntity> result, XSSFWorkbook workbook, XSSFSheet sheet) {
		int rowCount = 1;
		for (ApiAuditParentEntity apiAuditEntity : result) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;

			Cell cell = row.createCell(columnCount++);
			cell.setCellValue(CommonUtl.convertDatetoString(apiAuditEntity.getCreatedOn()));

			cell = row.createCell(columnCount++);
			cell.setCellValue(apiAuditEntity.getTransactionId());

			cell = row.createCell(columnCount++);
			cell.setCellValue(apiAuditEntity.getMessageId());

			cell = row.createCell(columnCount++);
			cell.setCellValue(apiAuditEntity.getBuyerId());

			cell = row.createCell(columnCount++);
			cell.setCellValue(apiAuditEntity.getSellerId());

			cell = row.createCell(columnCount++);
			cell.setCellValue(apiAuditEntity.getAction());

			cell = row.createCell(columnCount++);
			cell.setCellValue(apiAuditEntity.getType());

			cell = row.createCell(columnCount++);
			cell.setCellValue(apiAuditEntity.getError());

			cell = row.createCell(columnCount);
			cell.setCellValue(apiAuditEntity.getHeaders());
		}
	}

	private void writeHeaderLine(XSSFSheet sheet) {

		Row headerRow = sheet.createRow(0);
		Cell headerCell = headerRow.createCell(0);
		headerCell.setCellValue("created_on");
		headerCell = headerRow.createCell(1);
		headerCell.setCellValue("transaction_id");
		headerCell = headerRow.createCell(2);
		headerCell.setCellValue("message_id");
		headerCell = headerRow.createCell(3);
		headerCell.setCellValue("buyer_id");
		headerCell = headerRow.createCell(4);
		headerCell.setCellValue("seller_id");

		headerCell = headerRow.createCell(5);
		headerCell.setCellValue("action");

		headerCell = headerRow.createCell(6);
		headerCell.setCellValue("type");

		headerCell = headerRow.createCell(7);
		headerCell.setCellValue("error");

		headerCell = headerRow.createCell(8);
		headerCell.setCellValue("Header");
	}
}
