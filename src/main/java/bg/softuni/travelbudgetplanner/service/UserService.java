package bg.softuni.travelbudgetplanner.service;

import bg.softuni.travelbudgetplanner.config.UserSession;
import bg.softuni.travelbudgetplanner.model.dto.UserLoginDTO;
import bg.softuni.travelbudgetplanner.model.dto.UserRegisterDTO;
import bg.softuni.travelbudgetplanner.model.entity.User;
import bg.softuni.travelbudgetplanner.model.entity.UserRole;
import bg.softuni.travelbudgetplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserSession userSession;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserSession userSession) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userSession = userSession;
    }

    public boolean register(UserRegisterDTO data) {
        Optional<User> existingUser = userRepository.findByUsernameOrEmail(data.getUsername(), data.getEmail());

        if (existingUser.isPresent()) {
            return false;
        }

        User user = new User();
        user.setUsername(data.getUsername());
        user.setEmail(data.getEmail());
        user.setPassword(passwordEncoder.encode(data.getPassword()));
        user.setRole(UserRole.USER); // Default role

        userRepository.save(user);

        return true;
    }

    public boolean login(UserLoginDTO data) {
        Optional<User> byUsername =
                userRepository.findByUsername(data.getUsername());


        if (byUsername.isEmpty()) {
            return false;
        }

        User user = byUsername.get();

        if (!passwordEncoder.matches(data.getPassword(), user.getPassword())) {
            return false;
        }

        userSession.login(byUsername.get());

        return true;
    }

    public void logout() {
        userSession.logout();
    }
}
