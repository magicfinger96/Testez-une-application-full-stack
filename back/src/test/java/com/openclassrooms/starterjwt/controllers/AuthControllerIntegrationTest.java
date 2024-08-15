package com.openclassrooms.starterjwt.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

/**
 * Integration tests on {@link AuthController} class.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

  @MockBean
  private AuthenticationManager authenticationManager;

  @MockBean
  private JwtUtils jwtUtils;

  @MockBean
  private PasswordEncoder passwordEncoder;

  @MockBean
  private UserRepository userRepository;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private SignupRequest signUpRequest;

  /**
   * Creates a SignupRequest and a LoginRequest before each test.
   */
  @BeforeEach
  private void init() {
    signUpRequest = new SignupRequest();
    signUpRequest.setFirstName("Edith");
    signUpRequest.setLastName("Piaf");
    signUpRequest.setPassword("password");
    signUpRequest.setEmail("edith.p@live.fr");
  }

  /**
   * Tests that authenticateUser method returns a successful response with a
   * JwtResponse, with user existing.
   * 
   * @throws Exception if simulated call to the end point fails.
   */
  @Test
  public void authenticateUser_shouldSucceedRequest_whenUserExists() throws Exception {
    User user = User.builder().id(1L).firstName("John").lastName("Wick").email("john.wick@live.fr").password("password")
        .admin(true).build();

    UserDetailsImpl userDetails = UserDetailsImpl.builder().id(1L).firstName("John").lastName("Wick")
        .username("john.wick@live.fr").password("password").build();

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail(user.getEmail());
    loginRequest.setPassword(user.getPassword());

    JwtResponse jwtResponse = new JwtResponse("mocked-token", user.getId(), user.getEmail(), user.getFirstName(),
        user.getLastName(), user.isAdmin());

    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, user.getPassword());

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
    when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("mocked-token");
    when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

    mockMvc
        .perform(MockMvcRequestBuilders.post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(jwtResponse)));
  }

  /**
   * Tests that authenticateUser method returns a successful response with a
   * JwtResponse containing admin to false, with user not existing.
   * 
   * @throws Exception if simulated call to the end point fails.
   */
  @Test
  public void authenticateUser_shouldSucceedRequest_whenUserDoesntExist() throws Exception {
    User user = User.builder().id(1L).firstName("John").lastName("Wick").email("john.wick@live.fr").password("password")
        .admin(true).build();

    UserDetailsImpl userDetails = UserDetailsImpl.builder().id(1L).firstName("John").lastName("Wick")
        .username("john.wick@live.fr").password("password").build();

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail(user.getEmail());
    loginRequest.setPassword(user.getPassword());

    JwtResponse jwtResponse = new JwtResponse("mocked-token", user.getId(), user.getEmail(), user.getFirstName(),
        user.getLastName(), false);

    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, user.getPassword());

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
    when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("mocked-token");
    when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

    mockMvc
        .perform(MockMvcRequestBuilders.post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(jwtResponse)));
  }

  /**
   * Tests that registerUser method returns a successful response with the
   * MessageResponse "User registered successfully!".
   * 
   * @throws Exception if simulated call to the end point fails.
   */
  @Test
  public void registerUser_shouldSucceedRequest() throws Exception {
    when(userRepository.existsByEmail(any(String.class))).thenReturn(false);
    when(passwordEncoder.encode(any(String.class))).thenReturn("encryptedPwd");

    mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(signUpRequest))).andExpect(status().isOk()).andExpect(
            content().json(objectMapper.writeValueAsString(new MessageResponse("User registered successfully!"))));
  }

  /**
   * Tests that registerUser method returns a bad request when the email already
   * exists, with the MessageResponse "Error: Email is already taken!".
   * 
   * @throws Exception if simulated call to the end point fails.
   */
  @Test
  public void registerUser_shouldReturnBadRequest_whenEmailAlreadyExists() throws Exception {
    when(userRepository.existsByEmail(any(String.class))).thenReturn(true);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(signUpRequest))).andExpect(status().isBadRequest()).andExpect(
            content().json(objectMapper.writeValueAsString(new MessageResponse("Error: Email is already taken!"))));
  }
}
