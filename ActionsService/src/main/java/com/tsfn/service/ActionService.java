package com.tsfn.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsfn.model.Action;
import com.tsfn.repositroy.ActionRepository;
import com.tsfn.service.exception.ActionAlreadyExistsException;
import com.tsfn.service.exception.ActionNotFoundException;

@Service
public class ActionService {
	@Autowired
	ActionRepository ActionRepository ;
	
	public Action login(String email, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	public Action getActionById(int ActionId) throws ActionNotFoundException {
        Optional<Action> Action = ActionRepository.findById(ActionId);
        if (Action == null) {
            throw new ActionNotFoundException("Action with ID " + ActionId + " not found");
        }
        return Action.get();
    }

	public void deleteAction(int ActionId) throws ActionNotFoundException {
        Action Action = ActionRepository.findById(ActionId)
                .orElseThrow(() -> new ActionNotFoundException("Action with ID " + ActionId + " not found"));
        ActionRepository.delete(Action);
    }

    public Action updateAction(Action Action) throws ActionNotFoundException {
        int ActionId = Action.getId();
        if (!ActionRepository.existsById(ActionId)) {
            throw new ActionNotFoundException("Action with ID " + ActionId + " not found");
        }
        return ActionRepository.save(Action);
    }

    public Action createAction(Action Action) throws ActionAlreadyExistsException {
        String email = Action.getEmail();
        if (ActionRepository.existsByEmail(email)) {
            throw new ActionAlreadyExistsException("Action with email " + email + " already exists");
        }
        return ActionRepository.save(Action);
    }


}
