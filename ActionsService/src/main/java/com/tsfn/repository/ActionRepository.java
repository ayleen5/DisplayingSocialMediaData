package com.tsfn.repository;

import java.sql.Time;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tsfn.model.Action;


public interface ActionRepository extends JpaRepository<Action, Integer> {

    List<Action> findAllByRunOnDayAndRunOnTime(int runOnDay, Time runOnTime); 
    List<Action> findAllByRunOnDay(int runOnDay);

}
