package com.openclassrooms.starterjwt.security.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

/**
 * Tests UserDetailsImpl class.
 */
public class UserDetailsImplTest {

  private UserDetailsImpl classUnderTest;

  /**
   * Instantiate classUnderTest before each test.
   */
  @BeforeEach
  public void init() {
    classUnderTest = UserDetailsImpl.builder().id(1L).username("john.doe@live.fr").firstName("John").lastName("Doe")
        .admin(false).password("pwd").build();
  }

  /**
   * Tests that equals method returns true when compared to another
   * UserDetailsImpl with same id.
   */
  @Test
  public void equals_shouldReturnsTrue_withSameId() {
    UserDetailsImpl user2 = UserDetailsImpl.builder().id(1L).username("john.doe@live.fr").firstName("John")
        .lastName("Doe").admin(false).password("pwd").build();

    assertTrue(classUnderTest.equals(user2));
  }

  /**
   * Tests that equals method returns true when compared to itself.
   */
  @Test
  public void equals_shouldReturnsTrue_withSelf() {
    assertTrue(classUnderTest.equals(classUnderTest));
  }

  /**
   * Tests that equals method returns false when compared to null.
   */
  @Test
  public void equals_shouldReturnsFalse_withNull() {
    assertFalse(classUnderTest.equals(null));
  }

  /**
   * Tests that equals method returns false when compared to another type.
   */
  @Test
  public void equals_shouldReturnsFalse_withOtherType() {
    assertFalse(classUnderTest.equals(new Object()));
  }
  
  /**
   * Tests that getAuthorities method returns an empty Collection.
   */
  @Test
  public void getAuthorities_shouldReturnAnEmptyCollection() {
    assertThat(classUnderTest.getAuthorities()).isNotNull().isEmpty();
  }

  public Collection<? extends GrantedAuthority> getAuthorities() {        
    return new HashSet<GrantedAuthority>();
}
  
  /**
   * Tests that isAccountNonExpired method returns true.
   */
  @Test
  public void isAccountNonExpired_shouldReturnsTrue() {
    assertTrue(classUnderTest.isAccountNonExpired());
  }

  /**
   * Tests that isAccountNonLocked method returns true.
   */
  @Test
  public void isAccountNonLocked_shouldReturnsTrue() {
    assertTrue(classUnderTest.isAccountNonLocked());
  }

  /**
   * Tests that isCredentialsNonExpired method returns true.
   */
  @Test
  public void isCredentialsNonExpired_shouldReturnsTrue() {
    assertTrue(classUnderTest.isCredentialsNonExpired());
  }

  /**
   * Tests that isEnabled method returns true.
   */
  @Test
  public void isEnabled_shouldReturnsTrue() {
    assertTrue(classUnderTest.isEnabled());
  }
}
