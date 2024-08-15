package com.openclassrooms.starterjwt.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;

/**
 * Integration tests on {@link UserController} class.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

  @MockBean
  private UserMapper userMapper;

  @MockBean
  private UserService userService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private User user;
  private UserDto userDto;

  /**
   * Creates a user and its DTO before each test.
   */
  @BeforeEach
  private void init() {
    user = User.builder().id(1L).lastName("Smith").firstName("John").password("pwd").email("john.smith@live.fr")
        .admin(false).build();

    userDto = new UserDto();
    userDto.setId(1L);
    userDto.setLastName("Smith");
    userDto.setFirstName("John");
    userDto.setEmail("john.smith@live.fr");
    userDto.setPassword("pwd");
    userDto.setAdmin(false);
  }

  /**
   * Tests that findById method returns a 200 response with a user with given id.
   * 
   * @throws Exception if simulated call to the end point fails.
   */
  @Test
  @WithMockUser
  public void findById_shouldSucceedRequestWithAUser() throws Exception {
    Long id = user.getId();

    when(userService.findById(id)).thenReturn(user);
    when(userMapper.toDto(user)).thenReturn(userDto);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/user/" + id).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(userDto)));
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
    mockMvc.perform(MockMvcRequestBuilders.get("/api/user/one").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  /**
   * Tests that findById method returns a not found response when the user with
   * the given id doesn't exist.
   * 
   * @throws Exception if simulated call to the end point fails.
   */
  @Test
  @WithMockUser
  public void findById_shouldReturnNotFound_whenUserDoesntExist() throws Exception {
    Long id = 2L;

    when(userService.findById(id)).thenReturn(null);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/user/" + id).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  /**
   * Tests that delete method returns a successful response.
   * 
   * @throws Exception if simulated call to the end point fails.
   */
  @Test
  @WithMockUser(username = "john.smith@live.fr")
  public void delete_shouldSucceedRequest() throws Exception {
    Long id = 1L;

    when(userService.findById(id)).thenReturn(user);

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/" + id)).andExpect(status().isOk());
  }

  /**
   * Tests that delete method returns an unauthorized response when the user to
   * delete isn't the authenticated one.
   * 
   * @throws Exception if simulated call to the end point fails.
   */
  @Test
  @WithMockUser(username = "wrongEmail@live.fr")
  public void delete_shouldReturnUnauthorized_whenUserNotSelf() throws Exception {
    Long id = 1L;

    when(userService.findById(id)).thenReturn(user);

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/" + id)).andExpect(status().isUnauthorized());
  }

  /**
   * Tests that delete method returns a bad request response when the given id is
   * malformed.
   * 
   * @throws Exception if simulated call to the end point fails.
   */
  @Test
  @WithMockUser()
  public void delete_shouldReturnBadRequest_whenMalformedId() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/one")).andExpect(status().isBadRequest());
  }

  /**
   * Tests that delete method returns a not found response when the user with the
   * given id doesn't exist.
   * 
   * @throws Exception if simulated call to the end point fails.
   */
  @Test
  @WithMockUser
  public void delete_shouldReturnNotFound_whenUserDoesntExist() throws Exception {
    Long id = 2L;

    when(userService.findById(id)).thenReturn(null);

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/" + id).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }
}
