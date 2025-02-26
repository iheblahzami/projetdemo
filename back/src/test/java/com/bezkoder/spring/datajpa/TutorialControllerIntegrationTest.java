package com.bezkoder.spring.datajpa;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bezkoder.spring.datajpa.model.Tutorial;
import com.bezkoder.spring.datajpa.repository.TutorialRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TutorialControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TutorialRepository tutorialRepository;

    private Tutorial tutorial;

    @BeforeEach
    void setUp() {
        tutorialRepository.deleteAll(); // Clear the database before each test
        tutorial = new Tutorial("Test Title", "Test Description", true);
        tutorialRepository.save(tutorial);
    }

    @Test
    void testGetAllTutorials() throws Exception {
        mockMvc.perform(get("/api/tutorials"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Title"));
    }

    @Test
    void testGetTutorialById() throws Exception {
        mockMvc.perform(get("/api/tutorials/{id}", tutorial.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    @Test
    void testCreateTutorial() throws Exception {
        Tutorial newTutorial = new Tutorial("New Title", "New Description", false);

        mockMvc.perform(post("/api/tutorials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newTutorial)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Title"));
    }

    @Test
    void testUpdateTutorial() throws Exception {
        tutorial.setTitle("Updated Title");

        mockMvc.perform(put("/api/tutorials/{id}", tutorial.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(tutorial)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void testDeleteTutorial() throws Exception {
        mockMvc.perform(delete("/api/tutorials/{id}", tutorial.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindByPublished() throws Exception {
        mockMvc.perform(get("/api/tutorials/published"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Title"));
    }
}