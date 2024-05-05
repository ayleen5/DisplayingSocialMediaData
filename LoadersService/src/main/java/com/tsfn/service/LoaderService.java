package com.tsfn.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;

import java.nio.file.Paths;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.tsfn.helper.CsvProcessor;
import com.tsfn.helper.FileType;
import com.tsfn.helper.LoaderServiceHelper;
import com.tsfn.model.FileInfo;
import com.tsfn.model.Loader;
import com.tsfn.repository.LoaderRepository;

import lombok.Data;

@Data
@Service
public class LoaderService {

	@Autowired
	private LoaderRepository loaderRepository;

	@Autowired
	private LoaderServiceHelper loaderServiceHelper;

	@Autowired
	private CsvProcessor csvProcessor;

	private boolean Intasgram = true;
	private boolean Facebook = true;
	private boolean LinkedIn = true;

	public void processCsvFile(String directoryPath, FileType fileType, boolean isWithTime) {

		try {
			csvProcessor.processCsvFile(directoryPath, fileType, isWithTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Date parseDate(String dateString) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = sdf.parse(dateString);
			return new Date(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void processCsvFilesInRange(String directoryPath, LocalDateTime startDate, LocalDateTime endDate,

			String accountID, FileType fileType) {

		try {

			FileInfo[] csvRows = loaderServiceHelper.getCsvFiles(directoryPath);



			for (FileInfo fileInfo : csvRows) {

				LocalDateTime fileTimestamp = loaderServiceHelper.extractTimestampFromFileName(fileInfo.getPath());

				if (fileTimestamp.isAfter(startDate) && fileTimestamp.isBefore(endDate)) {

					String filename = Paths.get(fileInfo.getName()).getFileName().toString();

					String userId = filename.split("_")[0];

					csvProcessor.processCsvRow(fileInfo, fileTimestamp, userId, fileType);					       





				}

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public List<Loader> findAllByAccountLoader(String accountLoader) {
		return loaderRepository.findAllByAccountLoader(accountLoader);
	}

	public List<Loader> getFilesWithImpressionGreaterThanThreshold(int threshold) {
		return loaderRepository.findAllByImpressionsGreaterThanEqual(threshold);
	}

}
