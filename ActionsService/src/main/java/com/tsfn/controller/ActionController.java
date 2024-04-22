package com.tsfn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tsfn.model.Action;
import com.tsfn.service.ActionService;

@RestController
@RequestMapping("/actions")
public class ActionController {
	
	
	@Autowired
    private  ActionService actionService;

    
    

    @PostMapping("/create")
    public ResponseEntity<Action> createAction(@RequestBody Action action) {
        actionService.save(action);
        return new ResponseEntity<>(action, HttpStatus.CREATED);
    }

    @GetMapping("/get{id}")
    public ResponseEntity<Action> getActionById(@RequestParam int id) {
        return actionService.getById(id)
                .map(action -> new ResponseEntity<>(action, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Action>> getAllActions() {
        return new ResponseEntity<>(actionService.getAll(), HttpStatus.OK);
    }

    @PutMapping("/update{id}")
    public ResponseEntity<Void> updateAction(@RequestParam int id, @RequestBody Action action) {
        if (!actionService.getById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        action.setId(id);
        actionService.save(action);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete{id}")
    public ResponseEntity<Void> deleteAction(@RequestParam int id) {
        if (!actionService.getById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        actionService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

