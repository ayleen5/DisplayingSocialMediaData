package com.tsfn.controller.client.Loader;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@FeignClient(name = "loader-service", url = "http://localhost:7070")
public interface ClientLoaderController {

	@GetMapping("/Files/getAllByAccountLoader/{accountLoader}")
	List<Loader> getAllFilesByAccountLoader(@PathVariable String accountLoader);
	
	@GetMapping("/Files/impressionsGreaterThan/{threshold}")
	List<Loader> getFilesWithImpressionGreaterThanThreshold(@PathVariable int threshold);
		
    
   
}
