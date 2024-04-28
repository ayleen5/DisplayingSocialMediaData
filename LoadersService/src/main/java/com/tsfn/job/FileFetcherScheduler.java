package com.tsfn.job;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tsfn.model.DisableEnable;
import com.tsfn.model.Loader;
//import com.tsfn.service.FacebookService;
//import com.tsfn.service.InstagramService;
//import com.tsfn.service.LinkedInService;
import com.tsfn.service.LoaderService;

@Component
public final class FileFetcherScheduler implements Runnable, InitializingBean, DisposableBean {

	@Autowired
	private LoaderService loaderService;

	// Maintain a map to track file timestamps
	private Map<String, Instant> fileTimestamps = new HashMap<>();

	private boolean running;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	// Path to the directory containing the files

	private String DIRECTORY_PATH = "https://github.com/fadykittan/tsofen_project_data_files";

	public boolean start() {
		if (!running) {
			running = true;
			scheduler.scheduleAtFixedRate(this, 0, 5, TimeUnit.SECONDS); // Fetch files every 15 seconds
			return true;
		}
		return false;
	}

	public boolean stop() {
		if (running) {
			try {
				scheduler.shutdown();
				if (!scheduler.awaitTermination(5, TimeUnit.MINUTES)) {
					return false;
				}
				running = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	@Override
	public void run() {
		System.err.println(loaderService.isIntasgram());
		if (loaderService.isIntasgram()) {
			loaderService.processCsvInstagramFile(
					"C:\\Users\\yusra\\Desktop\\Java Microservice Development\\project\\files\\instagram"
			/*
			 * "https://github.com/fadykittan/tsofen_project_data_files/tree/main/instagram"
			 */);
		}

		if (loaderService.isFacebook()) {
			loaderService.processCsvFacebookFile(
					"C:\\Users\\yusra\\Desktop\\Java Microservice Development\\project\\files\\facebook");
//			loaderService.processCsvFacebookFile(
//					"https://github.com/fadykittan/tsofen_project_data_files/tree/main/facebook");
		}

		if (loaderService.isLinkedIn()) {
			loaderService.processCsvLinkedInFile(
					"C:\\Users\\yusra\\Desktop\\Java Microservice Development\\project\\files\\linkedin");
//			loaderService.processCsvLinkedInFile(
//					"https://github.com/fadykittan/tsofen_project_data_files/tree/main/linkedin");
		}

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (start()) {
			System.out.println("FileFetcherScheduler thread is running.");
			return;
		}
		System.err.println("FileFetcherScheduler thread encountered an error and is not running.");
	}

	@Override
	public void destroy() throws Exception {
		if (stop()) {
			System.out.println("FileFetcherScheduler thread is closed.");
			return;
		}
		System.err.println("FileFetcherScheduler thread encountered an error and is not closed.");
	}
}
