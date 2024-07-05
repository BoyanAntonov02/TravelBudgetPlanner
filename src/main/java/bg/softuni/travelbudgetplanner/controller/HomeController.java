package bg.softuni.travelbudgetplanner.controller;

import bg.softuni.travelbudgetplanner.user.TravelUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserDetails userDetails,
                       Model model) {


        if (userDetails instanceof TravelUserDetails travelUserDetails) {
            model.addAttribute("welcomeMessage", userDetails.getPassword());
        } else {
            model.addAttribute("welcomeMessage", "Anonymous");
        }

        return "index";
    }

    @GetMapping("/home")
    public String home(Model model) {
        return "home";
    }


}
