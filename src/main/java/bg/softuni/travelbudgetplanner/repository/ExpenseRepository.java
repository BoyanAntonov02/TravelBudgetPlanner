package bg.softuni.travelbudgetplanner.repository;

import bg.softuni.travelbudgetplanner.model.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
