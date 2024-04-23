package com.tsfn.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.tsfn.model.Metric;

public interface MetricRepository extends JpaRepository<Metric, Integer> {
	
 	Metric findByNameAndId(String Name, int Id);
    

}
