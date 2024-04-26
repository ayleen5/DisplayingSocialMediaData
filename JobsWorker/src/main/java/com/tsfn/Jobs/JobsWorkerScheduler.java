package com.tsfn.Jobs;
 
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tsfn.model.Metric;
import com.tsfn.repository.MetricRepository;
 
@Component
public final class JobsWorkerScheduler implements Runnable, InitializingBean, DisposableBean {

//    @Autowired
//    private MetricRepository metricRepository;
 
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
            scheduler.scheduleAtFixedRate(this, 0, 60, TimeUnit.SECONDS); // Schedule to run every 60 seconds
            running = true;
            System.out.println("ActionScheduler thread is running.");
            return true;
        }
        return false;
    }

    @Override
    public void run() {
//        List<Metric> mertics = metricRepository.findAll();
//        mertics.forEach(metric -> {
//            System.out.println("Performing action: " + metric.getName());
//        });
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
