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

import com.tsfn.model.Instagram;
import com.tsfn.service.InstagramService;


@RestController
@RequestMapping("/instagram")
public class InstagramController {
	@Autowired
	private InstagramService instagramService;

	@PostMapping("/create")
    public ResponseEntity<Instagram> createInstagramData(@RequestBody Instagram instagramData) {
		Instagram createdData = instagramService.createInstagramData(instagramData);
        return new ResponseEntity<>(createdData, HttpStatus.CREATED);
    }

    // Read
    @GetMapping("/getall/")
    public ResponseEntity<List<Instagram>> getAllFacebookData() {
        List<Instagram> instagramDataList = instagramService.getAllInstagramData();
        return new ResponseEntity<>(instagramDataList, HttpStatus.OK);
    }

    @GetMapping("/get{postId}")
    public ResponseEntity<Instagram> getInstagramDataById(@RequestParam int postId) {
        Optional<Instagram> instagramData = instagramService.getInstagramDataById(postId);
        return instagramData.map(data -> new ResponseEntity<>(data, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update
    @PutMapping("/update{postId}")
    public ResponseEntity<Instagram> updateInstagramData(@RequestParam int postId, @RequestBody Instagram updatedData) {
    	Instagram updatedInstagramData = instagramService.updateInstagramData(postId, updatedData);
        if (updatedInstagramData != null) {
            return new ResponseEntity<>(updatedInstagramData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete
    @DeleteMapping("/delete{postId}")
    public ResponseEntity<Void> deleteInstagramData(@RequestParam int postId) {
        instagramService.deleteInstagramData(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
