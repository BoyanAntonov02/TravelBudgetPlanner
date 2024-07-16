package bg.softuni.travelbudgetplanner.controller;

import bg.softuni.travelbudgetplanner.model.entity.Trip;
import bg.softuni.travelbudgetplanner.model.entity.UserEntity;
import bg.softuni.travelbudgetplanner.service.TripService;
import bg.softuni.travelbudgetplanner.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private static final Logger logger = Logger.getLogger(DashboardController.class.getName());
    private final TripService tripService;
    private final UserService userService;

    public DashboardController(TripService tripService, UserService userService) {
        this.tripService = tripService;
        this.userService = userService;
    }

    @GetMapping
    public String showDashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            logger.warning("User is not authenticated.");
            return "redirect:/login";
        }

        logger.info("Fetching trips for user: " + userDetails.getUsername());

        UserEntity user = userService.findByUsername(userDetails.getUsername());
        List<Trip> trips = tripService.findTripsByUser(user);

        model.addAttribute("trips", trips);
        return "dashboard";
    }
}
