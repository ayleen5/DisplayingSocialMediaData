package com.tsfn.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "linkedIn")
public class LinkedIn {
	
	
	private LocalDateTime timestamp;
	@Id
	private String postLink;
	private String contentType;
    private int impressions;
    private int vEO_oV; // viewsExcludingOffsite + offsiteViews
    private int clicks;
    private double clickThroughRate;
    private int likes;
    private int comments;
    private int reposts;
    private double engagementRate;
    
   
    
//    @Column(name = "ctr")
//    private double ctr;
//    
//    @Column(name = "engagement_rate")
//    private double engagementRate;
//    
//    @PrePersist
//    public void calculateMetrics() {
//        if (impressions > 0) {
//            ctr = (double) totalClicks / impressions;
//        } else {
//            ctr = 0.0;
//        }
//        
//        if (reach > 0) {
//            engagementRate = (double) (reactions + comments + shares) / reach;
//        } else {
//            engagementRate = 0.0;
//        }
//    }


}
