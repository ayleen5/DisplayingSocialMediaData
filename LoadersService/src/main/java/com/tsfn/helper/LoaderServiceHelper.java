package com.tsfn.helper;

import java.nio.file.Paths;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
 
import org.springframework.web.client.RestTemplate;

import com.tsfn.model.FileInfo;
import com.tsfn.model.Loader;
import com.tsfn.repository.LoaderRepository;

@Component
public class LoaderServiceHelper {

	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private LoaderRepository loaderRepository;

	
	Map<String, Loader> aggregatedPosts = new HashMap<>();
	
	public LocalDateTime extractTimestampFromFileName(String filePath) {

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

	public boolean isWithinLastHour(LocalDateTime timestamp, boolean isWithTime) {
		
		// Get the current time
		if(isWithTime) {
		LocalDateTime currentTime = LocalDateTime.now();

		// Calculate the start time of the last hour
		LocalDateTime lastHourStartTime = currentTime.minusHours(1);

		// Check if the timestamp is after the start time of the last hour
		return timestamp.isAfter(lastHourStartTime) && timestamp.isBefore(currentTime);
		}
		return true;
	}

	public boolean isValidNumeric(String str) {
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

	
	public FileInfo[] getCsvFiles(String directoryPath) {
	        String csvData = restTemplate.getForObject(directoryPath, String.class);
	        return FileInfo.fromJson(csvData);
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

	 
}
