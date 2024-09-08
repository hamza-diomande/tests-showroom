package com.oceane.dm.models.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DummyTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        Dummy dummy = new Dummy();
        Long expectedId = 1L;
        String expectedName = "Test Name";

        // Act
        dummy.setId(expectedId);
        dummy.setName(expectedName);

        // Assert
        assertEquals(expectedId, dummy.getId());
        assertEquals(expectedName, dummy.getName());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        Dummy dummy1 = new Dummy();
        dummy1.setId(1L);
        dummy1.setName("Test Name");

        Dummy dummy2 = new Dummy();
        dummy2.setId(1L);
        dummy2.setName("Test Name");

        Dummy dummy3 = new Dummy();
        dummy3.setId(2L);
        dummy3.setName("Another Name");

        // Act & Assert
        // Equality between same data objects
        assertEquals(dummy1, dummy2);
        assertEquals(dummy1.hashCode(), dummy2.hashCode());

        // Non-equality between different data objects
        assertNotEquals(dummy1, dummy3);
        assertNotEquals(dummy1.hashCode(), dummy3.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        Dummy dummy = new Dummy();
        dummy.setId(1L);
        dummy.setName("Test Name");

        String expected = "Dummy{id=1, name='Test Name'}";

        // Act
        String actual = dummy.toString();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testNotEqualsWithDifferentClass() {
        // Arrange
        Dummy dummy = new Dummy();
        dummy.setId(1L);
        dummy.setName("Test Name");

        String differentClassObject = "I am a string";

        // Act & Assert
        assertNotEquals(dummy, differentClassObject);
    }

    @Test
    void testEqualsWithSameInstance() {
        // Arrange
        Dummy dummy = new Dummy();
        dummy.setId(1L);
        dummy.setName("Test Name");

        // Act & Assert
        assertEquals(dummy, dummy); // Should be equal to itself
    }

    @Test
    void testEqualsWithNull() {
        // Arrange
        Dummy dummy = new Dummy();
        dummy.setId(1L);
        dummy.setName("Test Name");

        // Act & Assert
        assertNotEquals(dummy, null); // Should not be equal to null
    }
}
