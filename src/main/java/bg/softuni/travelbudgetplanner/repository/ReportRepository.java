package bg.softuni.travelbudgetplanner.repository;

import bg.softuni.travelbudgetplanner.model.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
