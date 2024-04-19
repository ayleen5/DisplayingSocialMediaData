package com.tsfn.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LoaderDTO {
	
	private int PostID;
	private LocalDateTime  Timestamp;
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


