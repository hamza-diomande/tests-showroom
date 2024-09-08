/*package com.oceane.dm.feedback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Entity class for users
 *//*
@Entity
@Table(name = "app_user")
public class User implements UserDetails {
    private static final long serialVersionUID = 1L;

    public enum Role {
        USER_ROLE(Set.of("ROLE_USER")),
        ADMIN_ROLE(Set.of("ROLE_USER", "ROLE_ADMIN"));

        private final Set<String> authorities;

        Role(Set<String> authorities) {
            this.authorities = authorities;
        }

        public Set<String> getAuthorities() {
            return Collections.unmodifiableSet(authorities);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identifier", length = 50, nullable = false, unique = true)
    private String identifier;

    @JsonIgnore
    @Column(name = "password_hash", length = 100, nullable = false)
    private String password;

    @Column(name = "firstName", length = 30, nullable = false)
    private String firstName;

    @Column(name = "lastName", length = 30, nullable = false)
    private String lastName;

    @Column(name = "company", length = 50)
    private String company;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "role", length = 20, nullable = false)
    private Role role = Role.USER_ROLE;

    /**
     * Gets id.
     *
     * @return the id
     *//*
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     *//*
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets identifier.
     *
     * @return the identifier
     *//*
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets identifier.
     *
     * @param identifier the identifier
     *//*
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     *//*
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets first name.
     *
     * @return the first name
     *//*
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets first name.
     *
     * @param firstName the first name
     *//*
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets last name.
     *
     * @return the last name
     *//*
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets last name.
     *
     * @param lastName the last name
     *//*
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets company.
     *
     * @return the company
     *//*
    public String getCompany() {
        return company;
    }

    /**
     * Sets company.
     *
     * @param company the company
     *//*
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * Gets email.
     *
     * @return the email
     *//*
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     *//*
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets role.
     *
     * @return the role
     *//*
    public Role getRole() {
        return role;
    }

    /**
     * Sets role.
     *
     * @param role the role
     *//*
    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && Objects.equals(identifier, user.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifier);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", identifier='" + identifier + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", company='" + company + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
*/