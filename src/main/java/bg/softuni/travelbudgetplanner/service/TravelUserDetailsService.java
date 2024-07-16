package bg.softuni.travelbudgetplanner.service;

import bg.softuni.travelbudgetplanner.model.entity.UserEntity;
import bg.softuni.travelbudgetplanner.model.entity.UserRole;
import bg.softuni.travelbudgetplanner.model.entity.UserRolesEntity;
import bg.softuni.travelbudgetplanner.repository.UserRepository;
import bg.softuni.travelbudgetplanner.user.TravelUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public class TravelUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public TravelUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository
                .findByUsername(username)
                .map(TravelUserDetailsService::map)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User:  " + username + " not found!"));
    }

    private static UserDetails map(UserEntity userEntity) {

        return new TravelUserDetails(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getRoles().stream().map(UserRolesEntity::getUserRole).map(TravelUserDetailsService::map).toList()
        );
    }

    private static GrantedAuthority map(UserRole role) {
        return new SimpleGrantedAuthority(
                "ROLE_" + role
        );
    }
}
