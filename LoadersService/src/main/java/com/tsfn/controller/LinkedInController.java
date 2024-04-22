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
import com.tsfn.service.LinkedInService;




@RestController
@RequestMapping("/linkedinFiles")
public class LinkedInController {

    @Autowired
    private LinkedInService linkedInService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadCsv(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new Exception("File is empty!");
            }
            linkedInService.processCsvLinkedInFile(file);
            String message = "CSV file uploaded and processed successfully.";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("An error occurred while processing the CSV file. " + e.getMessage()));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<LoaderDTO>> getAllLinkedInFiles() {
        try {
            List<LoaderDTO> linkedInFiles = linkedInService.getAllLinkedInFiles();
            return linkedInFiles.isEmpty()
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.ok(linkedInFiles);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<?> deleteAllLinkedInFiles() {
        try {
            int deletedCount = linkedInService.deleteAllLinkedInFiles();
            if (deletedCount > 0) {
                return ResponseEntity.ok("Deleted " + deletedCount + " LinkedIn files.");
            } else {
                return ResponseEntity.ok("No LinkedIn files found to delete.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
