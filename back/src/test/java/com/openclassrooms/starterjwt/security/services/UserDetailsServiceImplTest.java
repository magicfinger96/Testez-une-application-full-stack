package com.openclassrooms.starterjwt.security.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

/**
 * Tests {@link UserDetailsServiceImpl} class.
 */
@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserDetailsServiceImpl classUnderTest;

  /**
   * Tests that loadUserByUsername method returns a user with given username and
   * uses userRepository.findByEmail().
   */
  @Test
  public void loadUserByUsername_shouldReturnUser() {
    User user = User.builder().id(1L).email("john.doe@live.fr").lastName("Doe").firstName("John").password("pwd")
        .admin(false).build();

    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

    UserDetails existingUser = classUnderTest.loadUserByUsername(user.getEmail());

    assertThat(existingUser).isNotNull();
    assertThat(existingUser).isInstanceOf(UserDetailsImpl.class);
    verify(userRepository, times(1)).findByEmail(user.getEmail());
  }

  /**
   * Tests that the loadUserByUsername method throws a UsernameNotFoundException
   * if the user with the given email doesn't exist.
   */
  @Test
  public void loadUserByUsername_throwsIfUserDoesntExist() {
    String email = "john.doe@live.fr";
    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
    assertThrows(UsernameNotFoundException.class, () -> classUnderTest.loadUserByUsername(email));
  }
}
