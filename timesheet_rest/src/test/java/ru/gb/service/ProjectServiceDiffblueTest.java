package ru.gb.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.gb.model.Project;
import ru.gb.model.Timesheet;
import ru.gb.repository.ProjectRepository;
import ru.gb.repository.TimesheetRepository;

@ContextConfiguration(classes = {ProjectService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class ProjectServiceDiffblueTest {
    @MockBean
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @MockBean
    private TimesheetRepository timesheetRepository;

    /**
     * Method under test: {@link ProjectService#findById(Long)}
     */
    @Test
    void testFindById() {
        // Arrange
        Project project = new Project();
        project.setId(1L);
        project.setName("Name");
        Optional<Project> ofResult = Optional.of(project);
        when(projectRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act
        Optional<Project> actualFindByIdResult = projectService.findById(1L);

        // Assert
        verify(projectRepository).findById(eq(1L));
        assertSame(ofResult, actualFindByIdResult);
    }

    /**
     * Method under test: {@link ProjectService#findById(Long)}
     */
    @Test
    void testFindById2() {
        // Arrange
        when(projectRepository.findById(Mockito.<Long>any())).thenThrow(new NoSuchElementException("foo"));

        // Act and Assert
        assertThrows(NoSuchElementException.class, () -> projectService.findById(1L));
        verify(projectRepository).findById(eq(1L));
    }

    /**
     * Method under test: {@link ProjectService#findAll()}
     */
    @Test
    void testFindAll() {
        // Arrange
        ArrayList<Project> projectList = new ArrayList<>();
        when(projectRepository.findAll()).thenReturn(projectList);

        // Act
        List<Project> actualFindAllResult = projectService.findAll();

        // Assert
        verify(projectRepository).findAll();
        assertTrue(actualFindAllResult.isEmpty());
        assertSame(projectList, actualFindAllResult);
    }

    /**
     * Method under test: {@link ProjectService#findAll()}
     */
    @Test
    void testFindAll2() {
        // Arrange
        when(projectRepository.findAll()).thenThrow(new NoSuchElementException("foo"));

        // Act and Assert
        assertThrows(NoSuchElementException.class, () -> projectService.findAll());
        verify(projectRepository).findAll();
    }

    /**
     * Method under test: {@link ProjectService#create(Project)}
     */
    @Test
    void testCreate() {
        // Arrange
        Project project = new Project();
        project.setId(1L);
        project.setName("Name");
        when(projectRepository.save(Mockito.<Project>any())).thenReturn(project);

        Project project2 = new Project();
        project2.setId(1L);
        project2.setName("Name");

        // Act
        Project actualCreateResult = projectService.create(project2);

        // Assert
        verify(projectRepository).save(isA(Project.class));
        assertSame(project, actualCreateResult);
    }

    /**
     * Method under test: {@link ProjectService#create(Project)}
     */
    @Test
    void testCreate2() {
        // Arrange
        when(projectRepository.save(Mockito.<Project>any())).thenThrow(new NoSuchElementException("foo"));

        Project project = new Project();
        project.setId(1L);
        project.setName("Name");

        // Act and Assert
        assertThrows(NoSuchElementException.class, () -> projectService.create(project));
        verify(projectRepository).save(isA(Project.class));
    }

    /**
     * Method under test: {@link ProjectService#delete(Long)}
     */
    @Test
    void testDelete() {
        // Arrange
        doNothing().when(projectRepository).deleteById(Mockito.<Long>any());

        // Act
        projectService.delete(1L);

        // Assert that nothing has changed
        verify(projectRepository).deleteById(eq(1L));
    }

    /**
     * Method under test: {@link ProjectService#delete(Long)}
     */
    @Test
    void testDelete2() {
        // Arrange
        doThrow(new NoSuchElementException("foo")).when(projectRepository).deleteById(Mockito.<Long>any());

        // Act and Assert
        assertThrows(NoSuchElementException.class, () -> projectService.delete(1L));
        verify(projectRepository).deleteById(eq(1L));
    }

    /**
     * Method under test: {@link ProjectService#getTimesheets(Long)}
     */
    @Test
    void testGetTimesheets() {
        // Arrange
        Project project = new Project();
        project.setId(1L);
        project.setName("Name");
        Optional<Project> ofResult = Optional.of(project);
        when(projectRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        ArrayList<Timesheet> timesheetList = new ArrayList<>();
        when(timesheetRepository.findByProjectId(Mockito.<Long>any())).thenReturn(timesheetList);

        // Act
        List<Timesheet> actualTimesheets = projectService.getTimesheets(1L);

        // Assert
        verify(projectRepository).findById(eq(1L));
        verify(timesheetRepository).findByProjectId(eq(1L));
        assertTrue(actualTimesheets.isEmpty());
        assertSame(timesheetList, actualTimesheets);
    }

    /**
     * Method under test: {@link ProjectService#getTimesheets(Long)}
     */
    @Test
    void testGetTimesheets2() {
        // Arrange
        Project project = new Project();
        project.setId(1L);
        project.setName("Name");
        Optional<Project> ofResult = Optional.of(project);
        when(projectRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(timesheetRepository.findByProjectId(Mockito.<Long>any())).thenThrow(new NoSuchElementException("foo"));

        // Act and Assert
        assertThrows(NoSuchElementException.class, () -> projectService.getTimesheets(1L));
        verify(projectRepository).findById(eq(1L));
        verify(timesheetRepository).findByProjectId(eq(1L));
    }

    /**
     * Method under test: {@link ProjectService#getTimesheets(Long)}
     */
    @Test
    void testGetTimesheets3() {
        // Arrange
        Optional<Project> emptyResult = Optional.empty();
        when(projectRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(NoSuchElementException.class, () -> projectService.getTimesheets(1L));
        verify(projectRepository).findById(eq(1L));
    }
}
