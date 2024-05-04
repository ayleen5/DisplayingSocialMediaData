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

	public void processCsvFile(String directoryPath, FileType fileType , boolean isWithTime) {

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
			String accountID) {
		try {
			FileInfo[] csvRows = loaderServiceHelper.getCsvFiles(directoryPath);

			for (FileInfo fileInfo : csvRows) {
				LocalDateTime fileTimestamp = loaderServiceHelper.extractTimestampFromFileName(fileInfo.getPath());
				if (fileTimestamp.isAfter(startDate) && fileTimestamp.isBefore(endDate)) {
					// Process the file only if its timestamp is within the specified range and
					// matches the account ID
					String filename = Paths.get(fileInfo.getName()).getFileName().toString();
					String userId = filename.split("_")[0];
					if (loaderRepository.count() == 0 || loaderServiceHelper.isWithinLastHour(fileTimestamp, true)) {
						try (BufferedReader reader = new BufferedReader(
								new InputStreamReader(new URL(fileInfo.getDownloadUrl()).openStream()));
								CSVReader csvReader = new CSVReader(reader)) {

							String[] nextRecord;
							while ((nextRecord = csvReader.readNext()) != null) {
								try {
									Loader post = new Loader();
									if (!nextRecord[0].isBlank()) {
										Loader existingPost = loaderRepository.findByAccountLoaderAndTimestampAndPostId(
												userId, fileTimestamp, nextRecord[0]);

										if (existingPost != null)
											break;
										double impressions = loaderServiceHelper.isValidNumeric(nextRecord[17])
												? Double.parseDouble(nextRecord[17])
												: 0.0;
										double reach = loaderServiceHelper.isValidNumeric(nextRecord[18])
												? Double.parseDouble(nextRecord[18])
												: 0.0;
										double totalClicks = loaderServiceHelper.isValidNumeric(nextRecord[24])
												? Double.parseDouble(nextRecord[24])
												: 0.0;
										double ReactionsCommentsShares = loaderServiceHelper.isValidNumeric(
												nextRecord[20]) ? Double.parseDouble(nextRecord[20]) : 0.0;
										double reactions = loaderServiceHelper.isValidNumeric(nextRecord[21])
												? Double.parseDouble(nextRecord[21])
												: 0.0;
										double comments = loaderServiceHelper.isValidNumeric(nextRecord[22])
												? Double.parseDouble(nextRecord[22])
												: 0.0;
										double shares = loaderServiceHelper.isValidNumeric(nextRecord[23])
												? Double.parseDouble(nextRecord[23])
												: 0.0;

										post.setPostId(nextRecord[0]);
										post.setContentType(nextRecord[11]);
										post.setImpressions(impressions);
										post.setViews(reach);
										post.setClicks(totalClicks);
										post.setLikes(reactions);
										post.setComments(comments);
										post.setShares(shares);
										post.setTimestamp(fileTimestamp);
										post.setAccountLoader(userId);

										if (impressions == 0)
											post.setCTR(0);
										else
											post.setCTR(totalClicks / impressions);
										if (reach == 0)
											post.setEngagementrate(0);
										else
											post.setEngagementrate(ReactionsCommentsShares / reach);

										loaderServiceHelper.Aggrigation(post);
									}
								} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
									e.printStackTrace();
								}
							}
						} catch (IOException | CsvValidationException e) {
							e.printStackTrace();
						}
					}

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
