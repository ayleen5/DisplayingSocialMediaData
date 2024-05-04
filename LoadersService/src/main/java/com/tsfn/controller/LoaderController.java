package com.tsfn.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tsfn.message.ResponseMessage;
import com.tsfn.model.Loader;
import com.tsfn.model.ManualRunRequest;
import com.tsfn.service.LoaderService;

@RestController
@RequestMapping("/Files")
public class LoaderController {

	@Autowired
	private LoaderService csvService;
	String message = "";
	
	@PostMapping("/manual-run")
    public ResponseEntity<ResponseMessage> manualRun(
    		@RequestParam String loaderName,
            @RequestBody ManualRunRequest request) {

        try {

            String repositoryUrl = "https://api.github.com/repos/ayobna/tsofen_project_data_files/contents/";
            LocalDateTime startDate = request.getStartDate();
            LocalDateTime endDate = request.getEndDate();
            String accountID = request.getAccountID();

            String directoryPath = repositoryUrl + loaderName;
            csvService.processCsvFilesInRange(directoryPath, startDate, endDate, accountID);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage("Manual run for " + loaderName + " completed successfully."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Failed to process manual run for " + loaderName + ": " + e.getMessage()));
        }
    }
	@GetMapping("/process")
		    public ResponseEntity<ResponseMessage> processLoader(@RequestParam String loaderName) {
		        String url;
		        try {
		            switch (loaderName.toLowerCase()) {
		                case "instagram":
		                    url = "https://api.github.com/repos/ayobna/tsofen_project_data_files/contents/instagram";
		                    csvService.processCsvInstagramFile(url);
		                    break;
		                case "facebook":
		                    url = "https://api.github.com/repos/ayobna/tsofen_project_data_files/contents/facebook";
		                    csvService.processCsvFacebookFile(url);
		                    break;
		                case "linkedin":
		                    url = "https://api.github.com/repos/ayobna/tsofen_project_data_files/contents/linkedin";
		                    csvService.processCsvLinkedInFile(url);
		                    break;
		                default:
		                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		                            .body(new ResponseMessage("Loader not found: " + loaderName));
		            }
		            return ResponseEntity.status(HttpStatus.OK)
		                    .body(new ResponseMessage("Loader " + loaderName + " processed successfully."));
		        } catch (Exception e) {
		            e.printStackTrace();
		            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                    .body(new ResponseMessage("Failed to process loader " + loaderName + ": " + e.getMessage()));
		        }
		    }

	@PutMapping("/enable")
	public ResponseEntity<ResponseMessage> EnableLoader(@RequestParam String loaderName) {
		try {
			if (loaderName.equalsIgnoreCase("instagram")) {
				csvService.setIntasgram(true);
			} else if (loaderName.equalsIgnoreCase("Facebook")) {
				csvService.setFacebook(true);
			} else if (loaderName.equalsIgnoreCase("LinkedIn")) {
				csvService.setLinkedIn(true);
			}
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(loaderName + "enabled successfully."));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseMessage("uncuccessfully." + e.getMessage()));
		}
	}

	@PutMapping("/diable")
	public ResponseEntity<ResponseMessage> DisableLoader(@RequestParam String loaderName) {
		try {
			if (loaderName.equalsIgnoreCase("instagram")) {
				csvService.setIntasgram(false);
			} else if (loaderName.equalsIgnoreCase("Facebook")) {
				csvService.setFacebook(false);
			} else if (loaderName.equalsIgnoreCase("LinkedIn")) {
				csvService.setLinkedIn(false);
			}
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseMessage(loaderName + "disabled successfully."));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseMessage("uncuccessfully." + e.getMessage()));
		}
	}

	@GetMapping("/getAllByAccountLoader/{accountLoader}")
	public List<Loader> getAllFilesByAccountLoader(@PathVariable String accountLoader) {
		try {
			List<Loader> instagramFiles = csvService.findAllByAccountLoader(accountLoader);
			return instagramFiles;

		} catch (Exception e) {
			return null;
		}
	}

	@GetMapping("/impressionsGreaterThan/{threshold}")
	public List<Loader> getFilesWithImpressionGreaterThanThreshold(@PathVariable int threshold) {
		List<Loader> files = csvService.getFilesWithImpressionGreaterThanThreshold(threshold);
		if (files.isEmpty()) {
			return null;
		} else {
			return files;
		}
	}
	@GetMapping("/test")
	public  void test() {
		String repositoryUrl = "https://api.github.com/repos/ayobna/tsofen_project_data_files/contents/";

		csvService.processCsvInstagramFile(repositoryUrl + "instagram");
	}

}
