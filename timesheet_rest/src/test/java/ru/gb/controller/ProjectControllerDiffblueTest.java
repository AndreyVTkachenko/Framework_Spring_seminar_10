package ru.gb.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.gb.model.Project;
import ru.gb.service.ProjectService;

@ContextConfiguration(classes = {ProjectController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class ProjectControllerDiffblueTest {
    @Autowired
    private ProjectController projectController;

    @MockBean
    private ProjectService projectService;

    /**
     * Method under test: {@link ProjectController#findAll()}
     */
    @Test
    void testFindAll() throws Exception {
        // Arrange
        when(projectService.findAll()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/projects");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(projectController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ProjectController#create(Project)}
     */
    @Test
    void testCreate() throws Exception {
        // Arrange
        Project project = new Project();
        project.setId(1L);
        project.setName("Name");
        when(projectService.create(Mockito.<Project>any())).thenReturn(project);

        Project project2 = new Project();
        project2.setId(1L);
        project2.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(project2);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(projectController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":1,\"name\":\"Name\"}"));
    }

    /**
     * Method under test: {@link ProjectController#delete(Long)}
     */
    @Test
    void testDelete() throws Exception {
        // Arrange
        doNothing().when(projectService).delete(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/projects/{id}", 1L);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(projectController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link ProjectController#find(Long)}
     */
    @Test
    void testFind() throws Exception {
        // Arrange
        Project project = new Project();
        project.setId(1L);
        project.setName("Name");
        Optional<Project> ofResult = Optional.of(project);
        when(projectService.findById(Mockito.<Long>any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/projects/{id}", 1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(projectController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":1,\"name\":\"Name\"}"));
    }

    /**
     * Method under test: {@link ProjectController#findTimesheets(Long)}
     */
    @Test
    void testFindTimesheets() throws Exception {
        // Arrange
        when(projectService.getTimesheets(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/projects/{id}/timesheets", 1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(projectController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ProjectController#findTimesheets(Long)}
     */
    @Test
    void testFindTimesheets2() throws Exception {
        // Arrange
        when(projectService.getTimesheets(Mockito.<Long>any())).thenThrow(new NoSuchElementException("foo"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/projects/{id}/timesheets", 1L);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(projectController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
