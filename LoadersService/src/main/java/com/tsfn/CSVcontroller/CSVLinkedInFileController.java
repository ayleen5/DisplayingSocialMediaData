package com.tsfn.CSVcontroller;

import com.tsfn.message.ResponseMessage;
import com.tsfn.model.LinkedIn;
import com.tsfn.CSVservice.CSVLinkedInFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/linkedinFile")
public class CSVLinkedInFileController {

    @Autowired
    private CSVLinkedInFileService linkedInFileService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadCsv(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new Exception("File is empty!");
            }
            linkedInFileService.processCsv(file);
            String message = "CSV file uploaded and processed successfully.";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("An error occurred while processing the CSV file. " + e.getMessage()));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<LinkedIn>> getAllLinkedInFiles() {
        try {
            List<LinkedIn> linkedInFiles = linkedInFileService.getAllLinkedInFiles();
            return linkedInFiles.isEmpty()
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.ok(linkedInFiles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAllLinkedInFiles() {
    	try {
    	    int deletedCount = linkedInFileService.deleteAllLinkedInFiles();
    	       if (deletedCount > 0) {
    	           return ResponseEntity.ok("Deleted " + deletedCount + " linkedIn files.");
    	       } else {
    	           return ResponseEntity.ok("No linkedIn files found to delete.");
    	       }
    	   } catch (Exception e) {
    	     return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    	   }
    }
}
