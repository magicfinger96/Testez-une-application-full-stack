package com.openclassrooms.starterjwt.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;
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
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;

/**
 * Integration tests on {@link SessionController} class.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerIT {

  @MockBean
  private SessionMapper sessionMapper;

  @MockBean
  private SessionService sessionService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private Session session;
  private SessionDto sessionDto;

  /**
   * Creates a session and its DTO before each test.
   */
  @BeforeEach
  private void init() {

    session = Session.builder().id(1L).name("Session name").date(new Date()).description("Description of the session")
        .teacher(null).users(null).build();

    sessionDto = new SessionDto();
    sessionDto.setId(1L);
    sessionDto.setName("Session name");
    sessionDto.setDate(new Date());
    sessionDto.setDescription("Description of the session");
    sessionDto.setTeacher_id(1L);
    sessionDto.setUsers(null);
  }

  /**
   * Tests that findAll method returns a 200 response with all the sessions.
   * 
   * @throws Exception if simulated call to the end point fails.
   */
  @Test
  @WithMockUser
  public void findAll_shouldSucceedRequestWithAllSessions() throws Exception {
    List<Session> sessions = Arrays.asList(session);
    List<SessionDto> sessionDtos = Arrays.asList(sessionDto);

    when(sessionService.findAll()).thenReturn(sessions);
    when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/session").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(sessionDtos)));
  }

  /**
   * Tests that findById returns a 200 response with a session with given id.
   * 
   * @throws Exception if simulated call to the end point fails.
   */
  @Test
  @WithMockUser
  public void findById_shouldSucceedRequestWithASession() throws Exception {

    Long id = session.getId();

    when(sessionService.getById(Long.valueOf(id))).thenReturn(session);
    when(sessionMapper.toDto(session)).thenReturn(sessionDto);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/session/" + id).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(sessionDto)));
  }

  /**
   * Tests that findById returns a bad request response when the given id is
   * malformed.
   * 
   * @throws Exception if simulated call to the end point fails.
   */
  @Test
  @WithMockUser
  public void findById_shouldReturnBadRequest_whenMalformedId() throws Exception {

    mockMvc.perform(MockMvcRequestBuilders.get("/api/session/one").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  /**
   * Tests that findById returns a not found response when the session with the
   * given id doesn't exist.
   * 
   * @throws Exception if simulated call to the end point fails.
   */
  @Test
  @WithMockUser
  public void findById_shouldReturnNotFound_whenSessionDoesntExist() throws Exception {

    Long id = 2L;

    when(sessionService.getById(id)).thenReturn(null);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/session/" + id).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  /**
   * Tests that create method returns a 200 response with the created session.
   * 
   * @throws Exception if simulated call to the end point fails.
   */
  @Test
  @WithMockUser
  public void create_shouldSucceedRequestWithASession() throws Exception {
    when(sessionService.create(session)).thenReturn(session);
    when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
    when(sessionMapper.toDto(session)).thenReturn(sessionDto);

    mockMvc
        .perform(MockMvcRequestBuilders.post("/api/session")
            .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(sessionDto)))
        .andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(sessionDto)));
  }

}
