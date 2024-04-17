package com.tsfn.model;

import java.sql.Time;
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
@Table(name = "facebook")
public class Facebook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;

    private Time Timestamp;
    private String ContentType;
    private int Impressions;
    private int Views;
    private int Clicks;
    private int Likes;
    private int Comments;
    private int Shares;
    
    private double CTR;
    
    private double Engagementrate;
    
    @PrePersist
    public void calculateMetrics() {
        if (Impressions > 0) {
        	CTR = (double) Clicks / Impressions;
        } else {
        	CTR = 0.0;
        }
        
        if (Views > 0) {
        	Engagementrate = (double) (Likes + Comments + Shares) / Views;
        } else {
        	Engagementrate = 0.0;
        }
    }
    
}
