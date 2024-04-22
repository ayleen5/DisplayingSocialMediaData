package com.tsfn.model;
import java.time.LocalDateTime;
import lombok.Data;



@Data
public class Facebook {
 
	private String postId;
	private String pageId;
	private String pageName;
	private String title;
 	private String description;
	private double durationSec;
	private LocalDateTime publishTime;	
	private String captionType;	
	private String permalink;	
	private Boolean isCrosspost;	
	private Boolean isShare;	
	private String postType;	
	private String languages;	
	private String customLabels;	
	private String fundedContentStatus;	
	private String dataComment;	
	private LocalDateTime date;	
	private double impressions;	
	private double reach;
	private String impressionsUnique_User;
	private double reactionsCommentsShares;
	private double reactions;
	private double comments;
	private double shares;
	private double totalClicks;
	private double otherClicks;
	private double photoClick; //Matched Audience Targeting Consumption
	private double linkClick;
	private double negativeFeedback;// from users: Hide all
	private double secondsViewed;
	private double averageSecondsViewed;
	private double estimatedEarnings;// Estimated earnings (USD)
	private double adCPM;// Ad CPM (USD)
	private double adImpressions;
    
}
