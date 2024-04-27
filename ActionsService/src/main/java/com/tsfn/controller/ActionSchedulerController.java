package com.tsfn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsfn.Jobs.Scheduler.ActionScheduler;
import com.tsfn.service.exceptions.MerticAlreadyExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/ActionScheduler")
public class ActionSchedulerController {

    private static final Logger logger = LoggerFactory.getLogger(ActionSchedulerController.class);
    private final ActionScheduler actionScheduler;

    @Autowired
    public ActionSchedulerController(ActionScheduler actionScheduler) {
        this.actionScheduler = actionScheduler;
    }
 
	
	@PostMapping("/Start")
    public ResponseEntity<?> Start( ) {
		try {
             actionScheduler.start();
            logger.info("ActionSchedulerController.Start: ActionScheduler started successfully");
            return new ResponseEntity<>("ok", HttpStatus.OK);
        } catch (MerticAlreadyExistsException e) {
            logger.error("ActionSchedulerController.Start: Failed to start ActionScheduler:", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
 
	@PostMapping("/Stop")
    public ResponseEntity<?> Stop( ) {
		try {
            boolean isStopped = actionScheduler.stop();
            logger.info("ActionSchedulerController.Stop: ActionScheduler stopped:", isStopped);
            return new ResponseEntity<>(isStopped, HttpStatus.OK);
        } catch (MerticAlreadyExistsException e) {
            logger.error("ActionSchedulerController.Stop: Failed to stop ActionScheduler:", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
