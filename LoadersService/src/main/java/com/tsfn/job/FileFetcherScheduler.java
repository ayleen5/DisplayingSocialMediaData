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

import com.tsfn.model.LoaderDTO;
import com.tsfn.service.FacebookService;
import com.tsfn.service.InstagramService;
import com.tsfn.service.LinkedInService;

@Component
public final class FileFetcherScheduler implements Runnable, InitializingBean, DisposableBean {

	@Autowired
	private FacebookService facebookService;

	@Autowired
	private InstagramService instagramService;

	@Autowired
	private LinkedInService linkedinService;

	// Maintain a map to track file timestamps
	private Map<String, Instant> fileTimestamps = new HashMap<>();

	private boolean running;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	// Path to the directory containing the files
	private String DIRECTORY_PATH = "C:\\Users\\yusra\\Desktop\\Java Microservice Development\\project\\files";

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
//		try {
//			// Fetch files from the specified directory
//			List<String> csvFiles = Files.list(Paths.get(DIRECTORY_PATH))
//					.filter(path -> path.getFileName().toString().endsWith(".csv"))
//					.map(path -> path.toAbsolutePath().toString()).collect(Collectors.toList());
//
//			// Process each CSV file
//			for (String csvFile : csvFiles) {
//
//				if (csvFile.contains("instagram")) {
//					System.out.println("Files Instagram   Instagram  Instagram:");
//					instagramService.processCsvInstagramFile(csvFile);
//				} else if (csvFile.contains("facebook")) {
//					System.out.println("Files facebook   facebook  facebook:");
//					facebookService.processCsvFacebookFile(csvFile);
//				} else if (csvFile.contains("linkedin")) {
//					System.out.println("Files LinkedIn   LinkedIn  LinkedIn:");
//					linkedinService.processCsvLinkedInFile(csvFile);
//				}
//			}

		instagramService.processCsvInstagramFile(
				"C:\\Users\\yusra\\Desktop\\Java Microservice Development\\project\\files\\instagram");

		facebookService.processCsvFacebookFile(
				"C:\\Users\\yusra\\Desktop\\Java Microservice Development\\project\\files\\facebook");

		linkedinService.processCsvLinkedInFile(
				"C:\\Users\\yusra\\Desktop\\Java Microservice Development\\project\\files\\linkedin");

//		} catch (IOException e) {
//			e.printStackTrace();
//		}

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
