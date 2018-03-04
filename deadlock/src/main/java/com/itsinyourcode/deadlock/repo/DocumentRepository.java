package com.itsinyourcode.deadlock.repo;

import com.itsinyourcode.deadlock.model.entity.Document;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends CrudRepository<Document, Long> {
}
