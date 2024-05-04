package com.tsfn.job;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tsfn.helper.FileType;
import com.tsfn.service.LoaderService;

@Component
public final class FileFetcherScheduler implements Runnable, InitializingBean, DisposableBean {

	@Autowired
	private LoaderService loaderService;

	private boolean running;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public boolean start() {
		if (!running) {
			running = true;
			scheduler.scheduleAtFixedRate(this, 0, 50, TimeUnit.MINUTES);
			return true;
		}
		return false;
	}

	public boolean stop() {
		if (running) {
			try {
				scheduler.shutdown();
				if (!scheduler.awaitTermination(40, TimeUnit.MINUTES)) {
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

		String repositoryUrl = "https://api.github.com/repos/ayobna/tsofen_project_data_files/contents/";

		if (loaderService.isIntasgram()) {
			loaderService.processCsvFile(repositoryUrl + "instagram", FileType.INSTAGRAM, true);
		}

		if (loaderService.isFacebook()) {
			loaderService.processCsvFile(repositoryUrl + "facebook", FileType.FACEBOOK, true);
		}

		if (loaderService.isLinkedIn()) {

			loaderService.processCsvFile(repositoryUrl + "linkedin", FileType.LINKEDIN, true);
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
