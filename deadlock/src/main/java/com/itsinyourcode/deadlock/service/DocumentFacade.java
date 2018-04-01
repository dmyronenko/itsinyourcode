package com.itsinyourcode.deadlock.service;

import com.itsinyourcode.deadlock.model.entity.Document;
import com.itsinyourcode.deadlock.repo.DocumentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentFacade {

	private final DocumentService documentService;
	private final DocumentRepository documentRepository;

	@Transactional
	public Document processDocument(final String globalId) {
		final Document build = Document.builder()
			.globalId(globalId)
			.build();

		final Document saved = documentRepository.save(build);

		return documentService.generateName(saved);
	}
}
