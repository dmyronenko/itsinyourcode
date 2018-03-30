package com.itsinyourcode.deadlock.repo;

import com.itsinyourcode.deadlock.model.entity.Document;
import com.itsinyourcode.deadlock.model.entity.Image;
import com.itsinyourcode.deadlock.model.entity.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ParallelMergeDeadlockTest {

	@Autowired
	private DocumentRepository documentRepository;

	private List<Document> documents;

	@Before
	public void setUp() {
		// Create some initial data
		documents = IntStream.range(0, 5)
			.mapToObj(i -> Document.builder()
				.globalId(UUID.randomUUID().toString())
				.name("Document" + i)
				.image(Image.builder().path(UUID.randomUUID().toString()).build())
				.resource(Resource.builder().url(UUID.randomUUID().toString()).build())
				.build())
			.collect(Collectors.toList());
		documents.forEach(document -> document.getImages().forEach(image -> image.setDocument(document)));
		documents.forEach(document -> document.getResources().forEach(resource -> resource.setDocument(document)));

		documentRepository.saveAll(documents);
	}

	@Test
	public void multiThreadedMerge() {
		// Merge in parallel by adding image to eachh document
		documents.stream().parallel()
			.forEach(document -> {
				final ArrayList<Image> images = new ArrayList<>(document.getImages());
				images.add(Image.builder().path(UUID.randomUUID().toString()).document(document).build());
				document.setImages(images);
				documentRepository.save(document);
			});
	}
}