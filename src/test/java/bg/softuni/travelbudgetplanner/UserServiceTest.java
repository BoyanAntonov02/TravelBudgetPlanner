package bg.softuni.travelbudgetplanner;

import bg.softuni.travelbudgetplanner.config.UserSession;
import bg.softuni.travelbudgetplanner.model.dto.UserRegisterDTO;
import bg.softuni.travelbudgetplanner.model.entity.UserEntity;
import bg.softuni.travelbudgetplanner.model.entity.UserRolesEntity;
import bg.softuni.travelbudgetplanner.model.enums.UserRole;
import bg.softuni.travelbudgetplanner.repository.UserRepository;
import bg.softuni.travelbudgetplanner.repository.UserRolesRepository;
import bg.softuni.travelbudgetplanner.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserSession userSession;

    @Mock
    private UserRolesRepository roleRepository;

    @InjectMocks
    private UserService userService;

    private UserEntity testUser;
    private List<UserRolesEntity> roles;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


        testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");

        roles = new ArrayList<>();
        testUser.setRoles(roles);
    }

    @Test
    void testFindByUsername_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        UserEntity user = userService.findByUsername("testUser");

        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    void testFindByUsername_Failure() {
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userService.findByUsername("nonExistentUser");
        });

        assertEquals("User not found: nonExistentUser", thrown.getMessage());
    }

    @Test
    void testRegister_Success() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername("newUser");
        dto.setEmail("newuser@example.com");
        dto.setPassword("password");

        when(userRepository.findByUsernameOrEmail("newUser", "newuser@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        boolean result = userService.register(dto);

        assertTrue(result);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testRegister_Failure() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername("existingUser");
        dto.setEmail("existing@example.com");
        dto.setPassword("password");

        when(userRepository.findByUsernameOrEmail("existingUser", "existing@example.com")).thenReturn(Optional.of(testUser));

        boolean result = userService.register(dto);

        assertFalse(result);
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void testIsAdmin_Success() {
        UserRolesEntity adminRole = new UserRolesEntity();
        adminRole.setUserRole(UserRole.ADMIN);
        testUser.getRoles().add(adminRole);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        boolean result = userService.isAdmin(userDetails);

        assertTrue(result);
    }

    @Test
    void testIsAdmin_Failure() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        boolean result = userService.isAdmin(userDetails);

        assertFalse(result);
    }



    @Test
    void testMakeAdmin_Failure() {
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        userService.makeAdmin("nonExistentUser");

        verify(userRepository, never()).save(any(UserEntity.class));
    }
}
