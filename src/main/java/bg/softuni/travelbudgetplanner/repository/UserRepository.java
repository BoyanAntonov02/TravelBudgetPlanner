package bg.softuni.travelbudgetplanner.repository;

import bg.softuni.travelbudgetplanner.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
