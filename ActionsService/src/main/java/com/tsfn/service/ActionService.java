package com.tsfn.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsfn.model.Action;
import com.tsfn.repository.ActionRepository;


@Service
public class ActionService {
	@Autowired
    private  ActionRepository actionRepository;

    
   

    public void save(Action action) {
        actionRepository.save(action); // Create or update
    }

    public Optional<Action> getById(int id) {
        return actionRepository.findById(id);
    }

    public List<Action> getAll() {
        return actionRepository.findAll();
    }

    public void update(Action action) {
        actionRepository.save(action); // Update
    }

    public void delete(int id) {
        actionRepository.deleteById(id);
    }
}
