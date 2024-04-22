package com.tsfn.model;

import jakarta.persistence.Entity;
 
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name= "actions")
@Data
public class Action {
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id ;
	
	private String name;
	//private Date created_date; // should check if it it utils or sql
	// need to thing about that, kinda weird
//	private String created_by;
//	private Hashtable<String,ArrayList<String>> conditions;
//	@Enumerated(EnumType.STRING)
//	private ActionsType action;
	//private Time run_on_time;
//	private int run_on_day;
//	private String notification_text;
//	private String sentTo;
//	private boolean is_deleted;
//	private Date last_update;
//	private Date next_run;
	
}