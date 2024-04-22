package com.tsfn.model;
 
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
 
import jakarta.persistence.Entity;
 
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name= "actions")
@Data
public class Action {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String account_id;
    private String name;
    private Date created_date; 
    private String created_by;
    private Hashtable<String,ArrayList<String>> conditions;
    @Enumerated(EnumType.STRING)
    private ActionsType action_type;
    private Time run_on_time;
    private int run_on_day;
    private String notification_text;
    private String recipient; 
    private boolean isEnable;
    private boolean is_deleted;
    private Date last_update;
    private Date last_run;
}