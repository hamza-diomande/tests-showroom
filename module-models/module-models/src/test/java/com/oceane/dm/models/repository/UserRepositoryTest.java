package com.oceane.dm.models.repository;

import com.oceane.dm.models.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Création d'un utilisateur de test
        testUser = new User();
        testUser.setIdentifier("user123");
        testUser.setPassword("passwordHash");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("johndoe@example.com");

        // Enregistrement de l'utilisateur dans la base de données
        repository.save(testUser);
    }

    @Test
    void testFindById() {
        // Act
        Optional<User> foundUser = repository.findById(testUser.getId());

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(testUser.getId(), foundUser.get().getId());
        assertEquals(testUser.getIdentifier(), foundUser.get().getIdentifier());
    }

    @Test
    void testFindByIdentifier() {
        // Act
        Optional<User> foundUser = repository.findByIdentifier("user123");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(testUser.getIdentifier(), foundUser.get().getIdentifier());
    }

    @Test
    void testFindByEmail() {
        // Act
        Optional<User> foundUser = repository.findByEmail("johndoe@example.com");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(testUser.getEmail(), foundUser.get().getEmail());
    }

    @Test
    void testNotFoundById() {
        // Act
        Optional<User> foundUser = repository.findById(-1L);

        // Assert
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testNotFoundByIdentifier() {
        // Act
        Optional<User> foundUser = repository.findByIdentifier("nonExistentUser");

        // Assert
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testNotFoundByEmail() {
        // Act
        Optional<User> foundUser = repository.findByEmail("nonexistent@example.com");

        // Assert
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testUpdateUser() {
        // Act
        testUser.setLastName("Smith");
        repository.save(testUser);

        // Retrieve and Assert
        User updatedUser = repository.findById(testUser.getId()).orElse(null);
        assertNotNull(updatedUser);
        assertEquals("Smith", updatedUser.getLastName());
    }

    @Test
    void testDeleteUser() {
        // Act
        repository.delete(testUser);

        // Assert
        Optional<User> foundUser = repository.findById(testUser.getId());
        assertFalse(foundUser.isPresent());
    }
}
