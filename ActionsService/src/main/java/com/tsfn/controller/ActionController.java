package com.tsfn.controller;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tsfn.controller.client.security.SecurityClient;
import com.tsfn.controller.client.security.TokenRoleRequest;
import com.tsfn.controller.client.security.VerifyTokenAndCheckRolesResponse;
import com.tsfn.model.Action;
import com.tsfn.service.ActionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/actions")
public class ActionController {
	
	
	@Autowired
    private  ActionService actionService;
	
	@Autowired
	private  SecurityClient securityCleint;

	private static final Logger logger = LoggerFactory.getLogger(ActionController.class);
    

    @PostMapping("/create")
    public ResponseEntity<Action> createAction(@RequestBody Action action, @RequestHeader("Authorization") String token) {
    	try {
    		VerifyTokenAndCheckRolesResponse verifyTokenAndCheckRolesResponse = securityCleint.verifyTokenAndCheckRoles(new TokenRoleRequest());
    		if(verifyTokenAndCheckRolesResponse.isVerifyTokenAndCheckRoles()) {
    		actionService.save(action);
    		logger.info("ActionController.createAction: Success creating action with name:", action.getName());
    		return new ResponseEntity<>(action, HttpStatus.CREATED);
    		} 
    		logger.info("ActionController.createAction: Faild You dont have paramition");
    		return new ResponseEntity<>(null, HttpStatus.CREATED);
    		
    	} catch (Exception e){
    		logger.error("ActionController.createAction: Error creating action with name:"+ action.getName(), e.getMessage());
    		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    	}
    }

    @GetMapping("/get{id}")
    public ResponseEntity<Action> getActionById(@RequestParam int id) {
    	try {
            Optional<Action> result = actionService.getById(id);
            return result.map(action -> {
                logger.info("ActionController.getActionById: Found action with ID:" + action.getName());
                return new ResponseEntity<>(action, HttpStatus.OK);
            }).orElseGet(() -> {
                logger.warn("ActionController.getActionById: No action found with ID: "+ id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            });
    	} catch (Exception e) {
    	    logger.error("ActionController.getActionById: Error fetching action by ID: ", id, e.getMessage());
    		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    	}
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Action>> getAllActions() {
    	try {
            List<Action> actions = actionService.getAll();
            logger.info("ActionController.getAllActions: Success fetching all actions");
            return new ResponseEntity<>(actions, HttpStatus.OK);
    	} catch (Exception e) {
    	    logger.error("ActionController.getAllActions: Error fetching all actions", e.getMessage());
    		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    	}
    }

    @PutMapping("/update{id}")
    public ResponseEntity<Void> updateAction(@RequestParam int id, @RequestBody Action action) {
    	try {
            if (!actionService.getById(id).isPresent()) {
                logger.warn("ActionController.updateAction: No action found to update with ID:" + id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            action.setId(id);
            actionService.save(action);
            logger.info("ActionController.updateAction: Success updating action with ID: " +id);
            return new ResponseEntity<>(HttpStatus.OK);
    	} catch (Exception e) {
    	    logger.error("ActionController.updateAction: Error updating action with ID: "+id, e.getMessage());
    		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    	}
    }

    @DeleteMapping("/delete{id}")
    public ResponseEntity<Void> deleteAction(@RequestParam int id) {
    	try {
            if (!actionService.getById(id).isPresent()) {
                logger.warn("ActionController.deleteAction: No action found to delete with ID: "+ id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            actionService.delete(id);
            logger.info("ActionController.deleteAction: Success deleting action with ID: " +id);
            return new ResponseEntity<>(HttpStatus.OK);
    	} catch (Exception e) {
    	    logger.error("ActionController.deleteAction: Error deleting action with ID: " +id, e.getMessage());
    		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    	}
    }
}

