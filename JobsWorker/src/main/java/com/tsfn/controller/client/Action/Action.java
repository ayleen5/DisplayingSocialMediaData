package com.tsfn.controller.client.Action;
 
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
 
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
public class Action {
 
    
    private int id;
    private String account_id;
    private String name;
    private Date created_date; 
    private String created_by;
    private Hashtable<String,ArrayList<String>> conditions;
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