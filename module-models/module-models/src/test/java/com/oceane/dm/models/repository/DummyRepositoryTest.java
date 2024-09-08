package com.oceane.dm.models.repository;

import com.oceane.dm.models.model.Dummy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class DummyRepositoryTest {

    @Autowired
    private DummyRepository repository;

    private Dummy testDummy;

    @BeforeEach
    void setUp() {
        // Création d'un Dummy de test
        testDummy = new Dummy();
        testDummy.setName("Test Dummy");

        // Enregistrement du Dummy dans la base de données
        repository.save(testDummy);
    }

    @Test
    void testFindById() {
        // Act
        Optional<Dummy> foundDummy = repository.findById(testDummy.getId());

        // Assert
        assertTrue(foundDummy.isPresent());
        assertEquals(testDummy.getId(), foundDummy.get().getId());
        assertEquals(testDummy.getName(), foundDummy.get().getName());
    }

    @Test
    void testFindByName() {
        // Act
        Dummy foundDummy = repository.findByName("Test Dummy");

        // Assert
        assertNotNull(foundDummy);
        assertEquals("Test Dummy", foundDummy.getName());
    }

    @Test
    void testNotFoundById() {
        // Act
        Optional<Dummy> foundDummy = repository.findById(-1L);

        // Assert
        assertFalse(foundDummy.isPresent());
    }

    @Test
    void testUpdateDummy() {
        // Act
        testDummy.setName("Updated Dummy");
        repository.save(testDummy);

        // Retrieve and Assert
        Dummy updatedDummy = repository.findById(testDummy.getId()).orElse(null);
        assertNotNull(updatedDummy);
        assertEquals("Updated Dummy", updatedDummy.getName());
    }

    @Test
    void testDeleteDummy() {
        // Act
        repository.delete(testDummy);

        // Assert
        Optional<Dummy> foundDummy = repository.findById(testDummy.getId());
        assertFalse(foundDummy.isPresent());
    }
}
