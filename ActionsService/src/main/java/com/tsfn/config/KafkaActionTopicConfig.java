package com.tsfn.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaActionTopicConfig {
	
	@Bean
	public NewTopic tsofenTopic()
	{
		return TopicBuilder.name("ActionTopic").build();
	}

}