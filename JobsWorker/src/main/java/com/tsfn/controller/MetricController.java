package com.tsfn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.tsfn.model.Metric;
import com.tsfn.service.MetricService;
import com.tsfn.service.exceptions.MerticAlreadyExistsException;

import com.tsfn.service.exceptions.MetricNotFoundException;

@RestController
@RequestMapping("/metric")
public class MetricController {
	@Autowired
    private MetricService metricService;
	
	@GetMapping("/{id}")
    public ResponseEntity<?> getMetricById(@PathVariable int id) {
        try {
        	Metric metric = metricService.getMetricById(id);
            return new ResponseEntity<>(metric, HttpStatus.OK);
        } catch (MetricNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
	
	
	@PostMapping("/create")
    public ResponseEntity<?> createMetric(@RequestBody Metric metric) {
        try {
        	Metric createdMetric = metricService.addMertic(metric);
            return new ResponseEntity<>(createdMetric, HttpStatus.CREATED);
        } catch (MerticAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
 

    @PutMapping("/update")
    public ResponseEntity<?> updateMetric(@RequestBody Metric metric) {
        try {
        	Metric updatedMetric = metricService.updateMertic(metric);
            return new ResponseEntity<>(updatedMetric, HttpStatus.OK);
        } catch (MetricNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{merticId}")
    public ResponseEntity<?> deleteMetric(@PathVariable int merticId) {
        try {
            metricService.deleteMetric(merticId);
            return new ResponseEntity<>("Metric deleted successfully", HttpStatus.OK);
        } catch (MetricNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllMetrics() {
        try {
            return new ResponseEntity<>(metricService.getMetrics(), HttpStatus.OK);
        } catch (MetricNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
