package bg.softuni.travelbudgetplanner.repository;


import bg.softuni.travelbudgetplanner.model.entity.UserRolesEntity;
import bg.softuni.travelbudgetplanner.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRolesEntity, Long> {
    UserRolesEntity findByUserRole(UserRole role);
}
