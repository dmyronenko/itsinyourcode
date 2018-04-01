package com.itsinyourcode.deadlock.service;

import com.itsinyourcode.deadlock.model.entity.Document;
import com.itsinyourcode.deadlock.repo.DocumentRepository;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {
	private final DocumentRepository documentRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Document generateName(Document document) {

		// Merge
		final Document attached = documentRepository.save(document);

		// Change name
		attached.setName(UUID.randomUUID().toString());

		return document;
	}
}
