package com.tsfn.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tsfn.model.LoaderDTO;

public interface LoaderDTORepo extends JpaRepository<LoaderDTO, Integer> {

	LoaderDTO findByAccountLoaderAndTimestamp(String userId, LocalDateTime fileTimestamp);

	LoaderDTO findByAccountLoaderAndTimestampAndPostId(String userId, LocalDateTime fileTimestamp, String string);
}
