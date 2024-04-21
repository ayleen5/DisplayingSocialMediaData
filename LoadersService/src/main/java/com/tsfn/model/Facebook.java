package com.tsfn.model;
import java.time.LocalDateTime;
import lombok.Data;



@Data
public class Facebook {
 
	private String postId;
	private String pageId;
	private String pageName;
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
	private double secondVideoViews;	
	private double minuteVideoViews;	
	private double secondViewers;	
	private double minuteViewers;	
	private double secondVideoViewsFromBoostedPosts;	
	private double secondVideoViewsFromOrganicPosts;	
	private double reactions;//	comments and Shares
	private double comments;//	comments and Shares
	private double shares;
	private double secondsViewed;
	private double averageSecondsViewed;
	private double estimatedEarnings;// Estimated earnings (USD)
	private double adCPM;// Ad CPM (USD)
	private double adImpressions;
    
}
