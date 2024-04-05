package com.tsfn.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.tsfn.model.Facebook;
import com.tsfn.repository.FacebookRepo;

import org.springframework.stereotype.Service;

@Service
public class FacebookService {

	
	@Autowired
	private FacebookRepo facebookRepo;
	
	// Create
    public Facebook createFacebookData(Facebook facebookData) {
        return facebookRepo.save(facebookData);
    }

    // Read
    public List<Facebook> getAllFacebookData() {
        return facebookRepo.findAll();
    }

    public Optional<Facebook> getFacebookDataById(int postId) {
        return facebookRepo.findById(postId);
    }

    // Update
    public Facebook updateFacebookData(int postId, Facebook updatedData) {
        Optional<Facebook> existingData = facebookRepo.findById(postId);
        if (existingData.isPresent()) {
            Facebook dataToUpdate = existingData.get();
            
            if (updatedData.getTimestamp() != null) {
                dataToUpdate.setTimestamp(updatedData.getTimestamp());
            }
            if (updatedData.getPostType() != null) {
                dataToUpdate.setPostType(updatedData.getPostType());
            }
            if (updatedData.getImpressions() != 0) {
                dataToUpdate.setImpressions(updatedData.getImpressions());
            }
            if (updatedData.getReach() != 0) {
                dataToUpdate.setReach(updatedData.getReach());
            }
            if (updatedData.getTotalClicks() != 0) {
                dataToUpdate.setTotalClicks(updatedData.getTotalClicks());
            }
            if (updatedData.getReactions() != 0) {
                dataToUpdate.setReactions(updatedData.getReactions());
            }
            if (updatedData.getComments() != 0) {
                dataToUpdate.setComments(updatedData.getComments());
            }
            if (updatedData.getShares() != 0) {
                dataToUpdate.setShares(updatedData.getShares());
            }
            if (updatedData.getEngagementRate() != 0) {
                dataToUpdate.setEngagementRate(updatedData.getEngagementRate());
            }
            if (updatedData.getCtr() != 0) {
                dataToUpdate.setCtr(updatedData.getCtr());
            }
            dataToUpdate.setPostId(postId);
            
            return facebookRepo.save(dataToUpdate);
        } else {
            return null;
        }
    }


    // Delete
    public void deleteFacebookData(int postId) {
        facebookRepo.deleteById(postId);
    }
}
