package com.itsinyourcode.deadlock.repo;

import com.itsinyourcode.deadlock.service.DocumentFacade;
import com.itsinyourcode.deadlock.service.DocumentService;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import(value = { DocumentService.class, DocumentFacade.class })
public class DeadlockPropagationRequiresNewTest {
	@Autowired
	private DocumentFacade documentFacade;

	@Test
	public void deadlockPropagationRequiresNew() {
		documentFacade.processDocument(UUID.randomUUID().toString());
	}
}
