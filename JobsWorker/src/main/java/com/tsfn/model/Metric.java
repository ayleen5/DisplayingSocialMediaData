package com.tsfn.model;

import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Transactional
@Entity
@Table(name= "metrics")
@Data
public class Metric {
	@Id
	private int id ;
	private String name;
	private String metric;
	private int threshold;
	private int time_frame_hours ;
}
