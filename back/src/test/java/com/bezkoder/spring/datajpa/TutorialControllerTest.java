package com.bezkoder.spring.datajpa;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bezkoder.spring.datajpa.controller.TutorialController;
import com.bezkoder.spring.datajpa.model.Tutorial;
import com.bezkoder.spring.datajpa.repository.TutorialRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class TutorialControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TutorialRepository tutorialRepository;

    @InjectMocks
    private TutorialController tutorialController;

    private Tutorial tutorial;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tutorialController).build();
        tutorial = new Tutorial("Test Title", "Test Description", true);
        tutorial.setId(1L);
    }

    @Test
    void testGetAllTutorials() throws Exception {
        // Arrange
        List<Tutorial> tutorials = new ArrayList<>();
        tutorials.add(tutorial);
        when(tutorialRepository.findAll()).thenReturn(tutorials);

        // Act & Assert
        mockMvc.perform(get("/api/tutorials"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Title"));
    }

    @Test
    void testGetTutorialById_Found() throws Exception {
        // Arrange
        when(tutorialRepository.findById(1L)).thenReturn(Optional.of(tutorial));

        // Act & Assert
        mockMvc.perform(get("/api/tutorials/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    @Test
    void testGetTutorialById_NotFound() throws Exception {
        // Arrange
        when(tutorialRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/tutorials/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateTutorial() throws Exception {
        // Arrange
        when(tutorialRepository.save(any(Tutorial.class))).thenReturn(tutorial);

        // Act & Assert
        mockMvc.perform(post("/api/tutorials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(tutorial)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    @Test
    void testUpdateTutorial_Found() throws Exception {
        // Arrange
        when(tutorialRepository.findById(1L)).thenReturn(Optional.of(tutorial));
        when(tutorialRepository.save(any(Tutorial.class))).thenReturn(tutorial);

        // Act & Assert
        mockMvc.perform(put("/api/tutorials/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(tutorial)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    @Test
    void testUpdateTutorial_NotFound() throws Exception {
        // Arrange
        when(tutorialRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/api/tutorials/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(tutorial)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteTutorial() throws Exception {
        // Arrange
        doNothing().when(tutorialRepository).deleteById(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/tutorials/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteAllTutorials() throws Exception {
        // Arrange
        doNothing().when(tutorialRepository).deleteAll();

        // Act & Assert
        mockMvc.perform(delete("/api/tutorials"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindByPublished() throws Exception {
        // Arrange
        List<Tutorial> tutorials = new ArrayList<>();
        tutorials.add(tutorial);
        when(tutorialRepository.findByPublished(true)).thenReturn(tutorials);

        // Act & Assert
        mockMvc.perform(get("/api/tutorials/published"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Title"));
    }
}