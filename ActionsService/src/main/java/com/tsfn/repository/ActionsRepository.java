package com.tsfn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tsfn.model.Category;
import com.tsfn.model.Action;

public interface ActionsRepository extends JpaRepository<Action, Integer> {
		
	 	Optional<Action> findByTitleAndCompanyId(String title, int companyId);
	    List<Action> findByCompanyId(int companyId);
	    List<Action> findByCompanyIdAndCategory(int companyId, Category category);
	    List<Action> findByCompanyIdAndPriceLessThanEqual(int companyId, double maxPrice);

}
