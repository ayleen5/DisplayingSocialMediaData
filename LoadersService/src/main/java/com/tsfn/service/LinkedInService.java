package com.tsfn.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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

	public void processCsvLinkedInFile(String directoryPath) {
		try {
			List<String> linkedinCsvFiles = Files.list(Paths.get(directoryPath))
					.filter(path -> path.getFileName().toString().contains("linkedin")
							&& path.getFileName().toString().endsWith(".csv"))
					.map(path -> path.toAbsolutePath().toString()).collect(Collectors.toList());

			for (String filePath : linkedinCsvFiles) {
				try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
						CSVReader csvReader = new CSVReader(reader)) {

					String[] nextRecord;
					while ((nextRecord = csvReader.readNext()) != null) {
						try {
							LinkedIn entity = new LinkedIn();
							entity.setPostTitle(nextRecord[0]);
							entity.setPostLink(nextRecord[1]);
							entity.setPostType(nextRecord[2]);
							entity.setCampaignName(nextRecord[3]);
							entity.setPostedBy(nextRecord[4]);
							entity.setCreatedDate(parseDate(nextRecord[5]));
							entity.setCampaignStartDate(parseDate(nextRecord[6]));
							entity.setCampaignEndDate(parseDate(nextRecord[7]));
							entity.setAudience(nextRecord[8]);
							entity.setImpressions(Double.parseDouble(nextRecord[9]));
//  	                    entity.setVideoViews(Double.parseDouble(nextRecord[10]));
//  	                    entity.setOffsiteViews(Double.parseDouble(nextRecord[11]));
							entity.setClicks(Double.parseDouble(nextRecord[12]));
							entity.setCTR(Double.parseDouble(nextRecord[13]));
							entity.setLikes(Double.parseDouble(nextRecord[14]));
							entity.setComments(Double.parseDouble(nextRecord[15]));
							entity.setReposts(Double.parseDouble(nextRecord[16]));
							entity.setFollows(Double.parseDouble(nextRecord[17]));
							entity.setEngagementRate(Double.parseDouble(nextRecord[18]));
							entity.setContentType(nextRecord[19]);

							LoaderDTO dto = modelMapper.map(entity, LoaderDTO.class);
							if (dto != null) {
								loaderDTORepo.save(dto);
							} else {
								System.out.println("DTO conversion failed");
							}
						} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
							e.printStackTrace();
						}
					}
				} catch (IOException | CsvValidationException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
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
