package com.tsfn.CSVservice;

/*public class ServiceCSV {

}*/


import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tsfn.model.Instagram;
import com.tsfn.repository.InstagramRepo;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Service
public class CSVInstagramFileService {

    @Autowired
    private InstagramRepo InstaFileRepo;

    public void processCsvInstagramFile(MultipartFile file) {
        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {
            
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                try {
                	Instagram entity = new Instagram();
                	
                	int saves = Integer.parseInt(nextRecord[16]);
                	int impressions = Integer.parseInt(nextRecord[11]);
                	int reach = Integer.parseInt(nextRecord[12]);
                	int likes = Integer.parseInt(nextRecord[14]);
                	int comments = Integer.parseInt(nextRecord[15]);
                	int shares = Integer.parseInt(nextRecord[13]);
                	
                	entity.setPostId(Double.parseDouble(nextRecord[0]));
                	entity.setPostType(nextRecord[8]);
                	entity.setImpressions(impressions);
                	entity.setReach(reach);
                	entity.setShares(shares);
                	entity.setLikes(likes);
                	entity.setComments(comments);
                	entity.setSaves(saves);
                	
                	
                	if (impressions > 0) {
                		entity.setCtr((double) saves / impressions);
            		} else {
            			entity.setCtr(0.0);
            		}
                	
                	if (reach > 0) {
                		entity.setEngagementrate((double) (likes + comments + shares) / reach);
            		} else {
            			entity.setEngagementrate( 0.0);
            		}
                    
                	
                	InstaFileRepo.save(entity);
                } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
                    // Log the error or handle it appropriately
                    e.printStackTrace();
                }
            }
        } catch (IOException | CsvValidationException e) {
            // Log the error or handle it appropriately
            e.printStackTrace();
        }
    }
    
    
//    
//    private String truncateDescription(String description) {
//        int maxLength = 255; // Maximum length allowed for the description column
//        if (description.length() > maxLength) {
//            return description.substring(0, maxLength);
//        }
//        return description;
//    }
    
    public List<Instagram> getAllInstagramFiles() {
	    return InstaFileRepo.findAll();
	  }

	public int deleteAllInstagramFiles() {
		// TODO Auto-generated method stub
		try {
            List<Instagram> allFiles = InstaFileRepo.findAll();
            int deletedCount = allFiles.size();
            InstaFileRepo.deleteAll();
            return deletedCount;
        } catch (Exception e) {
            // Log the error or handle it appropriately
            e.printStackTrace();
            return 0; // Return 0 if an error occurs during deletion
        }
	}


}

