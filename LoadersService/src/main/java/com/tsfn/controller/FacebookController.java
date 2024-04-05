package com.tsfn.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tsfn.model.Facebook;
import com.tsfn.service.FacebookService;

@RestController
@RequestMapping("/facebook")
public class FacebookController {

    @Autowired
    private FacebookService facebookService;

    // Create
    @PostMapping("/create/")
    public ResponseEntity<Facebook> createFacebookData(@RequestBody Facebook facebookData) {
        Facebook createdData = facebookService.createFacebookData(facebookData);
        return new ResponseEntity<>(createdData, HttpStatus.CREATED);
    }

    // Read
    @GetMapping("/getall/")
    public ResponseEntity<List<Facebook>> getAllFacebookData() {
        List<Facebook> facebookDataList = facebookService.getAllFacebookData();
        return new ResponseEntity<>(facebookDataList, HttpStatus.OK);
    }

    @GetMapping("/get{postId}")
    public ResponseEntity<Facebook> getFacebookDataById(@RequestParam int postId) {
        Optional<Facebook> facebookData = facebookService.getFacebookDataById(postId);
        return facebookData.map(data -> new ResponseEntity<>(data, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update
    @PutMapping("/update{postId}")
    public ResponseEntity<Facebook> updateFacebookData(@RequestParam int postId, @RequestBody Facebook updatedData) {
        Facebook updatedFacebookData = facebookService.updateFacebookData(postId, updatedData);
        if (updatedFacebookData != null) {
            return new ResponseEntity<>(updatedFacebookData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete
    @DeleteMapping("/delete{postId}")
    public ResponseEntity<Void> deleteFacebookData(@RequestParam int postId) {
        facebookService.deleteFacebookData(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
