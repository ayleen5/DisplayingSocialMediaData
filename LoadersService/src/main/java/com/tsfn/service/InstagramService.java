package com.tsfn.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.tsfn.model.Instagram;
import com.tsfn.model.LoaderDTO;
import com.tsfn.repository.LoaderDTORepo;

@Service
public class InstagramService {
    
	@Autowired
    private  ModelMapper modelMapper;
    
    @Autowired
    private LoaderDTORepo loaderDTORepo;

 
	    public void processCsvInstagramFile(MultipartFile file) {
	        try (Reader reader = new InputStreamReader(file.getInputStream());
	             CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {

	            String[] nextRecord;
	            while ((nextRecord = csvReader.readNext()) != null) {
	                try {
	                    Instagram entity = new Instagram();
	                    entity.setPostId(nextRecord[0]);
	                    entity.setAccountId(nextRecord[1]);
	                    entity.setAccountUsername(nextRecord[2]);
	                    entity.setAccountName(nextRecord[3]);
	                    entity.setDescription(nextRecord[4]);
	                    entity.setDurationSec(Double.parseDouble(nextRecord[5]));
	                    //entity.setPublishTime(LocalDateTime.parse(nextRecord[6]));
	                    entity.setPermalink(nextRecord[7]);
	                    entity.setPostType(nextRecord[8]);
	                    entity.setDataComment(nextRecord[9]);
	                    entity.setDate(nextRecord[10]);
	                    entity.setImpressions(Double.parseDouble(nextRecord[11]));
	                    entity.setReach(Double.parseDouble(nextRecord[12]));
	                    entity.setShares(Double.parseDouble(nextRecord[13]));
	                    entity.setFollows(Double.parseDouble(nextRecord[14]));
	                    entity.setLikes(Double.parseDouble(nextRecord[15]));
	                    entity.setComments(Double.parseDouble(nextRecord[16]));
	                    entity.setSaves(Double.parseDouble(nextRecord[17]));
	                    entity.setPlays(Double.parseDouble(nextRecord[18]));
	                    LoaderDTO dto =  modelMapper.map(entity, LoaderDTO.class);
	                    if (dto != null) {
	                        loaderDTORepo.save(dto);
	                    } else {
	                        System.out.println("DTO conversion failed");
	                    }
	                    loaderDTORepo.save(dto); // Save the DTO directly, if LoaderDTO is an entity
	                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
	                    e.printStackTrace();
	                }
	            }
	        } catch (IOException | CsvValidationException e) {
	            e.printStackTrace();
	        }
	    }

	    public List<LoaderDTO> getAllInstagramFiles() {
	        return loaderDTORepo.findAll();
	    }

	    public int deleteAllInstagramFiles() {
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

