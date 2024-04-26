package com.tsfn.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;

import com.tsfn.model.Action;

@Service
public class KafkaActionProducerImpl {
	 @Autowired
	    private KafkaTemplate<String, String> kafkaTemplate;

	    public void sendMessage(Action action) {
	        // Serialize Action object to JSON string
	        String actionJson = new Gson().toJson(action);
	        System.out.println("KafkaTemplat ____________KafkaTemplat");
	        kafkaTemplate.send("ActionTopic", actionJson);
	    }

//	@Autowired
//	private KafkaTemplate<String,List<Action>> kafkaTemplate;
//	
//	public void sendMessage(String message)
//	{
//		kafkaTemplate.send("ActionTopic", message);
//	}
}