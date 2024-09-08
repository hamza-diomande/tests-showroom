package com.oceane.dm.document.repository;

import com.oceane.dm.document.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find a {@link User} for a given id
     * @param id the id
     * @return the user
     */
    Optional<User> findById(Long id);

    /**
     * Find a {@link User} for a given identifier
     * @param identifier the identifier
     * @return the user
     */
    Optional<User> findByIdentifier(String identifier);

    /**
     * Find a {@link User} for a given email
     * @param email the email
     * @return the user
     */
    Optional<User> findByEmail(String email);
}
