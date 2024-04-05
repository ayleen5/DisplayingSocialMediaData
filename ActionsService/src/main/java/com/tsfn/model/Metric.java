package com.tsfn.model;


import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name= "metrics")
@Data
public class Metric {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id ;
	
	private String name;
	private Date created_date; 
	// ???
	private String created_by;
	//@Enumerated(EnumType.STRING)
	private String metricType;
	private int threshold;
	private int time_frame_hours ;
	private boolean  is_deleted;
}
