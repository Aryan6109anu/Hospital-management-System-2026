package com.example.demo.CustomUserDetailsServiceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.entities.User;
import com.example.demo.model.Role;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.CustomUserDetailsService;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    // ✅ SUCCESS CASE
    @Test
    void loadUserByUsername_success() {

        // given
        User user = new User();
        user.setUsername("amit");
        user.setPassword("password123");
        user.setRole(Role.ROLE_PATIENT);   // ✅ correct role

        when(userRepository.findByUsername("amit"))
                .thenReturn(Optional.of(user));

        // when
        UserDetails userDetails =
                userDetailsService.loadUserByUsername("amit");

        // then
        assertNotNull(userDetails);
        assertEquals("amit", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(
            userDetails.getAuthorities()
                       .stream()
                       .anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"))
        );

        verify(userRepository, times(1))
                .findByUsername("amit");
    }

    // ❌ USER NOT FOUND CASE
    @Test
    void loadUserByUsername_userNotFound() {

        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("unknown")
        );

        verify(userRepository, times(1))
                .findByUsername("unknown");
    }
}
