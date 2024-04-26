package com.tsfn.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tsfn.model.Loader;

public interface LoaderRepository extends JpaRepository<Loader, Integer> {

	Loader findByAccountLoaderAndTimestamp(String userId, LocalDateTime fileTimestamp);

	Loader findByAccountLoaderAndTimestampAndPostId(String userId, LocalDateTime fileTimestamp, String string);
	
	List<Loader> findAllByAccountLoader(String accountLoader);
	
	List<Loader> findAllByImpressionsGreaterThanEqual(int threshold);
}
