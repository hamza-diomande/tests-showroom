package com.oceane.dm.models.repository;

import com.oceane.dm.models.model.TwoFactorUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TwoFactorUserRepository extends JpaRepository<TwoFactorUser,Long> {
    /**
     * Find a {@link User} for a given id
     * @param id the id
     * @return the user
     */
    Optional<TwoFactorUser> findById(Long id);

    /**
     * Find a {@link User} for a given identifier
     * @param identifier the identifier
     * @return the user
     */
    Optional<TwoFactorUser> findByIdentifier(String identifier);

    /**
     * Find a {@link User} for a given email
     * @param email the email
     * @return the user
     */
    Optional<TwoFactorUser> findByEmail(String email);
}
