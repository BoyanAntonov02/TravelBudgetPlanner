package bg.softuni.travelbudgetplanner.service;

import bg.softuni.travelbudgetplanner.model.entity.Expense;
import bg.softuni.travelbudgetplanner.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense saveExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found with id: " + id));
    }

    public List<Expense> findExpensesByTripId(Long tripId) {
        return expenseRepository.findByTripId(tripId);
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }
}
