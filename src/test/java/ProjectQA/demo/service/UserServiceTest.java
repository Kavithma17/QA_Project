package ProjectQA.demo.service;

import ProjectQA.demo.model.User;
import ProjectQA.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignupSuccess() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");

        // Mock repo responses
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);

        String result = userService.signup(user);

        assertEquals("Signup successful", result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testSignupDuplicateUsername() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            userService.signup(user);
        });

        assertEquals("Username already taken", exception.getMessage());
    }

    @Test
    public void testSignupDuplicateEmail() {
        User user = new User();
        user.setUsername("newuser");
        user.setEmail("duplicate@example.com");
        user.setPassword("password");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("duplicate@example.com")).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            userService.signup(user);
        });

        assertEquals("Email already registered", exception.getMessage());
    }

    @Test
    public void testLoginSuccess() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("password"));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        boolean result = userService.login("testuser", "password");

        assertTrue(result, "Login should succeed with correct credentials");
    }

    @Test
    public void testLoginFailWrongPassword() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("password"));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        boolean result = userService.login("testuser", "wrongpassword");

        assertFalse(result, "Login should fail with wrong password");
    }

    @Test
    public void testLoginFailNonExistentUser() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        boolean result = userService.login("nonexistent", "anyPassword");

        assertFalse(result, "Login should fail for non-existent username");
    }
}
