//package com.tsfn.service;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.Reader;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
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
//import com.tsfn.model.Instagram;
//import com.tsfn.model.LoaderDTO;
//import com.tsfn.repository.LoaderDTORepo;
//
//@Service
//public class InstagramService {
//
//	@Autowired
//	private ModelMapper modelMapper;
//
//	@Autowired
//	private LoaderDTORepo loaderDTORepo;
//
//	Map<String, LoaderDTO> aggregatedPosts = new HashMap<>();
//
//	public void processCsvInstagramFile(String directoryPath) {
//
//		try {
//			List<String> instagramCsvFiles = Files.list(Paths.get(directoryPath))
//					.filter(path -> path.getFileName().toString().contains("instagram")
//							&& path.getFileName().toString().endsWith(".csv"))
//					.map(path -> path.toAbsolutePath().toString()).collect(Collectors.toList());
//
//			for (String filePath : instagramCsvFiles) {
//
//				LocalDateTime fileTimestamp = extractTimestampFromFileName(filePath);
//				// the user id
//				String filename = Paths.get(filePath).getFileName().toString();
//				String userId = filename.split("_")[0];
//
//				// LoaderDTO existingPost =
//				// loaderDTORepo.findByAccountLoaderAndTimestamp(userId, fileTimestamp);
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
//								// LoaderDTO existingPost =
//								// loaderDTORepo.findByAccountLoaderAndTimestampAndPostId(userId, fileTimestamp,
//								// nextRecord[0]);
//								if (!nextRecord[0].isBlank()) {
//									LoaderDTO existingPost = loaderDTORepo.findByAccountLoaderAndTimestampAndPostId(
//											userId, fileTimestamp, nextRecord[0]);
//
//									if (existingPost != null)
//										break;
//
//									System.out.println("InstagramService Files Instagram   Instagram  Instagram:");
//
//									double impressions = isValidNumeric(nextRecord[11])
//											? Double.parseDouble(nextRecord[11])
//											: 0.0;
//									double saves = isValidNumeric(nextRecord[17]) ? Double.parseDouble(nextRecord[17])
//											: 0.0;
//									double reach = isValidNumeric(nextRecord[12]) ? Double.parseDouble(nextRecord[12])
//											: 0.0;
//									double likes = isValidNumeric(nextRecord[15]) ? Double.parseDouble(nextRecord[15])
//											: 0.0;
//
//									post.setPostId(nextRecord[0]);
//									post.setContentType(nextRecord[8]);
//									post.setImpressions(impressions);
//									post.setViews(reach); // reach
//									post.setClicks(saves); // saves
//									post.setLikes(likes);
//									post.setComments(
//											isValidNumeric(nextRecord[16]) ? Double.parseDouble(nextRecord[16]) : 0.0);
//									post.setShares(
//											isValidNumeric(nextRecord[13]) ? Double.parseDouble(nextRecord[13]) : 0.0);
//
//									if (impressions > 0 && saves > 0) {
//										post.setCTR(saves / impressions);
//									} else {
//										post.setCTR(0);
//									}
//									if (reach > 0) {
//										post.setEngagementrate(likes / (double) reach);
//									} else {
//										post.setEngagementrate(0);
//									}
//
//									post.setTimestamp(fileTimestamp);
//									post.setAccountLoader(userId);
//									Aggrigation(post);
//
//								}
//
//							} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
//								e.printStackTrace();
//							}
//						}
//					} catch (IOException | CsvValidationException e) {
//						e.printStackTrace();
//					}
//				}
//
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
////	public void processCsvInstagramFile(String filePath) {
////
////		try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
////				CSVReader csvReader = new CSVReader(reader)) {
////
////			String[] nextRecord;
////			while ((nextRecord = csvReader.readNext()) != null) {
////				try {
////					System.out.println("InstagramService Files Instagram   Instagram  Instagram:");
////					Instagram entity = new Instagram();
////					
////					entity.setPostId(nextRecord[0]);
////					entity.setAccountId(nextRecord[1]);
////					entity.setAccountUsername(nextRecord[2]);
////					entity.setAccountName(nextRecord[3]);
////					entity.setDescription(nextRecord[4]);
////					entity.setDurationSec(Double.parseDouble(nextRecord[5]));
////					// entity.setPublishTime(LocalDateTime.parse(nextRecord[6]));
////					entity.setPermalink(nextRecord[7]);
////					entity.setPostType(nextRecord[8]);
////					entity.setDataComment(nextRecord[9]);
////					entity.setDate(nextRecord[10]);
////					entity.setImpressions(Double.parseDouble(nextRecord[11]));
////					entity.setReach(Double.parseDouble(nextRecord[12]));
////					entity.setShares(Double.parseDouble(nextRecord[13]));
////					entity.setFollows(Double.parseDouble(nextRecord[14]));
////					entity.setLikes(Double.parseDouble(nextRecord[15]));
////					entity.setComments(Double.parseDouble(nextRecord[16]));
////					entity.setSaves(Double.parseDouble(nextRecord[17]));
////					entity.setPlays(Double.parseDouble(nextRecord[18]));
////
////					LoaderDTO dto = modelMapper.map(entity, LoaderDTO.class);
////					
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
////	public void processCsvInstagramFile(String directoryPath) {
////		try {
////			List<String> instagramCsvFiles = Files.list(Paths.get(directoryPath))
////					.filter(path -> path.getFileName().toString().contains("instagram")
////							&& path.getFileName().toString().endsWith(".csv"))
////					.map(path -> path.toAbsolutePath().toString()).collect(Collectors.toList());
////
////			for (String filePath : instagramCsvFiles) {
////
////				String filename = Paths.get(filePath).getFileName().toString();
////				String[] parts = filename.split("_|T");
////				String year = parts[2];
////				String month = parts[3];
////				String day = parts[4];
////				String hour = parts[5];
////				String minute = parts[6];
////				String second = parts[7].split("\\.")[0]; // Remove the file extension
////
////				// Construct LocalDateTime object from extracted parts
////				LocalDateTime timestamp = LocalDateTime.of(Integer.parseInt(year), Integer.parseInt(month),
////						Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute),
////						Integer.parseInt(second));
////				// the user id
////				String userId = filename.split("_")[0];
////
////				try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
////						CSVReader csvReader = new CSVReader(reader)) {
////
////					String[] nextRecord;
////					while ((nextRecord = csvReader.readNext()) != null) {
////						try {
////							Instagram entity = new Instagram();
////							System.out.println("InstagramService Files Instagram   Instagram  Instagram:");
////							entity.setPostId(nextRecord[0]);
////							entity.setAccountId(nextRecord[1]);
////							entity.setAccountUsername(nextRecord[2]);
////							entity.setAccountName(nextRecord[3]);
////							entity.setDescription(nextRecord[4]);
////							entity.setDurationSec(Double.parseDouble(nextRecord[5]));
////							// entity.setPublishTime(LocalDateTime.parse(nextRecord[6]));
////							entity.setPermalink(nextRecord[7]);
////							entity.setPostType(nextRecord[8]);
////							entity.setDataComment(nextRecord[9]);
////							entity.setDate(nextRecord[10]);
////							entity.setImpressions(Double.parseDouble(nextRecord[11]));
////							entity.setReach(Double.parseDouble(nextRecord[12]));
////							entity.setShares(Double.parseDouble(nextRecord[13]));
////							entity.setFollows(Double.parseDouble(nextRecord[14]));
////							entity.setLikes(Double.parseDouble(nextRecord[15]));
////							entity.setComments(Double.parseDouble(nextRecord[16]));
////							entity.setSaves(Double.parseDouble(nextRecord[17]));
////							entity.setPlays(Double.parseDouble(nextRecord[18]));
////
////							LoaderDTO dto = modelMapper.map(entity, LoaderDTO.class);
////
////							dto.setTimestamp(timestamp);
////							dto.setAccountLoader(userId);
////
////							if (dto != null) {
////								loaderDTORepo.save(dto);
////							} else {
////								System.out.println("DTO conversion failed");
////							}
////						} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
////							e.printStackTrace();
////						}
////					}
////				} catch (IOException | CsvValidationException e) {
////					e.printStackTrace();
////				}
////			}
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
////	}
//
////    public void processCsvInstagramFile(String filePath) throws CsvValidationException {
////        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
////             CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {
////
////            String[] nextRecord;
////            while ((nextRecord = csvReader.readNext()) != null) {
////                try {
////                    Instagram entity = new Instagram();
////                    
////                    entity.setPostId(nextRecord[0]);
////                    entity.setAccountId(nextRecord[1]);
////                    entity.setAccountUsername(nextRecord[2]);
////                    entity.setAccountName(nextRecord[3]);
////                    entity.setDescription(nextRecord[4]);
////                    entity.setDurationSec(Double.parseDouble(nextRecord[5]));
////                    //entity.setPublishTime(LocalDateTime.parse(nextRecord[6]));
////                    entity.setPermalink(nextRecord[7]);
////                    entity.setPostType(nextRecord[8]);
////                    entity.setDataComment(nextRecord[9]);
////                    entity.setDate(nextRecord[10]);
////                    entity.setImpressions(Double.parseDouble(nextRecord[11]));
////                    entity.setReach(Double.parseDouble(nextRecord[12]));
////                    entity.setShares(Double.parseDouble(nextRecord[13]));
////                    entity.setFollows(Double.parseDouble(nextRecord[14]));
////                    entity.setLikes(Double.parseDouble(nextRecord[15]));
////                    entity.setComments(Double.parseDouble(nextRecord[16]));
////                    entity.setSaves(Double.parseDouble(nextRecord[17]));
////                    entity.setPlays(Double.parseDouble(nextRecord[18]));
////
////                    LoaderDTO dto = modelMapper.map(entity, LoaderDTO.class);
////                    if (dto != null) {
////                        loaderDTORepo.save(dto);
////                    } else {
////                        System.out.println("DTO conversion failed");
////                    }
////                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
////                    e.printStackTrace();
////                }
////            }
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////    }
//
//	public List<LoaderDTO> getAllInstagramFiles() {
//		return loaderDTORepo.findAll();
//	}
//
//	public int deleteAllInstagramFiles() {
//		try {
//			long count = loaderDTORepo.count();
//			loaderDTORepo.deleteAll();
//			return (int) count;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return 0;
//		}
//	}
//
//	public List<LoaderDTO> findAllByAccountLoader(String accountLoader) {
//		return loaderDTORepo.findByAccountLoader(accountLoader);
//	}
//
//	public List<LoaderDTO> getFilesWithImpressionGreaterThanThreshold(int threshold) {
//		return loaderDTORepo.findByImpressionsGreaterThanEqual(threshold);
//	}
//}
