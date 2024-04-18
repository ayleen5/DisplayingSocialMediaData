package com.tsfn.model;

import java.sql.Time;
import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;



@Entity
@Data
@Table(name = "facebook")
public class Facebook {

	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;
    private LocalDateTime  Timestamp;
    private String Posttype;
    private int Impressions;
    private int Reach;
    private int Totalclicks;
    private int Reactions;
    private int Comments;
    private int Shares;
    private double CTR;
    private double Engagementrate;
    
	@PrePersist
	@PreUpdate
    public void calculateMetrics() {
        if (Impressions > 0) {
        	CTR = (double) Totalclicks / Impressions;
        } else {
        	CTR = 0.0;
        }
        
        if (Reach > 0) {
        	Engagementrate = (double) (Reactions + Comments + Shares) / Reach;
        } else {
        	Engagementrate = 0.0;
        }
    }
    
}
