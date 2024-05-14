package com.tsfn.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//import com.tsfn.controller.client.security.Role;
//import com.tsfn.controller.client.security.SeccurityHelper;
//import com.tsfn.controller.client.security.VerifyTokenAndCheckRolesResponse;
import com.tsfn.helper.FileType;

import com.tsfn.message.ResponseMessage;
import com.tsfn.model.Loader;
import com.tsfn.model.ManualRunRequest;
import com.tsfn.service.LoaderService;

@RestController
@RequestMapping("/Files")
public class LoaderController {

	@Autowired
	private LoaderService loaderService;
	
//	@Autowired
//	private SeccurityHelper seccurityHelper;

	String message = "";
	
	private static final Logger logger = LoggerFactory.getLogger(LoaderService.class);


	@PostMapping("/manual-run")
	public ResponseEntity<ResponseMessage> manualRun(@RequestParam String loaderName,
			@RequestBody ManualRunRequest request, @RequestHeader("Authorization") String authorizationHeader) {

		try {

//			List<Role> roles = new ArrayList<>();
//			roles.add(Role.TRIGGER_MANUAL_SCAN);
//			roles.add(Role.ADMIN);
//
//			VerifyTokenAndCheckRolesResponse verifyTokenAndCheckRolesResponse = seccurityHelper.verifyTokenAndCheckRoles(authorizationHeader, roles);
//			if (verifyTokenAndCheckRolesResponse.isVerifyTokenAndCheckRoles()) {
//
//				String repositoryUrl = "https://api.github.com/repos/ayobna/tsofen_project_data_files/contents/";
				LocalDateTime startDate = request.getStartDate();
				LocalDateTime endDate = request.getEndDate();
				String accountID = request.getAccountID();

			//	String directoryPath = repositoryUrl + loaderName;
				switch (loaderName.toLowerCase()) {
//				case "instagram":
//					loaderService.processCsvFilesInRange(directoryPath, startDate, endDate, accountID,
//							FileType.INSTAGRAM);
//					break;
//				case "facebook":
//					loaderService.processCsvFilesInRange(directoryPath, startDate, endDate, accountID,
//							FileType.FACEBOOK);
//					break;
//				case "linkedin":
//					loaderService.processCsvFilesInRange(directoryPath, startDate, endDate, accountID,
//							FileType.LINKEDIN);
//					break;
//				default:
//					logger.info("LoaderController.ManualRun" + verifyTokenAndCheckRolesResponse.getMessage());
//					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//							.body(new ResponseMessage("Loader not found: " + loaderName));
				}
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseMessage("Manual run for " + loaderName + " completed successfully."));

//			}
//			logger.info("LoaderController.ManualRun" + verifyTokenAndCheckRolesResponse.getMessage());
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("LoaderController.ManualRun" + e.getMessage());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new ResponseMessage("Failed to process manual run for " + loaderName + ": " + e.getMessage()));
		}
	}

	@GetMapping("/process")
	public ResponseEntity<ResponseMessage> processLoader(@RequestParam String loaderName) {

		String repositoryUrl = "https://api.github.com/repos/ayobna/tsofen_project_data_files/contents/";

		try {
			switch (loaderName.toLowerCase()) {
			case "instagram":
				loaderService.processCsvFile(repositoryUrl + "instagram", FileType.INSTAGRAM, false);
				break;
			case "facebook":

				loaderService.processCsvFile(repositoryUrl + "facebook", FileType.FACEBOOK, false);
				break;
			case "linkedin":
				loaderService.processCsvFile(repositoryUrl + "linkedin", FileType.LINKEDIN, false);
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
				loaderService.setIntasgram(true);
			} else if (loaderName.equalsIgnoreCase("Facebook")) {
				loaderService.setFacebook(true);
			} else if (loaderName.equalsIgnoreCase("LinkedIn")) {
				loaderService.setLinkedIn(true);
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
				loaderService.setIntasgram(false);
			} else if (loaderName.equalsIgnoreCase("Facebook")) {
				loaderService.setFacebook(false);
			} else if (loaderName.equalsIgnoreCase("LinkedIn")) {
				loaderService.setLinkedIn(false);
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
			List<Loader> instagramFiles = loaderService.findAllByAccountLoader(accountLoader);
			return instagramFiles;

		} catch (Exception e) {
			return null;
		}
	}

	@GetMapping("/impressionsGreaterThan/{threshold}")
	public List<Loader> getFilesWithImpressionGreaterThanThreshold(@PathVariable int threshold) {
		List<Loader> files = loaderService.getFilesWithImpressionGreaterThanThreshold(threshold);
		if (files.isEmpty()) {
			return null;
		} else {
			return files;
		}
	}

	@GetMapping("/test")
	public void test() {
		String repositoryUrl = "https://api.github.com/repos/ayobna/tsofen_project_data_files/contents/";

		loaderService.processCsvFile(repositoryUrl + "instagram", FileType.INSTAGRAM, false);
	}

}
