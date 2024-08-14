package com.openclassrooms.starterjwt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
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

}
