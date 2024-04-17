package com.tsfn.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;

import com.tsfn.dto.LoaderDTO;
import com.tsfn.model.Facebook;
import com.tsfn.repository.FacebookRepo;

import org.springframework.stereotype.Service;

@Service
public class FacebookService {


	@Autowired
	private FacebookRepo facebookRepo;

	@Autowired
	private ModelMapper modelMapper;
	


	    

	private LoaderDTO convertEntityToDto(Facebook facebook){
		modelMapper.getConfiguration()
		.setMatchingStrategy(MatchingStrategies.LOOSE);
		LoaderDTO facebookDTO = new LoaderDTO();
		facebookDTO = modelMapper.map(facebook, LoaderDTO.class);
		return facebookDTO;
	}
	
	private Facebook convertDtoToEntity(LoaderDTO loaderDTO) {
	    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
	    Facebook facebook = modelMapper.map(loaderDTO, Facebook.class);
	    return facebook;
	}

	// Create
	public LoaderDTO createFacebookData(LoaderDTO facebookData) {
	    Facebook facebookEntity = convertDtoToEntity(facebookData);
	    Facebook savedEntity = facebookRepo.save(facebookEntity);
	    return convertEntityToDto(savedEntity);
	}


	// Read
	public List<LoaderDTO> getAllFacebookData() {
		return facebookRepo.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
	}

	public Optional<LoaderDTO> getFacebookDataById(int postId) {
	    Optional<Facebook> facebookData = facebookRepo.findById(postId);
	    return facebookData.map(this::convertEntityToDto);
	}


	// Update
	// Update
	public LoaderDTO updateFacebookData(int postId, LoaderDTO updatedData) {
	    Optional<Facebook> existingData = facebookRepo.findById(postId);
	    if (existingData.isPresent()) {
	        Facebook dataToUpdate = existingData.get();

	        if (updatedData.getTimestamp() != null) {
	            dataToUpdate.setTimestamp(updatedData.getTimestamp());
	        }
	        if (updatedData.getContentType() != null) {
	            dataToUpdate.setContentType(updatedData.getContentType());
	        }
	        if (updatedData.getImpressions() != 0) {
	            dataToUpdate.setImpressions(updatedData.getImpressions());
	        }
	        if (updatedData.getViews() != 0) {
	            dataToUpdate.setViews(updatedData.getViews());
	        }
	        if (updatedData.getClicks() != 0) {
	            dataToUpdate.setClicks(updatedData.getClicks());
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
	        if (updatedData.getEngagementrate() != 0) {
	            dataToUpdate.setEngagementrate(updatedData.getEngagementrate());
	        }
	        if (updatedData.getCTR() != 0) {
	            dataToUpdate.setCTR(updatedData.getCTR());
	        }
	        dataToUpdate.setPostId(postId);

	        Facebook updatedEntity = facebookRepo.save(dataToUpdate);
	        return convertEntityToDto(updatedEntity);
	    } else {
	        return null;
	    }
	}



	// Delete
	// Delete
	public void deleteFacebookData(int postId) {
	    facebookRepo.deleteById(postId);
	}

}
