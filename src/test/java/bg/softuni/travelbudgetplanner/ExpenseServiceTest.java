package bg.softuni.travelbudgetplanner;

import bg.softuni.travelbudgetplanner.model.entity.Expense;
import bg.softuni.travelbudgetplanner.repository.ExpenseRepository;
import bg.softuni.travelbudgetplanner.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveExpense_Success() {
        Expense expense = new Expense();
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

        Expense savedExpense = expenseService.saveExpense(expense);

        assertNotNull(savedExpense);
        verify(expenseRepository).save(expense);
    }

    @Test
    void getExpenseById_Success() {
        Long id = 1L;
        Expense expense = new Expense();
        when(expenseRepository.findById(id)).thenReturn(Optional.of(expense));

        Expense foundExpense = expenseService.getExpenseById(id);

        assertNotNull(foundExpense);
        assertEquals(expense, foundExpense);
    }

    @Test
    void getExpenseById_NotFound() {
        Long id = 1L;
        when(expenseRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> expenseService.getExpenseById(id));
    }

    @Test
    void findExpensesByTripId_Success() {
        Long tripId = 1L;
        List<Expense> expenses = Arrays.asList(new Expense(), new Expense());
        when(expenseRepository.findByTripId(tripId)).thenReturn(expenses);

        List<Expense> foundExpenses = expenseService.findExpensesByTripId(tripId);

        assertNotNull(foundExpenses);
        assertEquals(2, foundExpenses.size());
    }

    @Test
    void deleteExpense_Success() {
        Long id = 1L;
        doNothing().when(expenseRepository).deleteById(id);

        expenseService.deleteExpense(id);

        verify(expenseRepository).deleteById(id);
    }
}