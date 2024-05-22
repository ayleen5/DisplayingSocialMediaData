package com.tsfn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tsfn.model.Action;
import java.sql.Time;
import java.util.List;

public interface ActionRepository extends JpaRepository<Action, Integer> {
    List<Action> findAllByRunOnDayAndRunOnTime(int runOnDay, Time runOnTime); 
    List<Action> findAllByRunOnDay(int runOnDay);
}
