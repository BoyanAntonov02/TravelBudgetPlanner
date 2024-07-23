package bg.softuni.travelbudgetplanner.service;

import bg.softuni.travelbudgetplanner.config.UserSession;
import bg.softuni.travelbudgetplanner.model.dto.UserRegisterDTO;
import bg.softuni.travelbudgetplanner.model.entity.UserEntity;
import bg.softuni.travelbudgetplanner.model.entity.UserRolesEntity;
import bg.softuni.travelbudgetplanner.model.enums.UserRole;
import bg.softuni.travelbudgetplanner.repository.UserRepository;
import bg.softuni.travelbudgetplanner.repository.UserRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserSession userSession;
    private UserRolesRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserSession userSession) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userSession = userSession;
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    public boolean register(UserRegisterDTO data) {
        Optional<UserEntity> existingUser = userRepository.findByUsernameOrEmail(data.getUsername(), data.getEmail());

        if (existingUser.isPresent()) {
            return false;
        }

        UserEntity user = new UserEntity();
        user.setUsername(data.getUsername());
        user.setEmail(data.getEmail());
        user.setPassword(passwordEncoder.encode(data.getPassword()));

        userRepository.save(user);

        return true;
    }

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public boolean isAdmin(UserDetails userDetails) {
        Optional<UserEntity> user = userRepository.findByUsername(userDetails.getUsername());
        return user.isPresent() && user.get().getRoles().stream()
                .anyMatch(role -> role.getUserRole() == UserRole.ADMIN);
    }

    public void makeAdmin(String username) {
        Optional<UserEntity> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            UserRolesEntity adminRole = new UserRolesEntity();
            adminRole.setUserRole(UserRole.ADMIN);
            user.getRoles().add(adminRole);
            userRepository.save(user);
        }
    }
}
