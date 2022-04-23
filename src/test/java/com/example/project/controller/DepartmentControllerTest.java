package com.example.project.controller;

import com.example.project.model.Department;
import com.example.project.service.DepartmentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.Model;

import static com.example.project.controller.DepartmentController.ALL_DEPARTMENTS;
import static com.example.project.controller.DepartmentController.REDIRECT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Test for {@link com.example.project.controller.DepartmentController}
 */
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("h2")
public class DepartmentControllerTest {

    @Mock
    Model model;

    @Mock
    DepartmentServiceImpl departmentService;

    @InjectMocks
    DepartmentController departmentController;

    @BeforeEach
    void setUp() {
        departmentController = new DepartmentController();
        departmentController.setDepartmentService(departmentService);
    }

    @Test
    public void deleteDepartment() {
        doNothing().when(departmentService).deleteDepartmentById(1L);

        // test that the correct view name is displayed
        String view = departmentController.deleteDepartment(1L);
        assertEquals(REDIRECT + ALL_DEPARTMENTS, view);
        ArgumentCaptor<Department> argumentCaptor = ArgumentCaptor.forClass(Department.class);
    }

    @Test
    public void getById() {
        Department department = new Department();
        department.setId(1L);
        department.setName("Sectie");
        when(departmentService.getDepartmentById(1L)).thenReturn(department);

        // test that the correct view name is displayed
        String view = departmentController.getById("1", model);
        assertEquals("department_info", view);
        ArgumentCaptor<Department> argumentCaptor = ArgumentCaptor.forClass(Department.class);

        // test that the object was added with the "department" attribute
        verify(model, times(1)).addAttribute(eq("department"), argumentCaptor.capture());
        Department departmentCaptured = argumentCaptor.getValue();
        assertEquals(1L, departmentCaptured.getId());
        assertEquals("Sectie", departmentCaptured.getName());
    }
}
