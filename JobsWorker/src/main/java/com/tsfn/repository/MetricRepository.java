package com.tsfn.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.tsfn.model.Metric;

//@Transactional(readOnly = true)
public interface MetricRepository extends JpaRepository<Metric, Integer> {
	
 	Metric findByNameAndId(String Name, int Id);
    

}
