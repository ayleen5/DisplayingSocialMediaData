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
@Table(name = "instagram")
public class Instagram {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int postId;

	private Time timestamp;
	private String ContentType;
	private int Impressions;
	private int Views;
	private int Clicks;
	private int likes;
	private int comments;
	private int shares;

	@Column(name = "ctr")
	private double CTR;

	@Column(name = "engagement_rate")
	private double Engagementrate;

	@PrePersist
	public void calculateMetrics() {
		if (Impressions > 0) {
			CTR = (double) Clicks / Impressions;
		} else {
			CTR = 0.0;
		}

		if (Views > 0) {
			Engagementrate = (double) (likes + comments + shares) / Views;
		} else {
			Engagementrate = 0.0;
		}
	}
}
