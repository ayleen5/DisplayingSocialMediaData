package com.tsfn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsfn.model.Loader;
import com.tsfn.service.LoaderService;


@RestController
@RequestMapping("/Files")
public class LoaderController {

	@Autowired
	private LoaderService csvService;
	String message = "";


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

}
