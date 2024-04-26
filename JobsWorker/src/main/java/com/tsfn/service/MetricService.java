package com.tsfn.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsfn.model.Metric;
import com.tsfn.repository.MetricRepository;
import com.tsfn.service.exceptions.MetricNotFoundException;

@Service
public class MetricService {
	
	@Autowired
	private MetricRepository metricRepository;
	
	
	public Metric getMetricById(int id) {
        return metricRepository.findById(id)
                               .orElseThrow(() -> new MetricNotFoundException("Metric not found with ID: " + id));
    }
	
	public Metric addMertic(Metric metric) {
		return metricRepository.save(metric);
	}
	
	public Metric updateMertic(Metric metric) {
		// Check if the metric exists
		if (!metricRepository.existsById(metric.getId())) {
			throw new MetricNotFoundException("Metric Not Found.");
		}
		// Update the metric
		return metricRepository.save(metric);
	}
	
	public void deleteMetric(int metricId) {
		// Delete the metric
		if (!metricRepository.existsById(metricId)) {
			throw new MetricNotFoundException("Metric not found.");
		}
		metricRepository.deleteById(metricId);
	}
	
	public List<Metric> getMetrics() {
		return metricRepository.findAll();
	}
}
