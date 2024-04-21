package com.tsfn.model;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Instagram {

	private String postId;
	private String accountId;
	private String accountUsername;
	private String accountName;
	private String description;
	private double durationSec;
	private LocalDateTime publishTime;	
	private String permalink;	
	private String postType;	
	private String dataComment;	
	private String date;	
	private double impressions;	
	private double reach;	
	private double shares;	
	private double follows;	
	private double likes;	
	private double comments;	
	private double saves;	
	private double plays;	

}
