package com.tsfn.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tsfn.dto.LoaderDTO;
import com.tsfn.model.Facebook;
import com.tsfn.service.FacebookService;

@RestController
@RequestMapping("/facebook")
public class FacebookController {

    @Autowired
    private FacebookService facebookService;

    // Create
    @PostMapping("/create")
    public ResponseEntity<LoaderDTO> createFacebookData(@RequestBody LoaderDTO facebookData) {
    	LoaderDTO createdData = facebookService.createFacebookData(facebookData);
        return new ResponseEntity<>(createdData, HttpStatus.CREATED);
    }

    // Read
    @GetMapping("/getall")
    public ResponseEntity<List<LoaderDTO>> getAllFacebookData() {
        List<LoaderDTO> facebookDataList = facebookService.getAllFacebookData();
        return new ResponseEntity<>(facebookDataList, HttpStatus.OK);
    }

    @GetMapping("/get{postId}")
    public ResponseEntity<LoaderDTO> getFacebookDataById(@RequestParam int postId) {
        Optional<LoaderDTO> facebookData = facebookService.getFacebookDataById(postId);
        return facebookData.map(data -> new ResponseEntity<>(data, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

 // Update
    @PutMapping("/update{postId}")
    public ResponseEntity<LoaderDTO> updateFacebookData(@RequestParam int postId, @RequestBody LoaderDTO updatedData) {
        LoaderDTO updatedFacebookData = facebookService.updateFacebookData(postId, updatedData);
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
