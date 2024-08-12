package com.openclassrooms.starterjwt.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  UserRepository userRepository;

  @InjectMocks
  UserService classUnderTest;

  private User user;

  @BeforeEach
  public void init() {
    user = User.builder().id(1L).email("johnDoe@live.fr").lastName("Doe").firstName("John").password("pwd")
        .admin(false).build();
  }

  @Test
  public void delete_shouldUseRepository() {

    willDoNothing().given(userRepository).deleteById(user.getId());

    classUnderTest.delete(user.getId());

    verify(userRepository, times(1)).deleteById(user.getId());
  }

  @Test
  public void findById_shouldReturnUser() {
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

    User existingUser = classUnderTest.findById(user.getId());

    assertThat(existingUser).isNotNull();
  }
}
