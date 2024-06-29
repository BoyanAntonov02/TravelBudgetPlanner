package bg.softuni.travelbudgetplanner.controller;

import bg.softuni.travelbudgetplanner.config.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserSession userSession;

    @Autowired
    public HomeController(UserSession userSession) {
        this.userSession = userSession;
    }

    @GetMapping("/")
    public String notLogged() {
        if (userSession.isUserLoggedIn()) {
            return "redirect:/home";
        }

        return "index";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("title", "Home");
        return "home";
    }
}
