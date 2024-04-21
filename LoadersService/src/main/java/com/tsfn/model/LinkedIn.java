package com.tsfn.model;

import java.sql.Date;
import lombok.Data;
 
@Data
public class LinkedIn {
 
	private String postTitle;
	private String postLink;
	private String postType;
	private String CampaignName;
	private String postedBy;
	private Date createdDate;
	private Date campaignStartDate;	
	private Date campaignEndDate;	
	private String audience;	
	private double impressions;	
	private double videoViews;	
	private double offsiteViews;	
	private double clicks;	
	private double cTR;	
	private double likes;	
	private double comments;	
	private double reposts;	
	private double follows;	
	private double engagementRate;	
	private String contentType;
}
