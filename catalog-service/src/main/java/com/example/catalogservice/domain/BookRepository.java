package com.example.catalogservice.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface BookRepository extends CrudRepository<Book,Long> {
	Optional<Book> findByIsbn(String isbn);
	boolean existsByIsbn(String isbn);

	@Transactional
	void deleteByIsbn(String isbn);

}