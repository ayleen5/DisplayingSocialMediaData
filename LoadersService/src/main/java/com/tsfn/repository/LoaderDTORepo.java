package com.tsfn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tsfn.model.LoaderDTO;

public interface LoaderDTORepo extends JpaRepository<LoaderDTO, Integer> {
}
