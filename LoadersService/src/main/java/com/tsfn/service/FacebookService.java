//package com.tsfn.service;
//
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.Reader;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.opencsv.CSVReader;
//import com.opencsv.CSVReaderBuilder;
//import com.opencsv.exceptions.CsvValidationException;
//import com.tsfn.model.Facebook;
//import com.tsfn.model.LoaderDTO;
//import com.tsfn.repository.LoaderDTORepo;
//
//@Service
//public class FacebookService {
//
//	@Autowired
//	private ModelMapper modelMapper;
//
//	@Autowired
//	private LoaderDTORepo loaderDTORepo;
//
//	Map<String, LoaderDTO> aggregatedPosts = new HashMap<>();
//
//	public void processCsvFacebookFile(String directoryPath) {
//		try {
//			List<String> facebookCsvFiles = Files.list(Paths.get(directoryPath))
//					.filter(path -> path.getFileName().toString().contains("facebook")
//							&& path.getFileName().toString().endsWith(".csv"))
//					.map(path -> path.toAbsolutePath().toString()).collect(Collectors.toList());
//
//			for (String filePath : facebookCsvFiles) {
//
//				LocalDateTime fileTimestamp = extractTimestampFromFileName(filePath);
//				// the user id
//				String filename = Paths.get(filePath).getFileName().toString();
//				String userId = filename.split("_")[0];
//
//
//				// if the database or ( it is within the last hour && no other file with the
//				// same user and time stamp)
//
//				if (loaderDTORepo.count() == 0 || (isWithinLastHour(fileTimestamp))) {
//					try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
//							CSVReader csvReader = new CSVReader(reader)) {
//
//						String[] nextRecord;
//						while ((nextRecord = csvReader.readNext()) != null) {
//							try {
//								LoaderDTO post = new LoaderDTO();
//								if (!nextRecord[0].isBlank()) {
//									LoaderDTO existingPost = loaderDTORepo.findByAccountLoaderAndTimestampAndPostId(
//											userId, fileTimestamp, nextRecord[0]);
//
//									if (existingPost != null)
//										break;
//									double impressions = isValidNumeric(nextRecord[17])
//											? Double.parseDouble(nextRecord[17])
//											: 0.0;
//									double reach = isValidNumeric(nextRecord[18]) ? Double.parseDouble(nextRecord[18])
//											: 0.0;
//									double totalClicks = isValidNumeric(nextRecord[24])
//											? Double.parseDouble(nextRecord[24])
//											: 0.0;
//									double ReactionsCommentsShares = isValidNumeric(nextRecord[20])
//											? Double.parseDouble(nextRecord[20])
//											: 0.0;
//									double reactions = isValidNumeric(nextRecord[21])
//											? Double.parseDouble(nextRecord[21])
//											: 0.0;
//									double comments = isValidNumeric(nextRecord[22])
//											? Double.parseDouble(nextRecord[22])
//											: 0.0;
//									double shares = isValidNumeric(nextRecord[23]) ? Double.parseDouble(nextRecord[23])
//											: 0.0;
//
//									post.setPostId(nextRecord[0]);
//									post.setContentType(nextRecord[11]);
//									post.setImpressions(impressions);
//									post.setViews(reach);
//									post.setClicks(totalClicks);
//									post.setLikes(reactions);
//									post.setComments(comments);
//									post.setShares(shares);
//									post.setTimestamp(fileTimestamp);
//									post.setAccountLoader(userId);
//
//									if (impressions == 0)
//										post.setCTR(0);
//									else
//										post.setCTR(totalClicks / impressions);
//									if (reach == 0)
//										post.setEngagementrate(0);
//									else
//										post.setEngagementrate(ReactionsCommentsShares / reach);
//
//									Aggrigation(post);
//								}
//							} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
//								e.printStackTrace();
//							}
//						}
//					} catch (IOException | CsvValidationException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private LocalDateTime extractTimestampFromFileName(String filePath) {
//		String filename = Paths.get(filePath).getFileName().toString();
//		String[] parts = filename.split("_|T");
//		String year = parts[2];
//		String month = parts[3];
//		String day = parts[4];
//		String hour = parts[5];
//		String minute = parts[6];
//		String second = parts[7].split("\\.")[0]; // Remove the file extension
//		return LocalDateTime.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day),
//				Integer.parseInt(hour), Integer.parseInt(minute), Integer.parseInt(second));
//	}
//
//	public boolean isWithinLastHour(LocalDateTime timestamp) {
//		// Get the current time
//		LocalDateTime currentTime = LocalDateTime.now();
//
//		// Calculate the start time of the last hour
//		LocalDateTime lastHourStartTime = currentTime.minusHours(1);
//
//		// Check if the timestamp is after the start time of the last hour
//		return timestamp.isAfter(lastHourStartTime) && timestamp.isBefore(currentTime);
//	}
//
//	private boolean isValidNumeric(String str) {
//		if (str == null || str.isBlank()) {
//			return false;
//		}
//		try {
//			Double.parseDouble(str);
//			return true;
//		} catch (NumberFormatException e) {
//			return false;
//		}
//	}
//
//	public void Aggrigation(LoaderDTO post) {
//
//		String postId = post.getPostId();
//		if (aggregatedPosts.containsKey(postId)) {
//			// Post with the same ID already exists, update impressions and reach
//			LoaderDTO existingPost = aggregatedPosts.get(postId);
//			existingPost.setImpressions(existingPost.getImpressions() + post.getImpressions());
//			existingPost.setViews(existingPost.getViews() + post.getViews());
//			existingPost.setClicks(existingPost.getClicks() + post.getClicks());
//			existingPost.setCTR(existingPost.getCTR() + post.getCTR());
//			existingPost.setLikes(existingPost.getLikes() + post.getLikes());
//			existingPost.setComments(existingPost.getComments() + post.getComments());
//			existingPost.setShares(existingPost.getShares() + post.getShares());
//			existingPost.setEngagementrate(existingPost.getEngagementrate() + post.getEngagementrate());
//		} else {
//			// Add the post to the map
//			aggregatedPosts.put(postId, post);
//		}
//
//		// Save the aggregated posts to the database
//		for (LoaderDTO aggregatedPost : aggregatedPosts.values()) {
//			loaderDTORepo.save(aggregatedPost);
//		}
//	}
//
////	public void processCsvFacebookFile(String filePath) {
////
////		try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
////				CSVReader csvReader = new CSVReader(reader)) {
////
////			String[] nextRecord;
////			while ((nextRecord = csvReader.readNext()) != null) {
////				try {
////					System.out.println(" FacebookService facebook   facebook  facebook:");
////					Facebook entity = new Facebook();
////					entity.setPostId(nextRecord[0]);
//////                          entity.setPageId(nextRecord[1]);
//////                          entity.setPageName(nextRecord[2]);
//////                          entity.setTitle(nextRecord[3]);
//////                          entity.setDescription(nextRecord[4]);
//////                          entity.setDurationSec(Double.parseDouble(nextRecord[5]));
////					// entity.setPublishTime(LocalDateTime.parse(nextRecord[6],
////					// DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//////                          entity.setCaptionType(nextRecord[7]);
//////                          entity.setPermalink(nextRecord[8]);
//////                          entity.setIsCrosspost(Boolean.parseBoolean(nextRecord[9]));
//////                          entity.setIsShare(Boolean.parseBoolean(nextRecord[10]));
////					entity.setPostType(nextRecord[11]);
//////                          entity.setLanguages(nextRecord[12]);
//////                          entity.setCustomLabels(nextRecord[13]);
//////                          entity.setFundedContentStatus(nextRecord[14]);
//////                          entity.setDataComment(nextRecord[15]);
////					// entity.setDate(LocalDateTime.parse(nextRecord[16],
////					// DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
////					entity.setImpressions(Double.parseDouble(nextRecord[17]));
////					entity.setReach(Double.parseDouble(nextRecord[18]));
////					entity.setImpressionsUnique_User(nextRecord[19]);
////					entity.setReactionsCommentsShares(Double.parseDouble(nextRecord[20]));
////					entity.setReactions(Double.parseDouble(nextRecord[21]));
////					entity.setComments(Double.parseDouble(nextRecord[22]));
////					entity.setShares(Double.parseDouble(nextRecord[23]));
////					entity.setTotalClicks(Double.parseDouble(nextRecord[24]));
//////                          entity.setOtherClicks(Double.parseDouble(nextRecord[25]));
//////                          entity.setPhotoClick(Double.parseDouble(nextRecord[26]));
//////                          entity.setLinkClick(Double.parseDouble(nextRecord[27]));
//////                          entity.setNegativeFeedback(Double.parseDouble(nextRecord[28]));
//////                          entity.setSecondsViewed(Double.parseDouble(nextRecord[29]));
//////                          entity.setAverageSecondsViewed(Double.parseDouble(nextRecord[30]));
//////                          entity.setEstimatedEarnings(Double.parseDouble(nextRecord[31]));
//////                          entity.setAdCPM(Double.parseDouble(nextRecord[32]));
//////                          entity.setAdImpressions(Double.parseDouble(nextRecord[33]));
////
////					LoaderDTO dto = modelMapper.map(entity, LoaderDTO.class);
////					if (dto != null) {
////						loaderDTORepo.save(dto);
////					} else {
////						System.out.println("DTO conversion failed");
////					}
////				} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
////					e.printStackTrace();
////				}
////			}
////		} catch (IOException | CsvValidationException e) {
////			e.printStackTrace();
////		}
////	}
//
////    public void processCsvFacebookFile(String directoryPath) {
////        try {
////            List<String> facebookCsvFiles = Files.list(Paths.get(directoryPath))
////                .filter(path -> path.getFileName().toString().contains("facebook") && path.getFileName().toString().endsWith(".csv"))
////                .map(path -> path.toAbsolutePath().toString())
////                .collect(Collectors.toList());
////
////            for (String filePath : facebookCsvFiles) {
////            	
////            	String filename = Paths.get(filePath).getFileName().toString();
////                String[] parts = filename.split("_|T");
////                String year = parts[2];
////                String month = parts[3];
////                String day = parts[4];
////                String hour = parts[5];
////                String minute = parts[6];
////                String second = parts[7].split("\\.")[0]; // Remove the file extension
////
////                // Construct LocalDateTime object from extracted parts
////                LocalDateTime timestamp = LocalDateTime.of(
////                    Integer.parseInt(year),
////                    Integer.parseInt(month),
////                    Integer.parseInt(day),
////                    Integer.parseInt(hour),
////                    Integer.parseInt(minute),
////                    Integer.parseInt(second)
////                );
////                
////				// the user id
////                String userId = filename.split("_")[0];
////                
////                
////                
////                try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
////                     CSVReader csvReader = new CSVReader(reader)) {
////
////                    String[] nextRecord;
////                    while ((nextRecord = csvReader.readNext()) != null) {
////                        try {
////                          Facebook entity = new Facebook();
////                          entity.setPostId(nextRecord[0]);
//////                          entity.setPageId(nextRecord[1]);
//////                          entity.setPageName(nextRecord[2]);
//////                          entity.setTitle(nextRecord[3]);
//////                          entity.setDescription(nextRecord[4]);
//////                          entity.setDurationSec(Double.parseDouble(nextRecord[5]));
////                          //entity.setPublishTime(LocalDateTime.parse(nextRecord[6], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//////                          entity.setCaptionType(nextRecord[7]);
//////                          entity.setPermalink(nextRecord[8]);
//////                          entity.setIsCrosspost(Boolean.parseBoolean(nextRecord[9]));
//////                          entity.setIsShare(Boolean.parseBoolean(nextRecord[10]));
////                          entity.setPostType(nextRecord[11]);
//////                          entity.setLanguages(nextRecord[12]);
//////                          entity.setCustomLabels(nextRecord[13]);
//////                          entity.setFundedContentStatus(nextRecord[14]);
//////                          entity.setDataComment(nextRecord[15]);
////                          //entity.setDate(LocalDateTime.parse(nextRecord[16], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
////                          entity.setImpressions(Double.parseDouble(nextRecord[17]));
////                          entity.setReach(Double.parseDouble(nextRecord[18]));
////                          entity.setImpressionsUnique_User(nextRecord[19]);
////                          entity.setReactionsCommentsShares(Double.parseDouble(nextRecord[20]));
////                          entity.setReactions(Double.parseDouble(nextRecord[21]));
////                          entity.setComments(Double.parseDouble(nextRecord[22]));
////                          entity.setShares(Double.parseDouble(nextRecord[23]));
////                          entity.setTotalClicks(Double.parseDouble(nextRecord[24]));
//////                          entity.setOtherClicks(Double.parseDouble(nextRecord[25]));
//////                          entity.setPhotoClick(Double.parseDouble(nextRecord[26]));
//////                          entity.setLinkClick(Double.parseDouble(nextRecord[27]));
//////                          entity.setNegativeFeedback(Double.parseDouble(nextRecord[28]));
//////                          entity.setSecondsViewed(Double.parseDouble(nextRecord[29]));
//////                          entity.setAverageSecondsViewed(Double.parseDouble(nextRecord[30]));
//////                          entity.setEstimatedEarnings(Double.parseDouble(nextRecord[31]));
//////                          entity.setAdCPM(Double.parseDouble(nextRecord[32]));
//////                          entity.setAdImpressions(Double.parseDouble(nextRecord[33]));
////	      
////	                          LoaderDTO dto = modelMapper.map(entity, LoaderDTO.class);
////	                          dto.setTimestamp(timestamp);
////	                          dto.setAccountLoader(userId);
////	                          if (dto != null) {
////	                              loaderDTORepo.save(dto);
////	                          } else {
////	                              System.out.println("DTO conversion failed");
////	                          }
////                        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
////                            e.printStackTrace();
////                        }
////                    }
////                } catch (IOException | CsvValidationException e) {
////                    e.printStackTrace();
////                }
////            }
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////    }
//
////    public void processCsvFacebookFile(MultipartFile file) {
////        try (Reader reader = new InputStreamReader(file.getInputStream());
////             CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {
////
////            String[] nextRecord;
////            while ((nextRecord = csvReader.readNext()) != null) {
////                try {
////                    Facebook entity = new Facebook();
////                    entity.setPostId(nextRecord[0]);
//////                    entity.setPageId(nextRecord[1]);
//////                    entity.setPageName(nextRecord[2]);
//////                    entity.setTitle(nextRecord[3]);
//////                    entity.setDescription(nextRecord[4]);
//////                    entity.setDurationSec(Double.parseDouble(nextRecord[5]));
////                    //entity.setPublishTime(LocalDateTime.parse(nextRecord[6], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//////                    entity.setCaptionType(nextRecord[7]);
//////                    entity.setPermalink(nextRecord[8]);
//////                    entity.setIsCrosspost(Boolean.parseBoolean(nextRecord[9]));
//////                    entity.setIsShare(Boolean.parseBoolean(nextRecord[10]));
////                    entity.setPostType(nextRecord[11]);
//////                    entity.setLanguages(nextRecord[12]);
//////                    entity.setCustomLabels(nextRecord[13]);
//////                    entity.setFundedContentStatus(nextRecord[14]);
//////                    entity.setDataComment(nextRecord[15]);
////                    //entity.setDate(LocalDateTime.parse(nextRecord[16], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
////                    entity.setImpressions(Double.parseDouble(nextRecord[17]));
////                    entity.setReach(Double.parseDouble(nextRecord[18]));
////                    entity.setImpressionsUnique_User(nextRecord[19]);
////                    entity.setReactionsCommentsShares(Double.parseDouble(nextRecord[20]));
////                    entity.setReactions(Double.parseDouble(nextRecord[21]));
////                    entity.setComments(Double.parseDouble(nextRecord[22]));
////                    entity.setShares(Double.parseDouble(nextRecord[23]));
////                    entity.setTotalClicks(Double.parseDouble(nextRecord[24]));
//////                    entity.setOtherClicks(Double.parseDouble(nextRecord[25]));
//////                    entity.setPhotoClick(Double.parseDouble(nextRecord[26]));
//////                    entity.setLinkClick(Double.parseDouble(nextRecord[27]));
//////                    entity.setNegativeFeedback(Double.parseDouble(nextRecord[28]));
//////                    entity.setSecondsViewed(Double.parseDouble(nextRecord[29]));
//////                    entity.setAverageSecondsViewed(Double.parseDouble(nextRecord[30]));
//////                    entity.setEstimatedEarnings(Double.parseDouble(nextRecord[31]));
//////                    entity.setAdCPM(Double.parseDouble(nextRecord[32]));
//////                    entity.setAdImpressions(Double.parseDouble(nextRecord[33]));
////
////                    LoaderDTO dto = modelMapper.map(entity, LoaderDTO.class);
////                    if (dto != null) {
////                        loaderDTORepo.save(dto);
////                    } else {
////                        System.out.println("DTO conversion failed");
////                    }
////                } catch (ArrayIndexOutOfBoundsException | NumberFormatException | NullPointerException e) {
////                    e.printStackTrace();
////                }
////            }
////        } catch (IOException | CsvValidationException e) {
////            e.printStackTrace();
////        }
////    }
//
//	public List<LoaderDTO> getAllFacebookFiles() {
//		return loaderDTORepo.findAll();
//	}
//
//	public int deleteAllFacebookFiles() {
//		try {
//			long count = loaderDTORepo.count();
//			loaderDTORepo.deleteAll();
//			return (int) count;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return 0;
//		}
//	}
//}
