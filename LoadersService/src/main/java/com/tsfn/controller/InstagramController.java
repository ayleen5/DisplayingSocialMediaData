package com.tsfn.controller;




import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.multipart.MultipartFile;

import com.tsfn.message.ResponseMessage;
import com.tsfn.model.LoaderDTO;
import com.tsfn.service.InstagramService;



@RestController
@RequestMapping("/instagramFiles")
public class InstagramController {

    @Autowired
    private InstagramService csvService;
    String message = "";
    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadCsv() {
        try {
        	
        	MultipartFile file = null;
        	if (file.isEmpty())
            {
            	throw new Exception ("file is empty!");
            }
        	csvService.processCsvInstagramFile(file);
            message = "CSV file uploaded and processed successfully.";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            		.body(new ResponseMessage("An error occurred while processing the CSV file." + e.getMessage()));
        }
    }
    
    @GetMapping("/getAll")
	  public ResponseEntity<List<LoaderDTO>> getAllInstagramFiles() {
	    try {
	    	List<LoaderDTO> instagramFiles = csvService.getAllInstagramFiles();
            return instagramFiles.isEmpty()
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.ok(instagramFiles);
			/*
			 * List<InstagramFile> instagramFiles = csvService.getAllInstagramFiles();
			 * 
			 * if (instagramFiles.isEmpty()) { return new
			 * ResponseEntity<>(HttpStatus.NO_CONTENT); }
			 * 
			 * return new ResponseEntity<>(instagramFiles, HttpStatus.OK);
			 */
	    } catch (Exception e) {
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }
    
    @DeleteMapping("/deleteAll")
	  public ResponseEntity<?> deleteAllInstagramFiles() {
	    try {
	    	int deletedCount = csvService.deleteAllInstagramFiles();
	        if (deletedCount > 0) {
	            return ResponseEntity.ok("Deleted " + deletedCount + " Instagram files.");
	        } else {
	            return ResponseEntity.ok("No Instagram files found to delete.");
	        }
	    } catch (Exception e) {
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }
    

}