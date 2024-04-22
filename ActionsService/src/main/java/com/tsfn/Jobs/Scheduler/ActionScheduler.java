package com.tsfn.Jobs.Scheduler;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tsfn.model.Action;
import com.tsfn.repository.ActionRepository;

 

@Component
public final class ActionScheduler implements Runnable, InitializingBean, DisposableBean {

    @Autowired
    private ActionRepository actionRepository;
 
    private boolean running;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public boolean start() {
        if (!running) {
            running = true;
            scheduler.scheduleAtFixedRate(this, 0, 60, TimeUnit.SECONDS); // Schedule to run every 60 seconds
            return true;
        }
        return false;
    }

    public boolean stop() {
        if (running) {
            try {
                scheduler.shutdown();
                if (!scheduler.awaitTermination(5, TimeUnit.MINUTES)) {
                    return false;
                }
                running = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public void run() {
        // Implement your task here
        List<Action> actions = actionRepository.findAll();
        actions.forEach(action -> {
            // Implement your action logic here
            System.out.println("Performing action: " + action.getName());
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (start()) {
            System.out.println("ActionScheduler thread is running.");
            return;
        }
        System.err.println("ActionScheduler thread encountered an error and is not running.");
    }

    @Override
    public void destroy() throws Exception {
        if (stop()) {
            System.out.println("ActionScheduler thread is closed.");
            return;
        }
        System.err.println("ActionScheduler thread encountered an error and is not closed.");
    }
}

