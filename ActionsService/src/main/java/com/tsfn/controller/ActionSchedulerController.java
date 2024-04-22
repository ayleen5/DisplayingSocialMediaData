package com.tsfn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsfn.Jobs.Scheduler.ActionScheduler;
import com.tsfn.model.Metric;
import com.tsfn.service.MetricService;
import com.tsfn.service.exceptions.MerticAlreadyExistsException;

@RestController
@RequestMapping("/ActionScheduler")
public class ActionSchedulerController {

	@Autowired
    private ActionScheduler actionScheduler;
	
	
	@PostMapping("/Run")
    public ResponseEntity<?> createMetric(@RequestBody Metric metric) {
        try {
        	actionScheduler.run();
            return new ResponseEntity<>("run", HttpStatus.CREATED);
        } catch (MerticAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
 
}
