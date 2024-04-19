package com.tsfn.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tsfn.service.LinkedInService;
import com.tsfn.model.LinkedIn;

@RestController
@RequestMapping("/linkedIn")
public class LinkedInController {
	
	@Autowired
    private LinkedInService linkedInService;
	
	// Create
    @PostMapping("/create")
    public ResponseEntity<LinkedIn> createLinkedInData(@RequestBody LinkedIn linkedInData) {
        LinkedIn createdData = linkedInService.createLinkedInData(linkedInData);
        return new ResponseEntity<>(createdData, HttpStatus.CREATED);
    }

    // Read
    @GetMapping("/getall")
    public ResponseEntity<List<LinkedIn>> getAllLinkedInData() {
        List<LinkedIn> linkedInDataList = linkedInService.getAllLinkedInData();
        return new ResponseEntity<>(linkedInDataList, HttpStatus.OK);
    }

//    @GetMapping("/get{postId}")
//    public ResponseEntity<LinkedIn> getLinkedInDataById(@RequestParam int postId) {
//        Optional<LinkedIn> linkedInData = linkedInService.getLinkedInDataById(postId);
//        return linkedInData.map(data -> new ResponseEntity<>(data, HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//
//    // Update
//    @PutMapping("/update{postId}")
//    public ResponseEntity<LinkedIn> updateLinkedInData(@RequestParam int postId, @RequestBody LinkedIn updatedData) {
//        LinkedIn updatedLinkedInData = linkedInService.updateLinkedInData(postId, updatedData);
//        if (updatedLinkedInData != null) {
//            return new ResponseEntity<>(updatedLinkedInData, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    // Delete
//    @DeleteMapping("/delete{postLink}")
//    public ResponseEntity<Void> deleteLinkedInData(@RequestParam String postLink) {
//        linkedInService.deleteLinkedInData(postId);
//        return new ResponseEntity<>(HttpStatus.OK);
//    } 
    
    
}
