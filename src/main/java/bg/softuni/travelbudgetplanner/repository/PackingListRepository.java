package bg.softuni.travelbudgetplanner.repository;

import bg.softuni.travelbudgetplanner.model.entity.PackingList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackingListRepository extends JpaRepository<PackingList, Long> {
}
