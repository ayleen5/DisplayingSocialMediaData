//package com.tsfn.controller;
//
//import com.tsfn.job.FileFetcherScheduler;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/fetchFiles")
//public class FileFetcherSchedulerController {
//
//    private FileFetcherScheduler fileFetcherScheduler;
//
//    @Autowired
//    public FileFetcherSchedulerController(FileFetcherScheduler fileFetcherScheduler) {
//        this.fileFetcherScheduler = fileFetcherScheduler;
//    }
//
//    @PostMapping("/start")
//    public String startScheduler() {
//        if (fileFetcherScheduler.start()) {
//            return "FileFetcherScheduler started successfully.";
//        } else {
//            return "FileFetcherScheduler is already running.";
//        }
//    }
//
//    @PostMapping("/stop")
//    public String stopScheduler() {
//        if (fileFetcherScheduler.stop()) {
//            return "FileFetcherScheduler stopped successfully.";
//        } else {
//            return "FileFetcherScheduler is not running.";
//        }
//    }
//}
