package com.tsfn.dto;

import java.sql.Time;

import lombok.Data;

@Data
public class LoaderDTO {
	
	private int PostID;
	private Time Timestamp;
	private String ContentType;
	private int Impressions;
	private int Views;
	private int Clicks;
	private double CTR;
	private int Likes;
	private int Comments;
	private int Shares;
	private double Engagementrate;
	
	}


