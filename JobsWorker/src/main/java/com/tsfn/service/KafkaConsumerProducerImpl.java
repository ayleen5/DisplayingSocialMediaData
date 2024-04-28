package com.tsfn.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tsfn.controller.client.Action.Action;
import com.tsfn.controller.client.Loader.ClientLoaderController;
import com.tsfn.controller.client.Loader.Loader;
import com.tsfn.model.Metric;
import com.tsfn.repository.MetricRepository;

@Service
public class KafkaConsumerProducerImpl {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	ClientLoaderController clientLoader;

	@Autowired
	MetricRepository metricRepository;
	@Autowired
	MetricService meService;

	String informationMesages = ""; // Via_To_MSG

	@KafkaListener(topics = "ActionTopic", groupId = "groupId")
	public void listen(String actionJson) {
		Action action = new Gson().fromJson(actionJson, Action.class);
//		System.out.println("listen KafkaConsumerProducerImpl" + action.getName());
		Hashtable<String, ArrayList<String>> conditions;
		String accountId;
//

//		 Perform your logic on the list of actions
		// Clear the informationMesages list before processing new messages
		informationMesages = "";
		try {

			if (action != null) {
				
				conditions = action.getConditions();
				accountId = action.getAccount_id();
				
				if (CheckPosts(conditions, accountId)) {
					
					// Via_To_MSG
					informationMesages = action.getAction_type().name() + "_" + action.getRecipient() + "_"
							+ action.getNotification_text();
				}

			}

			System.out.println("BEFORE sending a message from KafkaConsumerProducerImpl");
			sendMessage(informationMesages);
			System.out.println("AFTER sending a message from KafkaConsumerProducerImpl");
		} catch (Exception e) {
			System.err.println("An error occurred during message processing: " + e.getMessage());
		}

//		System.out.println("message recieved :"+ message+" from ActionTopic !!");
	}

	public boolean CheckPosts(Hashtable<String, ArrayList<String>> conditions, String accountId) {
		// get all files for the accoundId
		
		System.out.println("In the ifff AFTER NULLLLL" + accountId);

		List<Loader> posts = clientLoader.getAllFilesByAccountLoader(accountId);
//		System.out.println("AFTER get all posts");

		// check condition
		boolean flag = false;

		for (Loader post : posts) {
//			System.out.println(" -----the post NAME  :::: " + post.getContentType());
			flag = (flag || CheckConditionForPosts(conditions, post));
		}
		System.out.println("FLAAAGGGG :::: " + flag);
		return flag;

	}

	@Transactional(readOnly = true)
	public boolean CheckConditionForPosts(Hashtable<String, ArrayList<String>> conditions, Loader post) {

		boolean clause_result = true, metric_flag = false, cond_result = false;

		for (String clause : conditions.keySet()) {

			ArrayList<String> metricsInfo = conditions.get(clause);

			clause_result = true; // and
//			System.err.println("hhhh1");

			for (String metricId : metricsInfo) {
//				System.err.println("hhhh1"+metricId);
				
				int id = Integer.parseInt(metricId);
//				System.err.println(id);
				
				Metric metricB = meService.getMetricById(id);
				
//				System.err.println("mmmm"+metricB);
				String metric = metricB.getMetric();
//				System.err.println(metric);
				int threshold = metricB.getThreshold();
				int time_frame_hours = metricB.getTime_frame_hours();
//				System.out.println("Deeeeebuuuugggg");
				metric_flag = true;
				// Impressions
				if (metric.equals("Impressions")) {

					if (!(post.getImpressions() >= threshold
							&& isWithinTimeFrame(post.getTimestamp(), time_frame_hours))) {
						metric_flag = false;
						clause_result = false;
						break;
					}
				}
				// Views
				else if (metric.equals("Views")) {
					// function that get all the files with the Views >= Threshold
					if (!(post.getViews() >= threshold && isWithinTimeFrame(post.getTimestamp(), time_frame_hours))) {
						metric_flag = false;
						clause_result = false;
						break;
					}
				}
				// Clicks
				else if (metric.equals("Clicks")) {
					if (!(post.getClicks() >= threshold && isWithinTimeFrame(post.getTimestamp(), time_frame_hours))) {
						metric_flag = false;
						clause_result = false;
						break;
					}
				}
				// Likes
				else if (metric.equals("Likes")) {
					if (!(post.getLikes() >= threshold && isWithinTimeFrame(post.getTimestamp(), time_frame_hours))) {
						metric_flag = false;
						clause_result = false;
						break;
					}
				}
				// Comments
				else if (metric.equals("Comments")) {
					if (!(post.getComments() >= threshold
							&& isWithinTimeFrame(post.getTimestamp(), time_frame_hours))) {
						metric_flag = false;
						clause_result = false;
						break;
					}
				}
				// Shares
				else if (metric.equals("Shares")) {
					if (!(post.getShares() >= threshold && isWithinTimeFrame(post.getTimestamp(), time_frame_hours))) {
						metric_flag = false;
						clause_result = false;
						break;
					}
				}
				clause_result = (clause_result && metric_flag); // and
			}
			cond_result = (cond_result || clause_result);
		}
		return cond_result;
	}

	// Function to check if a timestamp falls within the last time_frame_hours
	private boolean isWithinTimeFrame(LocalDateTime timestamp, int time_frame_hours) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime lowerBound = now.minusHours(time_frame_hours);
		return timestamp.isAfter(lowerBound) && timestamp.isBefore(now);
	}

	public void sendMessage(String message) {
		
		kafkaTemplate.send("NotificationTopic", message);
	}

}