package com.tsfn.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsfn.model.Instagram;
import com.tsfn.repository.InstagramRepo;

@Service
public class InstagramService {

	@Autowired
	private InstagramRepo instagramRepo;

	// Create
	public Instagram createInstagramData(Instagram InstagramData) {
		return instagramRepo.save(InstagramData);
	}

	// Read
	public List<Instagram> getAllInstagramData() {
		return instagramRepo.findAll();
	}

	public Optional<Instagram> getInstagramDataById(int postId) {
		return instagramRepo.findById(postId);
	}

	// Update
	public Instagram updateInstagramData(int postId, Instagram updatedData) {
		Optional<Instagram> existingData = instagramRepo.findById(postId);
		if (existingData.isPresent()) {
			Instagram dataToUpdate = existingData.get();

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
			if (updatedData.getSaves() != 0) {
				dataToUpdate.setSaves(updatedData.getSaves());
			}
			if (updatedData.getLikes() != 0) {
				dataToUpdate.setLikes(updatedData.getLikes());
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

			return instagramRepo.save(dataToUpdate);
		} else {
			return null;
		}
	}


	// Delete
	public void deleteInstagramData(int postId) {
		instagramRepo.deleteById(postId);
	}
}

