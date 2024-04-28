package com.tsfn.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tsfn.controller.client.Action.Action;

@Configuration
public class KafkaActionConsumerConfig {

//	@Value("localhost:9092")
	@Value("${spring.kafka.consumer.bootstrap-servers}")
	private String bootstrapServers ;
	
	
	//config consumer and fit it properties 
	public Map<String ,Object> consumerConfig()
	{
		Map<String ,Object> props= new HashMap<String, Object>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG , bootstrapServers);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return props;
	}
	
	
	@Bean
	public ConsumerFactory<String,String> consumerFactory()
	{
		return new DefaultKafkaConsumerFactory<>(consumerConfig());
	}
	//listener that listen to all topics
	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> factory(ConsumerFactory<String,String> consumerFactory)
	{
		ConcurrentKafkaListenerContainerFactory<String, String> factory 
				= new ConcurrentKafkaListenerContainerFactory<String, String>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

//	public Map<String, Object> consumerConfig() {
//		Map<String, Object> props = new HashMap<String, Object>();
//		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
//		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
//		return props;
//	}
//
//	@Bean
//	public ConsumerFactory<String, String> consumerFactory() {
//		return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(),
//				new ErrorHandlingDeserializer<>(new JsonDeserializer<>(new TypeReference<String>() {
//				})));
//	}
//
////	@Bean
////	public ConsumerFactory<String,List<Action>> consumerFactory()
////	{
////		return new DefaultKafkaConsumerFactory<>(consumerConfig());
////	}
//	// listener that listen to all topics
//	@Bean
//	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> factory(
//			ConsumerFactory<String, String> consumerFactory) {
//		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//		factory.setConsumerFactory(consumerFactory());
//		return factory;
//	}

//	 config consumer and fit it properties
//	public Map<String, Object> consumerConfig() {
//		Map<String, Object> props = new HashMap<String, Object>();
//		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
//		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
//		return props;
//	}
//
//	@Bean
//	public ConsumerFactory<String, List<Action>> consumerFactory() {
//		return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(),
//				new ErrorHandlingDeserializer<>(new JsonDeserializer<>(new TypeReference<List<Action>>() {
//				})));
//	}
//
////	@Bean
////	public ConsumerFactory<String,List<Action>> consumerFactory()
////	{
////		return new DefaultKafkaConsumerFactory<>(consumerConfig());
////	}
//	// listener that listen to all topics
//	@Bean
//	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, List<Action>>> factory(
//			ConsumerFactory<String, List<Action>> consumerFactory) {
//		ConcurrentKafkaListenerContainerFactory<String, List<Action>> factory = new ConcurrentKafkaListenerContainerFactory<>();
//		factory.setConsumerFactory(consumerFactory());
//		return factory;
//	}

}