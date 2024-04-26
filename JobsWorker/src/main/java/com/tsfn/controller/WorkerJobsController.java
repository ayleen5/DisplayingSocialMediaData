package com.tsfn.controller;
 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsfn.Jobs.JobsWorkerScheduler;
import com.tsfn.service.exceptions.MerticAlreadyExistsException;

@RestController
@RequestMapping("/ActionScheduler")
public class WorkerJobsController {

    @Autowired
    public JobsWorkerScheduler jobsWorkerScheduler;
 
	
	@PostMapping("/Start")
    public ResponseEntity<?> Start( ) {
        try {
        	jobsWorkerScheduler.start();
        	 
            return new ResponseEntity<>( "ok", HttpStatus.OK);
        } catch (MerticAlreadyExistsException e) {
        	
        	
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
 
	@PostMapping("/Stop")
    public ResponseEntity<?> Stop( ) {
        try {

        	boolean isStop =  jobsWorkerScheduler.stop();
       
            return new ResponseEntity<>(isStop, HttpStatus.OK);
        } catch (MerticAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
