package com.tsfn.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.tsfn.model.Facebook;
import com.tsfn.model.LoaderDTO;
import com.tsfn.repository.LoaderDTORepo;

@Service
public class FacebookService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LoaderDTORepo loaderDTORepo;

    public void processCsvFacebookFile(MultipartFile file) {
        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {

            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                try {
                    Facebook entity = new Facebook();
                    entity.setPostId(nextRecord[0]);
                    entity.setPageId(nextRecord[1]);
                    entity.setPageName(nextRecord[2]);
                    entity.setTitle(nextRecord[3]);
                    entity.setDescription(nextRecord[4]);
                    entity.setDurationSec(Double.parseDouble(nextRecord[5]));
                    //entity.setPublishTime(LocalDateTime.parse(nextRecord[6], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    entity.setCaptionType(nextRecord[7]);
                    entity.setPermalink(nextRecord[8]);
                    entity.setIsCrosspost(Boolean.parseBoolean(nextRecord[9]));
                    entity.setIsShare(Boolean.parseBoolean(nextRecord[10]));
                    entity.setPostType(nextRecord[11]);
                    entity.setLanguages(nextRecord[12]);
                    entity.setCustomLabels(nextRecord[13]);
                    entity.setFundedContentStatus(nextRecord[14]);
                    entity.setDataComment(nextRecord[15]);
                    //entity.setDate(LocalDateTime.parse(nextRecord[16], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    entity.setImpressions(Double.parseDouble(nextRecord[17]));
                    entity.setReach(Double.parseDouble(nextRecord[18]));
                    entity.setImpressionsUnique_User(nextRecord[19]);
                    entity.setReactionsCommentsShares(Double.parseDouble(nextRecord[20]));
                    entity.setReactions(Double.parseDouble(nextRecord[21]));
                    entity.setComments(Double.parseDouble(nextRecord[22]));
                    entity.setShares(Double.parseDouble(nextRecord[23]));
                    entity.setTotalClicks(Double.parseDouble(nextRecord[24]));
                    entity.setOtherClicks(Double.parseDouble(nextRecord[25]));
                    entity.setPhotoClick(Double.parseDouble(nextRecord[26]));
                    entity.setLinkClick(Double.parseDouble(nextRecord[27]));
                    entity.setNegativeFeedback(Double.parseDouble(nextRecord[28]));
                    entity.setSecondsViewed(Double.parseDouble(nextRecord[29]));
                    entity.setAverageSecondsViewed(Double.parseDouble(nextRecord[30]));
                    entity.setEstimatedEarnings(Double.parseDouble(nextRecord[31]));
                    entity.setAdCPM(Double.parseDouble(nextRecord[32]));
                    entity.setAdImpressions(Double.parseDouble(nextRecord[33]));

                    LoaderDTO dto = modelMapper.map(entity, LoaderDTO.class);
                    if (dto != null) {
                        loaderDTORepo.save(dto);
                    } else {
                        System.out.println("DTO conversion failed");
                    }
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    public List<LoaderDTO> getAllFacebookFiles() {
        return loaderDTORepo.findAll();
    }

    public int deleteAllFacebookFiles() {
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
