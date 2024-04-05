package com.tsfn.model;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
	private Date created_date; // should check if it it utils or sql
	// need to thing about that, kinda weird
	private String created_by;
	private Hashtable<String,ArrayList<String>> conditions;
	@Enumerated(EnumType.STRING)
	private String metricType;
	private int threshold;
	private int time_frame_hours ;
	private boolean  is_deleted;
}
