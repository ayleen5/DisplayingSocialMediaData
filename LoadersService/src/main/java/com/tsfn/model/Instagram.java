package com.tsfn.model;

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
@Table(name = "instagram")
public class Instagram {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int postId;

	private LocalDateTime  timestamp;
	private String Posttype;
	private int Impressions;
	private int Reach;
	private int Saves;
	private int likes;
	private int comments;
	private int shares;

	@Column(name = "ctr")
	private double CTR;

	@Column(name = "engagement_rate")
	private double Engagementrate;

	@PrePersist
	@PreUpdate
	public void calculateMetrics() {
		if (Impressions > 0) {
			CTR = (double) Saves / Impressions;
		} else {
			CTR = 0.0;
		}

		if (Reach > 0) {
			Engagementrate = (double) (likes + comments + shares) / Reach;
		} else {
			Engagementrate = 0.0;
		}
	}
}
