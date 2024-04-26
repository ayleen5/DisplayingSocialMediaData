package com.tsfn.Jobs.Scheduler;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsfn.model.Action;
import com.tsfn.repository.ActionRepository;
import com.tsfn.service.KafkaActionProducerImpl;

@Component
public final class ActionScheduler implements Runnable, InitializingBean, DisposableBean {

    @Autowired
    private ActionRepository actionRepository;
    
    @Autowired
    private KafkaActionProducerImpl kafkaActionProducer;
    
//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
// 
    private volatile boolean running;  // Use volatile to ensure visibility across threads
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public synchronized boolean stop() {
        if (running) {
            try {
                scheduler.shutdown();
                if (!scheduler.awaitTermination(5, TimeUnit.MINUTES)) {
                    scheduler.shutdownNow();
                }
                running = false;
                System.out.println("ActionScheduler thread has stopped.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // restore interrupt status
                System.err.println("Failed to stop the scheduler gracefully.");
                return false;
            } finally {
                scheduler = null; // Help garbage collection
            }
            return true;
        }
        return false;
    }

    public synchronized boolean start() {
        if (!running) {
            if (scheduler == null || scheduler.isShutdown()) {
                scheduler = Executors.newScheduledThreadPool(1);
            }
            scheduler.scheduleAtFixedRate(this, 0, 5, TimeUnit.SECONDS); // Schedule to run every 60 seconds
            running = true;
            System.out.println("ActionScheduler thread is running.");
            return true;
        }
        return false;
    }

    @Override
    public void run() {

        List<Action> actions = actionRepository.findAll();
        for(Action action: actions) {
        	System.out.println(action.getName());
        	System.out.println("BEFORE sending a message from Action");
        	kafkaActionProducer.sendMessage(action); // Send list of actions to Kafka topic
            System.out.println("BEFORE sending a message from Action");
        }
        
        
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            String serializedActions = objectMapper.writeValueAsString(actions);
//            kafkaTemplate.send("your_topic_name", serializedActions);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
        
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!start()) {
            System.err.println("ActionScheduler thread encountered an error and is not running.");
        }
    }

    @Override
    public void destroy() throws Exception {
        if (!stop()) {
            System.err.println("ActionScheduler thread encountered an error and is not closed.");
        }
    }
}
