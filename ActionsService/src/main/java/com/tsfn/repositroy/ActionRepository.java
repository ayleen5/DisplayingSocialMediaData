package com.tsfn.repositroy;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tsfn.model.Action;

public interface ActionRepository extends JpaRepository<Action, Integer> {
	public Action findByEmailAndPassword(String email, String password);
	public boolean existsByEmail(String email);
}
