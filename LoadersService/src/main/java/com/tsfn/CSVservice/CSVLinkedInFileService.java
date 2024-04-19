package com.tsfn.CSVservice;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.tsfn.model.LinkedIn;
import com.tsfn.repository.LinkedInRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Service
public class CSVLinkedInFileService {

    @Autowired
    private LinkedInRepo linkedInFileRepository;

    public void processCsv(MultipartFile file) {
        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {

            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                try {
                	int vEO_oV = Integer.parseInt(nextRecord[10]) + Integer.parseInt(nextRecord[11]);
                    LinkedIn entity = new LinkedIn();
                    entity.setPostLink(truncatePostTitle(nextRecord[1]));
                    entity.setContentType(nextRecord[19]);
                    entity.setImpressions(Integer.parseInt(nextRecord[9]));
                    entity.setVEO_oV(vEO_oV);
                    entity.setClicks(Integer.parseInt(nextRecord[12]));
                    entity.setClickThroughRate(Double.parseDouble(nextRecord[13]));
                    entity.setLikes(Integer.parseInt(nextRecord[14]));
                    entity.setComments(Integer.parseInt(nextRecord[15]));
                    entity.setReposts(Integer.parseInt(nextRecord[16]));
                    entity.setEngagementRate(Double.parseDouble(nextRecord[18]));

                    linkedInFileRepository.save(entity);
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    // Log the error or handle it appropriately
                    e.printStackTrace();
                }
            }
        } catch (IOException | CsvValidationException e) {
            // Log the error or handle it appropriately
            e.printStackTrace();
        }
    }

    public List<LinkedIn> getAllLinkedInFiles() {
        return linkedInFileRepository.findAll();
    }
    
    private String truncatePostTitle(String PostTitle) {
        int maxLength = 255; // Maximum length allowed for the description column
        if (PostTitle.length() > maxLength) {
            return PostTitle.substring(0, maxLength);
        }
        return PostTitle;
    }
    public int deleteAllLinkedInFiles() {
    	// TODO Auto-generated method stub
		try {
            List<LinkedIn> allFiles = linkedInFileRepository.findAll();
            int deletedCount = allFiles.size();
            linkedInFileRepository.deleteAll();
            return deletedCount;
        } catch (Exception e) {
            // Log the error or handle it appropriately
            e.printStackTrace();
            return 0; // Return 0 if an error occurs during deletion
        }
    }
}