package com.oceane.dm.models.repository;

import com.oceane.dm.models.model.TwoFactorUser;
import com.oceane.dm.models.model.TwoFactorUser.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class TwoFactorUserRepositoryTest {

    @Autowired
    private TwoFactorUserRepository repository;

    private TwoFactorUser testUser;

    @BeforeEach
    void setUp() {
        // Création d'un utilisateur de test avec un email unique pour chaque test
        testUser = new TwoFactorUser();
        testUser.setIdentifier("user" + System.currentTimeMillis());  // Assure une unicité
        testUser.setPassword("passwordHash");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setCompany("Acme Corp");
        testUser.setEmail("johndoe" + System.currentTimeMillis() + "@example.com");  // Assure une unicité
        testUser.setRole(Role.USER_ROLE);
        testUser.setAuth2Fa(true);
        testUser.setSecret("secretKey");

        // Enregistrement de l'utilisateur dans la base de données
        repository.save(testUser);
    }

    @Test
    void testFindById() {
        // Act
        Optional<TwoFactorUser> foundUser = repository.findById(testUser.getId());

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(testUser.getId(), foundUser.get().getId());
    }

    @Test
    void testFindByIdentifier() {
        // Act
        Optional<TwoFactorUser> foundUser = repository.findByIdentifier(testUser.getIdentifier());

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(testUser.getIdentifier(), foundUser.get().getIdentifier());
    }

    @Test
    void testFindByEmail() {
        // Act
        Optional<TwoFactorUser> foundUser = repository.findByEmail(testUser.getEmail());

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(testUser.getEmail(), foundUser.get().getEmail());
    }

    @Test
    void testNotFoundByIdentifier() {
        // Act
        Optional<TwoFactorUser> foundUser = repository.findByIdentifier("nonExistentUser");

        // Assert
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testNotFoundByEmail() {
        // Act
        Optional<TwoFactorUser> foundUser = repository.findByEmail("nonexistent@example.com");

        // Assert
        assertFalse(foundUser.isPresent());
    }
}
