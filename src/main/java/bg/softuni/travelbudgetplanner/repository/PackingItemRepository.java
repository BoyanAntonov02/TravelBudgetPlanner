package bg.softuni.travelbudgetplanner.repository;

import bg.softuni.travelbudgetplanner.model.entity.PackingItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackingItemRepository  extends JpaRepository<PackingItem, Long> {
}
