package bg.softuni.travelbudgetplanner.controller;

import bg.softuni.travelbudgetplanner.model.dto.TripDTO;
import bg.softuni.travelbudgetplanner.model.entity.Trip;
import bg.softuni.travelbudgetplanner.model.entity.UserEntity;
import bg.softuni.travelbudgetplanner.service.TripService;
import bg.softuni.travelbudgetplanner.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.logging.Logger;

@Controller
@RequestMapping("/trips")
public class TripController {

    private static final Logger logger = Logger.getLogger(TripController.class.getName());
    private final TripService tripService;
    private final UserService userService;

    public TripController(TripService tripService, UserService userService) {
        this.tripService = tripService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String createTripForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            logger.warning("User is not authenticated.");
            return "redirect:/login";
        }
        logger.info("User is authenticated: " + userDetails.getUsername());
        model.addAttribute("tripData", new TripDTO());
        return "create-trip";
    }

    @PostMapping
    public String createTrip(TripDTO tripData, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            logger.warning("User is not authenticated.");
            return "redirect:/login";
        }

        logger.info("Creating trip for user: " + userDetails.getUsername());

        UserEntity user = userService.findByUsername(userDetails.getUsername());
        Trip trip = new Trip();
        trip.setDestination(tripData.getDestination());
        trip.setStartDate(LocalDate.parse(tripData.getStartDate()));
        trip.setEndDate(LocalDate.parse(tripData.getEndDate()));
        trip.setBudget(tripData.getBudget());
        trip.setUser(user);

        tripService.saveTrip(trip);

        return "redirect:/dashboard";
    }


    @GetMapping("/edit/{id}")
    public String editTripForm(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            logger.warning("User is not authenticated.");
            return "redirect:/login";
        }

        Trip trip = tripService.getTripById(id);
        if (!trip.getUser().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/dashboard";
        }

        TripDTO tripData = new TripDTO();
        tripData.setDestination(trip.getDestination());
        tripData.setStartDate(trip.getStartDate().toString());
        tripData.setEndDate(trip.getEndDate().toString());
        tripData.setBudget(trip.getBudget());

        model.addAttribute("tripData", tripData);
        model.addAttribute("tripId", id);

        return "edit-trip";
    }

    @PostMapping("/edit/{id}")
    public String editTrip(@PathVariable Long id, @Valid TripDTO tripData, BindingResult bindingResult, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            logger.warning("User is not authenticated.");
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            logger.warning("Validation errors found: " + bindingResult.getAllErrors());
            model.addAttribute("tripData", tripData);
            return "edit-trip";
        }

        Trip trip = tripService.getTripById(id);
        if (!trip.getUser().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/dashboard";
        }

        trip.setDestination(tripData.getDestination());
        trip.setStartDate(LocalDate.parse(tripData.getStartDate()));
        trip.setEndDate(LocalDate.parse(tripData.getEndDate()));
        trip.setBudget(tripData.getBudget());

        tripService.saveTrip(trip);

        return "redirect:/dashboard";
    }

    @PostMapping("/delete/{id}")
    public String deleteTrip(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            logger.warning("User is not authenticated.");
            return "redirect:/login";
        }

        Trip trip = tripService.getTripById(id);
        if (!trip.getUser().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/dashboard";
        }

        tripService.deleteTrip(id);

        return "redirect:/dashboard";
    }
}
