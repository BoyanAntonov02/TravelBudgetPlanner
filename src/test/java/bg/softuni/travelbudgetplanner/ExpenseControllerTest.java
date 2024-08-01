package bg.softuni.travelbudgetplanner;

import bg.softuni.travelbudgetplanner.controller.ExpenseController;
import bg.softuni.travelbudgetplanner.model.dto.ExpenseDTO;
import bg.softuni.travelbudgetplanner.model.entity.Expense;
import bg.softuni.travelbudgetplanner.model.entity.Trip;
import bg.softuni.travelbudgetplanner.model.entity.UserEntity;
import bg.softuni.travelbudgetplanner.model.enums.ExpenseCategory;
import bg.softuni.travelbudgetplanner.service.ExpenseService;
import bg.softuni.travelbudgetplanner.service.TripService;
import bg.softuni.travelbudgetplanner.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpenseControllerTest {

    @Mock
    private ExpenseService expenseService;

    @Mock
    private TripService tripService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private UserDetails userDetails;

    @Mock
    private ExpenseCategory expenseCategory;

    @InjectMocks
    private ExpenseController expenseController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createExpenseForm_AuthenticatedUser() {
        Long tripId = 1L;
        Trip trip = new Trip();
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        trip.setUser(user);

        when(userDetails.getUsername()).thenReturn("testUser");
        when(tripService.getTripById(tripId)).thenReturn(trip);

        String viewName = expenseController.createExpenseForm(tripId, model, userDetails);

        assertEquals("create-expense", viewName);
        verify(model).addAttribute(eq("expenseData"), any(ExpenseDTO.class));
        verify(model).addAttribute("tripId", tripId);
    }

    @Test
    void createExpenseForm_UnauthenticatedUser() {
        Long tripId = 1L;

        String viewName = expenseController.createExpenseForm(tripId, model, null);

        assertEquals("redirect:/login", viewName);
    }

    @Test
    void createExpense_Success() {
        Long tripId = 1L;
        Trip trip = new Trip();
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        trip.setUser(user);

        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setDescription("Test Expense");
        expenseDTO.setAmount(100.0);
        expenseDTO.setDate("2023-05-01");
        expenseDTO.setCategory(expenseCategory);

        when(userDetails.getUsername()).thenReturn("testUser");
        when(tripService.getTripById(tripId)).thenReturn(trip);
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = expenseController.createExpense(tripId, expenseDTO, bindingResult, userDetails, model);

        assertEquals("redirect:/trips/view/" + tripId, viewName);
        verify(expenseService).saveExpense(any(Expense.class));
    }

    @Test
    void createExpense_ValidationErrors() {
        Long tripId = 1L;
        ExpenseDTO expenseDTO = new ExpenseDTO();

        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = expenseController.createExpense(tripId, expenseDTO, bindingResult, userDetails, model);

        assertEquals("create-expense", viewName);
        verify(model).addAttribute("expenseData", expenseDTO);
    }


    @Test
    void editExpense_Success() {
        Long expenseId = 1L;
        Expense expense = new Expense();
        Trip trip = new Trip();
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        trip.setUser(user);
        trip.setId(1L);
        expense.setTrip(trip);

        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setDescription("Updated Expense");
        expenseDTO.setAmount(150.0);
        expenseDTO.setDate("2023-05-02");
        expenseDTO.setCategory(expenseCategory);

        when(userDetails.getUsername()).thenReturn("testUser");
        when(expenseService.getExpenseById(expenseId)).thenReturn(expense);
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = expenseController.editExpense(expenseId, expenseDTO, bindingResult, userDetails, model);

        assertEquals("redirect:/trips/view/" + trip.getId(), viewName);
        verify(expenseService).saveExpense(expense);
    }

    @Test
    void deleteExpense_Success() {
        Long expenseId = 1L;
        Expense expense = new Expense();
        Trip trip = new Trip();
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        trip.setUser(user);
        trip.setId(1L);
        expense.setTrip(trip);

        when(userDetails.getUsername()).thenReturn("testUser");
        when(expenseService.getExpenseById(expenseId)).thenReturn(expense);

        String viewName = expenseController.deleteExpense(expenseId, userDetails);

        assertEquals("redirect:/trips/view/" + trip.getId(), viewName);
        verify(expenseService).deleteExpense(expenseId);
    }
}