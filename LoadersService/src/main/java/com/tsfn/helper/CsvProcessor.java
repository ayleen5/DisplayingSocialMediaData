package com.tsfn.helper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.opencsv.CSVReader;
import com.tsfn.model.FileInfo;
import com.tsfn.repository.LoaderRepository;
 
import com.opencsv.exceptions.CsvValidationException;

import com.tsfn.model.Loader;
 
 
@Component
public class CsvProcessor {

	@Autowired
    private LoaderRepository loaderRepository;
	@Autowired
    private LoaderServiceHelper loaderServiceHelper;
	

    public void processCsvFile(String directoryPath, FileType fileType,  boolean isWithTime) {
        try {
            FileInfo[] csvRows = loaderServiceHelper.getCsvFiles(directoryPath);

            for (FileInfo fileInfo : csvRows) {
                LocalDateTime fileTimestamp = loaderServiceHelper.extractTimestampFromFileName(fileInfo.getPath());
                String filename = Paths.get(fileInfo.getName()).getFileName().toString();
                String accountId = filename.split("_")[0];

                if (loaderServiceHelper.isWithinLastHour(fileTimestamp, isWithTime)) {
                    processCsvRow(fileInfo, fileTimestamp, accountId, fileType);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processCsvRow(FileInfo fileInfo, LocalDateTime fileTimestamp, String accountId, FileType fileType) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(fileInfo.getDownloadUrl()).openStream()));
             CSVReader csvReader = new CSVReader(reader)) {

            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                try {
                    Loader post = new Loader();
                    if (!nextRecord[0].isBlank()) {
                        Loader existingPost = loaderRepository.findByAccountLoaderAndTimestampAndPostId(accountId, fileTimestamp, nextRecord[0]);

                        if (existingPost != null)
                            break;

                        populateLoaderFields(post, nextRecord, fileType, fileTimestamp, accountId);
                        post.setTimestamp(fileTimestamp);
                        post.setAccountLoader(accountId);
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

    private void populateLoaderFields(Loader post, String[] nextRecord, FileType fileType, LocalDateTime fileTimestamp, String accountId) {
        // Populate loader fields based on file type
        switch (fileType) {
            case INSTAGRAM:
                populateInstagramFields(post, nextRecord);
                break;
            case LINKEDIN:
                populateLinkedInFields(post, nextRecord, fileTimestamp, accountId);
                break;
            case FACEBOOK:
                populateFacebookFields(post, nextRecord,fileTimestamp, accountId);
                break;
            default:
                // Handle unsupported file types
                break;
        }
    }

    private void populateInstagramFields(Loader post, String[] nextRecord) {
        double impressions = loaderServiceHelper.isValidNumeric(nextRecord[11]) ? Double.parseDouble(nextRecord[11]) : 0.0;
        double saves = loaderServiceHelper.isValidNumeric(nextRecord[17]) ? Double.parseDouble(nextRecord[17]) : 0.0;
        double reach = loaderServiceHelper.isValidNumeric(nextRecord[12]) ? Double.parseDouble(nextRecord[12]) : 0.0;
        double likes = loaderServiceHelper.isValidNumeric(nextRecord[15]) ? Double.parseDouble(nextRecord[15]) : 0.0;

        post.setPostId(nextRecord[0]);
        post.setContentType(nextRecord[8]);
        post.setImpressions(impressions);
        post.setViews(reach);
        post.setClicks(saves);
        post.setLikes(likes);
        post.setComments(loaderServiceHelper.isValidNumeric(nextRecord[16]) ? Double.parseDouble(nextRecord[16]) : 0.0);
        post.setShares(loaderServiceHelper.isValidNumeric(nextRecord[13]) ? Double.parseDouble(nextRecord[13]) : 0.0);

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
    }

    private void populateLinkedInFields(Loader post, String[] nextRecord,LocalDateTime fileTimestamp, String accountId) {
        double impressions = loaderServiceHelper.isValidNumeric(nextRecord[9]) ? Double.parseDouble(nextRecord[9]) : 0.0;
        double clicks = loaderServiceHelper.isValidNumeric(nextRecord[12]) ? Double.parseDouble(nextRecord[12]) : 0.0;
        double likes = loaderServiceHelper.isValidNumeric(nextRecord[14]) ? Double.parseDouble(nextRecord[14]) : 0.0;
        double comments = loaderServiceHelper.isValidNumeric(nextRecord[15]) ? Double.parseDouble(nextRecord[15]) : 0.0;
        double reports = loaderServiceHelper.isValidNumeric(nextRecord[16]) ? Double.parseDouble(nextRecord[16]) : 0.0;
        double CTR = loaderServiceHelper.isValidNumeric(nextRecord[13]) ? Double.parseDouble(nextRecord[13]) : 0.0;
        double Engagementrate = loaderServiceHelper.isValidNumeric(nextRecord[18]) ? Double.parseDouble(nextRecord[18]) : 0.0;
        double offsiteVideoViews = loaderServiceHelper.isValidNumeric(nextRecord[11]) ? Double.parseDouble(nextRecord[11]) : 0.0;
        double offsiteViews = loaderServiceHelper.isValidNumeric(nextRecord[10]) ? Double.parseDouble(nextRecord[10]) : 0.0;
        double Views = offsiteVideoViews + offsiteViews; // (Excluding offsite video views)] + [Offsite Views]

        post.setPostId(nextRecord[1]);// postLink
        post.setContentType(nextRecord[19]);
        post.setImpressions(impressions);
        post.setViews(Views); // to fix later
        post.setClicks(clicks);
        post.setLikes(likes);
        post.setComments(comments);
        post.setShares(reports);
        post.setTimestamp(fileTimestamp);
        post.setAccountLoader(accountId);
        post.setCTR(CTR);
        post.setEngagementrate(Engagementrate);
        loaderServiceHelper.Aggrigation(post);
    }

    private void populateFacebookFields(Loader post, String[] nextRecord, LocalDateTime fileTimestamp, String accountId) {
        double impressions = loaderServiceHelper.isValidNumeric(nextRecord[17]) ? Double.parseDouble(nextRecord[17]) : 0.0;
        double reach = loaderServiceHelper.isValidNumeric(nextRecord[18]) ? Double.parseDouble(nextRecord[18]) : 0.0;
        double totalClicks = loaderServiceHelper.isValidNumeric(nextRecord[24]) ? Double.parseDouble(nextRecord[24]) : 0.0;
        double ReactionsCommentsShares = loaderServiceHelper.isValidNumeric(nextRecord[20]) ? Double.parseDouble(nextRecord[20]) : 0.0;
        double reactions = loaderServiceHelper.isValidNumeric(nextRecord[21]) ? Double.parseDouble(nextRecord[21]) : 0.0;
        double comments = loaderServiceHelper.isValidNumeric(nextRecord[22]) ? Double.parseDouble(nextRecord[22]) : 0.0;
        double shares = loaderServiceHelper.isValidNumeric(nextRecord[23]) ? Double.parseDouble(nextRecord[23]) : 0.0;

        post.setPostId(nextRecord[0]);
        post.setContentType(nextRecord[11]);
        post.setImpressions(impressions);
        post.setViews(reach);
        post.setClicks(totalClicks);
        post.setLikes(reactions);
        post.setComments(comments);
        post.setShares(shares);
        post.setTimestamp(fileTimestamp);
        post.setAccountLoader(accountId);

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


}
