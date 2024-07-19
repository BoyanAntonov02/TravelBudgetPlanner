package bg.softuni.travelbudgetplanner.controller;

import bg.softuni.travelbudgetplanner.model.dto.ExpenseDTO;
import bg.softuni.travelbudgetplanner.model.entity.Expense;
import bg.softuni.travelbudgetplanner.model.entity.Trip;
import bg.softuni.travelbudgetplanner.model.entity.UserEntity;
import bg.softuni.travelbudgetplanner.service.ExpenseService;
import bg.softuni.travelbudgetplanner.service.TripService;
import bg.softuni.travelbudgetplanner.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.logging.Logger;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    private static final Logger logger = Logger.getLogger(ExpenseController.class.getName());
    private final ExpenseService expenseService;
    private final TripService tripService;
    private final UserService userService;

    public ExpenseController(ExpenseService expenseService, TripService tripService, UserService userService) {
        this.expenseService = expenseService;
        this.tripService = tripService;
        this.userService = userService;
    }

    @GetMapping("/create/{tripId}")
    public String createExpenseForm(@PathVariable Long tripId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            logger.warning("User is not authenticated.");
            return "redirect:/login";
        }
        logger.info("User is authenticated: " + userDetails.getUsername());

        Trip trip = tripService.getTripById(tripId);
        if (!trip.getUser().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/dashboard";
        }

        model.addAttribute("expenseData", new ExpenseDTO());
        model.addAttribute("tripId", tripId);
        return "create-expense";
    }

    @PostMapping("/create/{tripId}")
    public String createExpense(@PathVariable Long tripId, @Valid ExpenseDTO expenseData, BindingResult bindingResult, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            logger.warning("User is not authenticated.");
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            logger.warning("Validation errors found: " + bindingResult.getAllErrors());
            model.addAttribute("expenseData", expenseData);
            return "create-expense";
        }

        Trip trip = tripService.getTripById(tripId);
        if (!trip.getUser().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/dashboard";
        }

        Expense expense = new Expense();
        expense.setDescription(expenseData.getDescription());
        expense.setAmount(expenseData.getAmount());
        expense.setDate(LocalDate.parse(expenseData.getDate()));
        expense.setCategory(expenseData.getCategory());
        expense.setTrip(trip);

        expenseService.saveExpense(expense);

        return "redirect:/trips/view/" + tripId;
    }

    @GetMapping("/edit/{id}")
    public String editExpenseForm(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            logger.warning("User is not authenticated.");
            return "redirect:/login";
        }

        Expense expense = expenseService.getExpenseById(id);
        if (!expense.getTrip().getUser().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/dashboard";
        }

        ExpenseDTO expenseData = new ExpenseDTO();
        expenseData.setDescription(expense.getDescription());
        expenseData.setAmount(expense.getAmount());
        expenseData.setDate(expense.getDate().toString());
        expenseData.setCategory(expense.getCategory());

        model.addAttribute("expenseData", expenseData);
        model.addAttribute("expenseId", id);

        return "edit-expense";
    }

    @PostMapping("/edit/{id}")
    public String editExpense(@PathVariable Long id, @Valid ExpenseDTO expenseData, BindingResult bindingResult, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            logger.warning("User is not authenticated.");
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            logger.warning("Validation errors found: " + bindingResult.getAllErrors());
            model.addAttribute("expenseData", expenseData);
            return "edit-expense";
        }

        Expense expense = expenseService.getExpenseById(id);
        if (!expense.getTrip().getUser().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/dashboard";
        }

        expense.setDescription(expenseData.getDescription());
        expense.setAmount(expenseData.getAmount());
        expense.setDate(LocalDate.parse(expenseData.getDate()));
        expense.setCategory(expenseData.getCategory());

        expenseService.saveExpense(expense);

        return "redirect:/trips/view/" + expense.getTrip().getId();
    }

    @PostMapping("/delete/{id}")
    public String deleteExpense(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            logger.warning("User is not authenticated.");
            return "redirect:/login";
        }

        Expense expense = expenseService.getExpenseById(id);
        if (!expense.getTrip().getUser().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/dashboard";
        }

        expenseService.deleteExpense(id);

        return "redirect:/trips/view/" + expense.getTrip().getId();
    }
}
