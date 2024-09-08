package com.oceane.dm.models.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setIdentifier("testUser");
        user.setPassword("securePassword");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setCompany("ExampleCorp");
        user.setRole(User.Role.USER_ROLE);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, user.getId());
        assertEquals("testUser", user.getIdentifier());
        assertEquals("securePassword", user.getPassword());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("ExampleCorp", user.getCompany());
        assertEquals(User.Role.USER_ROLE, user.getRole());
    }

    @Test
    void testAuthorities() {
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testEqualsAndHashCode() {
        User sameUser = new User();
        sameUser.setId(1L);
        sameUser.setIdentifier("testUser");

        User differentUser = new User();
        differentUser.setId(2L);
        differentUser.setIdentifier("anotherUser");

        assertEquals(user, sameUser);
        assertNotEquals(user, differentUser);

        assertEquals(user.hashCode(), sameUser.hashCode());
        assertNotEquals(user.hashCode(), differentUser.hashCode());
    }

    @Test
    void testUserDetailsMethods() {
        assertEquals(user.getEmail(), user.getUsername());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    void testToString() {
        String expected = "User{id=1, identifier='testUser', firstName='John', lastName='Doe', company='ExampleCorp', email='john.doe@example.com'}";
        assertEquals(expected, user.toString());
    }

    @Test
    void testRoleAuthorities() {
        user.setRole(User.Role.ADMIN_ROLE);
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertEquals(2, authorities.size());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }
}
