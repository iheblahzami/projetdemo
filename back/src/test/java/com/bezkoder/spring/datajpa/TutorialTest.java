package com.bezkoder.spring.datajpa;

import com.bezkoder.spring.datajpa.model.Tutorial;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TutorialTest {

    @Test
    void testTutorialEntity() {
        // Arrange
        Tutorial tutorial = new Tutorial("Test Title", "Test Description", true);
        tutorial.setId(1L);

        // Act & Assert
        assertEquals(1L, tutorial.getId());
        assertEquals("Test Title", tutorial.getTitle());
        assertEquals("Test Description", tutorial.getDescription());
        assertTrue(tutorial.isPublished());

        // Test toString()
        String expectedToString = "Tutorial [id=1, title=Test Title, desc=Test Description, published=true]";
        assertEquals(expectedToString, tutorial.toString());
    }
}