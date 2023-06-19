package com.nsdl.beckn.common.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nsdl.beckn.common.model.AuditDataModel;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuditFileService {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

	@Value("${beckn.persistence.file-path}")
	private String filePath;

	public void fileAudit(AuditDataModel dataModel) throws IOException {
		String fileName = dataModel.getAction().toLowerCase() + "_" + dataModel.getMessageId() + "_" + System.currentTimeMillis();

		// byte[] strToBytes = this.mapper.writeValueAsString(dataModel).getBytes(); // writing complete model as json part to file
		byte[] strToBytes = dataModel.getJson().getBytes(); // writing only the json part to file

		LocalDate now = LocalDate.now();
		String folderName = now.format(FORMATTER).toUpperCase();
		StringBuilder sb = new StringBuilder();
		StringBuilder file = sb.append(this.filePath).append(folderName).append("/").append(fileName).append(".json");

		Path path = Paths.get(file.toString());

		Files.createDirectories(path.getParent());
		Files.createFile(path);
		Files.write(path, strToBytes);

		log.info("file create at path {}", file);
	}

}
