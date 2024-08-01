package bg.softuni.travelbudgetplanner;

import bg.softuni.travelbudgetplanner.model.entity.UserEntity;
import bg.softuni.travelbudgetplanner.model.entity.UserRolesEntity;
import bg.softuni.travelbudgetplanner.model.enums.UserRole;
import bg.softuni.travelbudgetplanner.repository.UserRepository;
import bg.softuni.travelbudgetplanner.service.TravelUserDetailsService;
import bg.softuni.travelbudgetplanner.user.TravelUserDetails;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TravelUserDetailsServiceTest {

    @Test
    void loadUserByUsername_Success() {

        UserRepository userRepository = Mockito.mock(UserRepository.class);
        TravelUserDetailsService userDetailsService = new TravelUserDetailsService(userRepository);

        String username = "testUser";
        String password = "testPassword";

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);

        UserRolesEntity role = new UserRolesEntity();
        role.setUserRole(UserRole.ADMIN);

        List<UserRolesEntity> roles = new ArrayList<>();
        roles.add(role);

        userEntity.setRoles(roles);

        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(userEntity));


        UserDetails userDetails = userDetailsService.loadUserByUsername(username);


        assertEquals(username, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());

        Set<GrantedAuthority> expectedAuthorities = new HashSet<>();
        expectedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        assertEquals(expectedAuthorities, userDetails.getAuthorities());
    }

    @Test
    void loadUserByUsername_UserNotFound() {

        UserRepository userRepository = Mockito.mock(UserRepository.class);
        TravelUserDetailsService userDetailsService = new TravelUserDetailsService(userRepository);

        String nonExistentUsername = "nonExistentUser";

        Mockito.when(userRepository.findByUsername(nonExistentUsername))
                .thenReturn(Optional.empty());

        UsernameNotFoundException thrownException = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(nonExistentUsername)
        );

        String expectedMessage = "User:  " + nonExistentUsername + " not found!";
        assertEquals(expectedMessage, thrownException.getMessage());
    }
}
