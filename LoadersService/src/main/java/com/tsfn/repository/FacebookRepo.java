package com.tsfn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tsfn.dto.LoaderDTO;
import com.tsfn.model.Facebook;

public interface FacebookRepo extends JpaRepository<Facebook, Integer>{

	LoaderDTO save(LoaderDTO facebookData);

}
