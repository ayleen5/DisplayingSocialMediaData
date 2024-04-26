//package com.tsfn.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.tsfn.message.ResponseMessage;
//import com.tsfn.model.LoaderDTO;
//import com.tsfn.service.FacebookService;
//
//@RestController
//@RequestMapping("/facebookFiles")
//public class FacebookController {
//
//    @Autowired
//    private FacebookService facebookService;
//    private static final String CSV_FILE_PATH = "C:\\Users\\yusra\\Desktop\\Java Microservice Development\\project\\files";
//    
//    
//    @PostMapping("/upload")
//    public ResponseEntity<ResponseMessage> uploadCsv() {
//        try {
//        	facebookService.processCsvFacebookFile(CSV_FILE_PATH);
//            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("CSV files uploaded and processed successfully."));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ResponseMessage("An error occurred while processing the CSV files: " + e.getMessage()));
//        }
//    }
//
////    @PostMapping("/upload")
////    public ResponseEntity<ResponseMessage> uploadCsv(@RequestParam("file") MultipartFile file) {
////        try {
////            if (file.isEmpty()) {
////                throw new Exception("File is empty!");
////            }
////            facebookService.processCsvFacebookFile(file);
////            String message = "CSV file uploaded and processed successfully.";
////            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
////        } catch (Exception e) {
////            e.printStackTrace();
////            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
////                    .body(new ResponseMessage("An error occurred while processing the CSV file. " + e.getMessage()));
////        }
////    }
//
//    @GetMapping("/getAll")
//    public ResponseEntity<List<LoaderDTO>> getAllFacebookFiles() {
//        try {
//            List<LoaderDTO> facebookFiles = facebookService.getAllFacebookFiles();
//            return facebookFiles.isEmpty()
//                    ? ResponseEntity.noContent().build()
//                    : ResponseEntity.ok(facebookFiles);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @DeleteMapping("/deleteAll")
//    public ResponseEntity<?> deleteAllFacebookFiles() {
//        try {
//            int deletedCount = facebookService.deleteAllFacebookFiles();
//            if (deletedCount > 0) {
//                return ResponseEntity.ok("Deleted " + deletedCount + " Facebook files.");
//            } else {
//                return ResponseEntity.ok("No Facebook files found to delete.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//}
