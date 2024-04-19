package com.tsfn.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsfn.model.LinkedIn;
import com.tsfn.repository.LinkedInRepo;

@Service
public class LinkedInService {

	@Autowired
	private LinkedInRepo linkedInRepo;
	
	// Create
    public LinkedIn createLinkedInData(LinkedIn linkedInData) {
        return linkedInRepo.save(linkedInData);
    }
    
    // Read
 	public List<LinkedIn> getAllLinkedInData() {
 		return linkedInRepo.findAll();
 	}
 	
 	// Delete
    public void deleteLinkedInData(int postId) {
        linkedInRepo.deleteById(postId);
    }
 	
 	public Optional<LinkedIn> getLinkedInDataById(int postId) {
		return linkedInRepo.findById(postId);
	}
 	
 	// Update
    public LinkedIn updateLinkedInData(int postId, LinkedIn updatedData) {
        Optional<LinkedIn> existingData = linkedInRepo.findById(postId);
        if (existingData.isPresent()) {
            LinkedIn dataToUpdate = existingData.get();
            
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
            
            return linkedInRepo.save(dataToUpdate);
        } else {
            return null;
        }
    }
 	
 	
 	
 	
 	
}
