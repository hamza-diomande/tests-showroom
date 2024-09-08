package com.oceane.dm.document.model;

import com.oceane.dm.document.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setIdentifier("user123");
        user.setPassword("password123");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setCompany("Example Corp");
        user.setEmail("john.doe@example.com");
        user.setRole(User.Role.USER_ROLE);
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(1L, user.getId());
        assertEquals("user123", user.getIdentifier());
        assertEquals("password123", user.getPassword());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("Example Corp", user.getCompany());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals(User.Role.USER_ROLE, user.getRole());
    }

    @Test
    public void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    public void testGetUsername() {
        assertEquals("john.doe@example.com", user.getUsername());
    }

    @Test
    public void testIsAccountNonExpired() {
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked() {
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired() {
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    public void testIsEnabled() {
        assertTrue(user.isEnabled());
    }

    @Test
    public void testEqualsAndHashCode() {
        User anotherUser = new User();
        anotherUser.setId(1L);
        anotherUser.setIdentifier("user123");

        assertEquals(user, anotherUser);
        assertEquals(user.hashCode(), anotherUser.hashCode());

        anotherUser.setId(2L);
        assertNotEquals(user, anotherUser);
        assertNotEquals(user.hashCode(), anotherUser.hashCode());
    }

    @Test
    public void testToString() {
        String expectedString = "User{id=1, identifier='user123', firstName='John', lastName='Doe', company='Example Corp', email='john.doe@example.com'}";
        assertEquals(expectedString, user.toString());
    }
}
