package com.tsfn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

 
import com.tsfn.model.Action;
import com.tsfn.service.ActionService;
import com.tsfn.service.exception.ActionAlreadyExistsException;
import com.tsfn.service.exception.ActionNotFoundException;
@RestController
@RequestMapping("/Actions")
public class ActionController {

	 @Autowired
	 private ActionService ActionService;
	 
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        Action Action = ActionService.login(email, password);
        if (Action != null) {
            return new ResponseEntity<>(Action, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }
    }
    
    
    
    @GetMapping("/{ActionId}")
    public ResponseEntity<?> getActionById(@PathVariable int ActionId) {
        try {
            Action Action = ActionService.getActionById(ActionId);
            return new ResponseEntity<>(Action, HttpStatus.OK);
        } catch (ActionNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping("/create")
    public ResponseEntity<?> createAction(@RequestBody Action Action) {
        try {
            Action createdAction = ActionService.createAction(Action);
            return new ResponseEntity<>(createdAction, HttpStatus.CREATED);
        } catch (ActionAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{ActionId}")
    public ResponseEntity<?> updateAction(@PathVariable int ActionId, @RequestBody Action Action) {
        try {
            Action.setId(ActionId);
            Action updatedAction = ActionService.updateAction(Action);
            return new ResponseEntity<>(updatedAction, HttpStatus.OK);
        } catch (ActionNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{ActionId}")
    public ResponseEntity<?> deleteAction(@PathVariable int ActionId) {
        try {
            ActionService.deleteAction(ActionId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ActionNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
  
}
