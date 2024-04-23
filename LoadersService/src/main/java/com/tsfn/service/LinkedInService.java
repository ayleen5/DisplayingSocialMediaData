package com.tsfn.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.tsfn.model.LinkedIn;
import com.tsfn.model.LoaderDTO;
import com.tsfn.repository.LoaderDTORepo;

@Service
public class LinkedInService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private LoaderDTORepo loaderDTORepo;

	Map<String, LoaderDTO> aggregatedPosts = new HashMap<>();

	public void processCsvLinkedInFile(String directoryPath) {
		try {
			List<String> linkedinCsvFiles = Files.list(Paths.get(directoryPath))
					.filter(path -> path.getFileName().toString().contains("linkedin")
							&& path.getFileName().toString().endsWith(".csv"))
					.map(path -> path.toAbsolutePath().toString()).collect(Collectors.toList());

			for (String filePath : linkedinCsvFiles) {

				LocalDateTime fileTimestamp = extractTimestampFromFileName(filePath);
				// the user id
				String filename = Paths.get(filePath).getFileName().toString();
				String userId = filename.split("_")[0];

				
				// if the database or ( it is within the last hour && no other file with the
				// same user and time stamp)

				if (loaderDTORepo.count() == 0 || (isWithinLastHour(fileTimestamp))) {
					try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
							CSVReader csvReader = new CSVReader(reader)) {

						String[] nextRecord;
						while ((nextRecord = csvReader.readNext()) != null) {
							try {

								LoaderDTO post = new LoaderDTO();
								if (!nextRecord[0].isBlank()) {
									LoaderDTO existingPost = loaderDTORepo.findByAccountLoaderAndTimestampAndPostId(
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
									
									double offsiteVideoViews=isValidNumeric(nextRecord[11]) ? Double.parseDouble(nextRecord[11])
											: 0.0;
									double offsiteViews = isValidNumeric(nextRecord[10]) ? Double.parseDouble(nextRecord[10])
											: 0.0;
									double Views = offsiteVideoViews + offsiteViews; // (Excluding offsite video views)] + [Offsite Views]

									post.setPostId(nextRecord[1]);// postLink
									post.setContentType(nextRecord[19]);
									post.setImpressions(impressions);
									post.setViews(Views ); // to fix later
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
		} catch (IOException e) {
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

	public void Aggrigation(LoaderDTO post) {

		String postId = post.getPostId();
		if (aggregatedPosts.containsKey(postId)) {
			// Post with the same ID already exists, update impressions and reach
			LoaderDTO existingPost = aggregatedPosts.get(postId);
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
		for (LoaderDTO aggregatedPost : aggregatedPosts.values()) {
			loaderDTORepo.save(aggregatedPost);
		}
	}

//	public void processCsvLinkedInFile(String filePath) {
//
//		try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
//				CSVReader csvReader = new CSVReader(reader)) {
//
//			String[] nextRecord;
//			while ((nextRecord = csvReader.readNext()) != null) {
//				try {
//					System.out.println("LinkedInService LinkedIn   LinkedIn  LinkedIn:");
//					LinkedIn entity = new LinkedIn();
//					entity.setPostTitle(nextRecord[0]);
//					entity.setPostLink(nextRecord[1]);
//					entity.setPostType(nextRecord[2]);
//					entity.setCampaignName(nextRecord[3]);
//					entity.setPostedBy(nextRecord[4]);
////					entity.setCreatedDate(parseDate(nextRecord[5]));
////					entity.setCampaignStartDate(parseDate(nextRecord[6]));
////					entity.setCampaignEndDate(parseDate(nextRecord[7]));
//					entity.setAudience(nextRecord[8]);
//					entity.setImpressions(Double.parseDouble(nextRecord[9]));
////  	                    entity.setVideoViews(Double.parseDouble(nextRecord[10]));
////  	                    entity.setOffsiteViews(Double.parseDouble(nextRecord[11]));
//					entity.setClicks(Double.parseDouble(nextRecord[12]));
//					entity.setCTR(Double.parseDouble(nextRecord[13]));
//					entity.setLikes(Double.parseDouble(nextRecord[14]));
//					entity.setComments(Double.parseDouble(nextRecord[15]));
//					entity.setReposts(Double.parseDouble(nextRecord[16]));
//					entity.setFollows(Double.parseDouble(nextRecord[17]));
//					entity.setEngagementRate(Double.parseDouble(nextRecord[18]));
//					entity.setContentType(nextRecord[19]);
//
//					LoaderDTO dto = modelMapper.map(entity, LoaderDTO.class);
//					if (dto != null) {
//						loaderDTORepo.save(dto);
//					} else {
//						System.out.println("DTO conversion failed");
//					}
//				} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
//					e.printStackTrace();
//				}
//			}
//		} catch (IOException | CsvValidationException e) {
//			e.printStackTrace();
//		}
//	}

//	public void processCsvLinkedInFile(String directoryPath) {
//		try {
//			List<String> linkedinCsvFiles = Files.list(Paths.get(directoryPath))
//					.filter(path -> path.getFileName().toString().contains("linkedin")
//							&& path.getFileName().toString().endsWith(".csv"))
//					.map(path -> path.toAbsolutePath().toString()).collect(Collectors.toList());
//
//			for (String filePath : linkedinCsvFiles) {
//
//				String filename = Paths.get(filePath).getFileName().toString();
//				String[] parts = filename.split("_|T");
//				String year = parts[2];
//				String month = parts[3];
//				String day = parts[4];
//				String hour = parts[5];
//				String minute = parts[6];
//				String second = parts[7].split("\\.")[0]; // Remove the file extension
//
//				// Construct LocalDateTime object from extracted parts
//				LocalDateTime timestamp = LocalDateTime.of(Integer.parseInt(year), Integer.parseInt(month),
//						Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute),
//						Integer.parseInt(second));
//
//				// the user id
//				String userId = filename.split("_")[0];
//
//				try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
//						CSVReader csvReader = new CSVReader(reader)) {
//
//					String[] nextRecord;
//					while ((nextRecord = csvReader.readNext()) != null) {
//						try {
//							LinkedIn entity = new LinkedIn();
//							entity.setPostTitle(nextRecord[0]);
//							entity.setPostLink(nextRecord[1]);
//							entity.setPostType(nextRecord[2]);
//							entity.setCampaignName(nextRecord[3]);
//							entity.setPostedBy(nextRecord[4]);
//							entity.setCreatedDate(parseDate(nextRecord[5]));
//							entity.setCampaignStartDate(parseDate(nextRecord[6]));
//							entity.setCampaignEndDate(parseDate(nextRecord[7]));
//							entity.setAudience(nextRecord[8]);
//							entity.setImpressions(Double.parseDouble(nextRecord[9]));
////  	                    entity.setVideoViews(Double.parseDouble(nextRecord[10]));
////  	                    entity.setOffsiteViews(Double.parseDouble(nextRecord[11]));
//							entity.setClicks(Double.parseDouble(nextRecord[12]));
//							entity.setCTR(Double.parseDouble(nextRecord[13]));
//							entity.setLikes(Double.parseDouble(nextRecord[14]));
//							entity.setComments(Double.parseDouble(nextRecord[15]));
//							entity.setReposts(Double.parseDouble(nextRecord[16]));
//							entity.setFollows(Double.parseDouble(nextRecord[17]));
//							entity.setEngagementRate(Double.parseDouble(nextRecord[18]));
//							entity.setContentType(nextRecord[19]);
//
//							LoaderDTO dto = modelMapper.map(entity, LoaderDTO.class);
//							dto.setTimestamp(timestamp);
//							dto.setAccountLoader(userId);
//
//							if (dto != null) {
//								loaderDTORepo.save(dto);
//							} else {
//								System.out.println("DTO conversion failed");
//							}
//						} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
//							e.printStackTrace();
//						}
//					}
//				} catch (IOException | CsvValidationException e) {
//					e.printStackTrace();
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

//	    public void processCsvLinkedInFile(MultipartFile file) {
//	        try (Reader reader = new InputStreamReader(file.getInputStream());
//	             CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {
//
//	            String[] nextRecord;
//	            while ((nextRecord = csvReader.readNext()) != null) {
//	                try {
//	                	LinkedIn entity = new LinkedIn();
//	                	entity.setPostTitle(nextRecord[0]);
//	                    entity.setPostLink(nextRecord[1]);
//	                    entity.setPostType(nextRecord[2]);
//	                    entity.setCampaignName(nextRecord[3]);
//	                    entity.setPostedBy(nextRecord[4]);
//	                    entity.setCreatedDate(parseDate(nextRecord[5]));
//	                    entity.setCampaignStartDate(parseDate(nextRecord[6]));
//	                    entity.setCampaignEndDate(parseDate(nextRecord[7]));
//	                    entity.setAudience(nextRecord[8]);
//	                    entity.setImpressions(Double.parseDouble(nextRecord[9]));
////	                    entity.setVideoViews(Double.parseDouble(nextRecord[10]));
////	                    entity.setOffsiteViews(Double.parseDouble(nextRecord[11]));
//	                    entity.setClicks(Double.parseDouble(nextRecord[12]));
//	                    entity.setCTR(Double.parseDouble(nextRecord[13]));
//	                    entity.setLikes(Double.parseDouble(nextRecord[14]));
//	                    entity.setComments(Double.parseDouble(nextRecord[15]));
//	                    entity.setReposts(Double.parseDouble(nextRecord[16]));
//	                    entity.setFollows(Double.parseDouble(nextRecord[17]));
//	                    entity.setEngagementRate(Double.parseDouble(nextRecord[18]));
//	                    entity.setContentType(nextRecord[19]);
//	                    
//	                    
//	                    LoaderDTO dto =  modelMapper.map(entity, LoaderDTO.class);
//	                    if (dto != null) {
//	                        loaderDTORepo.save(dto);
//	                    } else {
//	                        System.out.println("DTO conversion failed");
//	                    }
//	                    loaderDTORepo.save(dto); // Save the DTO directly, if LoaderDTO is an entity
//	                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
//	                    e.printStackTrace();
//	                }
//	            }
//	        } catch (IOException | CsvValidationException e) {
//	            e.printStackTrace();
//	        }
//	    }

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

	public List<LoaderDTO> getAllLinkedInFiles() {
		return loaderDTORepo.findAll();
	}

	public int deleteAllLinkedInFiles() {
		try {
			long count = loaderDTORepo.count();
			loaderDTORepo.deleteAll();
			return (int) count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
