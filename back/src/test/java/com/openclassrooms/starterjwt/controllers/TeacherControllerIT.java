package com.openclassrooms.starterjwt.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;

/**
 * Integration tests on {@link TeacherController} class.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerIT {

  @MockBean
  private TeacherMapper teacherMapper;

  @MockBean
  private TeacherService teacherService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private Teacher teacher;
  private TeacherDto teacherDto;

  /**
   * Creates a teacher and its DTO before each test.
   */
  @BeforeEach
  private void init() {
    teacher = Teacher.builder().id(1L).lastName("Dupont").firstName("Jane").build();

    teacherDto = new TeacherDto();
    teacherDto.setId(1L);
    teacherDto.setLastName("Dupont");
    teacherDto.setFirstName("Jane");
  }

  /**
   * Tests that findAll method returns a 200 response with all the teachers.
   * 
   * @throws Exception if simulated call to the end point fails.
   */
  @Test
  @WithMockUser
  public void findAll_shouldSucceedRequestWithAllTeachers() throws Exception {
    List<Teacher> teachers = Arrays.asList(teacher);
    List<TeacherDto> teacherDtos = Arrays.asList(teacherDto);

    when(teacherService.findAll()).thenReturn(teachers);
    when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(teacherDtos)));
  }

  /**
   * Tests that findById method returns a 200 response with a teacher with given
   * id.
   * 
   * @throws Exception if simulated call to the end point fails.
   */
  @Test
  @WithMockUser
  public void findById_shouldSucceedRequestWithATeacher() throws Exception {
    Long id = teacher.getId();

    when(teacherService.findById(id)).thenReturn(teacher);
    when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/" + id).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(teacherDto)));
  }

  /**
   * Tests that findById method returns a bad request response when the given id
   * is malformed.
   * 
   * @throws Exception if simulated call to the end point fails.
   */
  @Test
  @WithMockUser
  public void findById_shouldReturnBadRequest_whenMalformedId() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/one").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  /**
   * Tests that findById method returns a not found response when the teacher with
   * the given id doesn't exist.
   * 
   * @throws Exception if simulated call to the end point fails.
   */
  @Test
  @WithMockUser
  public void findById_shouldReturnNotFound_whenTeacherDoesntExist() throws Exception {
    Long id = 2L;

    when(teacherService.findById(id)).thenReturn(null);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/" + id).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }
}
