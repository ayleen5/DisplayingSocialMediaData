package com.tsfn.controller.client.Loader;

import java.time.LocalDateTime;


import lombok.Data;



@Data

public class Loader {
	
	private int id;
	private String postId;
	private String contentType;
	private double impressions;
	private double views;
	private double clicks;
	private double cTR;
	private double likes;
	private double comments;
	private double shares;
	private double engagementrate;
	
	private String accountLoader;
	private LocalDateTime timestamp; 
	

}
