package bg.softuni.travelbudgetplanner.controller;

import bg.softuni.travelbudgetplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.logging.Logger;

@Controller
public class AdminController {

    private static final Logger logger = Logger.getLogger(AdminController.class.getName());

    @Autowired
    private UserService userService;

    @PostMapping("/users/makeAdmin")
    public String makeAdmin(@RequestParam String username, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null || !userService.isAdmin(userDetails)) {
            logger.warning("User is not authenticated or not an admin.");
            return "redirect:/login";
        }

        userService.makeAdmin(username);

        return "redirect:/dashboard";
    }
}
