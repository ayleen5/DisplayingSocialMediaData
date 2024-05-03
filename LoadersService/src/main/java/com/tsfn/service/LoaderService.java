package com.tsfn.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.el.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.tsfn.model.FileInfo;
import com.tsfn.model.Loader;
import com.tsfn.repository.LoaderRepository;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Data
@Service
public class LoaderService {

	@Autowired
	private LoaderRepository loaderRepository;
	@Autowired
	private RestTemplate restTemplate;

	private boolean Intasgram = true;
	private boolean Facebook = true;
	private boolean LinkedIn = true;

	Map<String, Loader> aggregatedPosts = new HashMap<>();

	public void processCsvInstagramFile(String directoryPath) {

		try {
			FileInfo[] csvRows = getCsvFiles(directoryPath);

			for (FileInfo fileInfo : csvRows) {

				LocalDateTime fileTimestamp = extractTimestampFromFileName(fileInfo.getPath());
				// the user id
				String filename = Paths.get(fileInfo.getName()).getFileName().toString();
				String userId = filename.split("_")[0];

				if (isWithinLastHour(fileTimestamp)) {
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

									double impressions = isValidNumeric(nextRecord[11])
											? Double.parseDouble(nextRecord[11])
											: 0.0;
									double saves = isValidNumeric(nextRecord[17]) ? Double.parseDouble(nextRecord[17])
											: 0.0;
									double reach = isValidNumeric(nextRecord[12]) ? Double.parseDouble(nextRecord[12])
											: 0.0;
									double likes = isValidNumeric(nextRecord[15]) ? Double.parseDouble(nextRecord[15])
											: 0.0;

									post.setPostId(nextRecord[0]);
									post.setContentType(nextRecord[8]);
									post.setImpressions(impressions);
									post.setViews(reach); // reach
									post.setClicks(saves); // saves
									post.setLikes(likes);
									post.setComments(
											isValidNumeric(nextRecord[16]) ? Double.parseDouble(nextRecord[16]) : 0.0);
									post.setShares(
											isValidNumeric(nextRecord[13]) ? Double.parseDouble(nextRecord[13]) : 0.0);

									if (impressions > 0 && saves > 0) {
										post.setCTR(saves / impressions);
									} else {
										post.setCTR(0);
									}
									if (reach > 0) {
										post.setEngagementrate(likes / (double) reach);
									} else {
										post.setEngagementrate(0);
									}

									post.setTimestamp(fileTimestamp);
									post.setAccountLoader(userId);
									Aggrigation(post);

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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processCsvLinkedInFile(String directoryPath) {

		try {
			FileInfo[] csvRows = getCsvFiles(directoryPath);

			for (FileInfo fileInfo : csvRows) {

				LocalDateTime fileTimestamp = extractTimestampFromFileName(fileInfo.getPath());
				// the user id
				String filename = Paths.get(fileInfo.getName()).getFileName().toString();
				String userId = filename.split("_")[0];

				if (isWithinLastHour(fileTimestamp)) {
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
									double impressions = isValidNumeric(nextRecord[9])
											? Double.parseDouble(nextRecord[9])
											: 0.0;

									double clicks = isValidNumeric(nextRecord[12]) ? Double.parseDouble(nextRecord[12])
											: 0.0;

									double likes = isValidNumeric(nextRecord[14]) ? Double.parseDouble(nextRecord[14])
											: 0.0;
									double comments = isValidNumeric(nextRecord[15])
											? Double.parseDouble(nextRecord[15])
											: 0.0;
									double reports = isValidNumeric(nextRecord[16]) ? Double.parseDouble(nextRecord[16])
											: 0.0;
									double CTR = isValidNumeric(nextRecord[13]) ? Double.parseDouble(nextRecord[13])
											: 0.0;
									double Engagementrate = isValidNumeric(nextRecord[18])
											? Double.parseDouble(nextRecord[18])
											: 0.0;

									double offsiteVideoViews = isValidNumeric(nextRecord[11])
											? Double.parseDouble(nextRecord[11])
											: 0.0;
									double offsiteViews = isValidNumeric(nextRecord[10])
											? Double.parseDouble(nextRecord[10])
											: 0.0;
									double Views = offsiteVideoViews + offsiteViews; // (Excluding offsite video views)]
																						// + [Offsite Views]

									post.setPostId(nextRecord[1]);// postLink
									post.setContentType(nextRecord[19]);
									post.setImpressions(impressions);
									post.setViews(Views); // to fix later
									post.setClicks(clicks);
									post.setLikes(likes);
									post.setComments(comments);
									post.setShares(reports);
									post.setTimestamp(fileTimestamp);
									post.setAccountLoader(userId);
									post.setCTR(CTR);
									post.setEngagementrate(Engagementrate);
									Aggrigation(post);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processCsvFacebookFile(String directoryPath) {

		try {
			FileInfo[] csvRows = getCsvFiles(directoryPath);

			for (FileInfo fileInfo : csvRows) {

				LocalDateTime fileTimestamp = extractTimestampFromFileName(fileInfo.getPath());
				// the user id
				String filename = Paths.get(fileInfo.getName()).getFileName().toString();
				String userId = filename.split("_")[0];

				if (isWithinLastHour(fileTimestamp)) {
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
									double impressions = isValidNumeric(nextRecord[17])
											? Double.parseDouble(nextRecord[17])
											: 0.0;
									double reach = isValidNumeric(nextRecord[18]) ? Double.parseDouble(nextRecord[18])
											: 0.0;
									double totalClicks = isValidNumeric(nextRecord[24])
											? Double.parseDouble(nextRecord[24])
											: 0.0;
									double ReactionsCommentsShares = isValidNumeric(nextRecord[20])
											? Double.parseDouble(nextRecord[20])
											: 0.0;
									double reactions = isValidNumeric(nextRecord[21])
											? Double.parseDouble(nextRecord[21])
											: 0.0;
									double comments = isValidNumeric(nextRecord[22])
											? Double.parseDouble(nextRecord[22])
											: 0.0;
									double shares = isValidNumeric(nextRecord[23]) ? Double.parseDouble(nextRecord[23])
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

									Aggrigation(post);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private LocalDateTime extractTimestampFromFileName(String filePath) {

		String filename = Paths.get(filePath).getFileName().toString();
		String[] parts = filename.split("_|T");
		String year = parts[2];
		String month = parts[3];
		String day = parts[4];
		String hour = parts[5];
		String minute = parts[6];
		String second = parts[7].split("\\.")[0]; // Remove the file extension
		return LocalDateTime.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day),
				Integer.parseInt(hour), Integer.parseInt(minute), Integer.parseInt(second));
	}

	public boolean isWithinLastHour(LocalDateTime timestamp) {
		// Get the current time
		LocalDateTime currentTime = LocalDateTime.now();

		// Calculate the start time of the last hour
		LocalDateTime lastHourStartTime = currentTime.minusHours(1);

		// Check if the timestamp is after the start time of the last hour
		return timestamp.isAfter(lastHourStartTime) && timestamp.isBefore(currentTime);
	}

	private boolean isValidNumeric(String str) {
		if (str == null || str.isBlank()) {
			return false;
		}
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public void Aggrigation(Loader post) {

		String postId = post.getPostId();
		if (aggregatedPosts.containsKey(postId)) {
			// Post with the same ID already exists, update impressions and reach
			Loader existingPost = aggregatedPosts.get(postId);
			existingPost.setImpressions(existingPost.getImpressions() + post.getImpressions());
			existingPost.setViews(existingPost.getViews() + post.getViews());
			existingPost.setClicks(existingPost.getClicks() + post.getClicks());
			existingPost.setCTR(existingPost.getCTR() + post.getCTR());
			existingPost.setLikes(existingPost.getLikes() + post.getLikes());
			existingPost.setComments(existingPost.getComments() + post.getComments());
			existingPost.setShares(existingPost.getShares() + post.getShares());
			existingPost.setEngagementrate(existingPost.getEngagementrate() + post.getEngagementrate());
		} else {
			// Add the post to the map
			aggregatedPosts.put(postId, post);
		}

		// Save the aggregated posts to the database
		for (Loader aggregatedPost : aggregatedPosts.values()) {
			loaderRepository.save(aggregatedPost);
		}
	}

	private Date parseDate(String dateString) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = sdf.parse(dateString);
			return new Date(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Loader> getAllFiles() {
		return loaderRepository.findAll();
	}

	public int deleteAllFiles() {
		try {
			long count = loaderRepository.count();
			loaderRepository.deleteAll();
			return (int) count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public List<Loader> findAllByAccountLoader(String accountLoader) {
		return loaderRepository.findAllByAccountLoader(accountLoader);
	}

	public List<Loader> getFilesWithImpressionGreaterThanThreshold(int threshold) {
		return loaderRepository.findAllByImpressionsGreaterThanEqual(threshold);
	}

	public FileInfo[] getCsvFiles(String directoryPath) {
		String csvData = restTemplate.getForObject(directoryPath, String.class);
		String[] lines = csvData.split("\\r?\\n");
		FileInfo[] fileInfoArray = FileInfo.fromJson(csvData);
		return fileInfoArray;
	}
}
