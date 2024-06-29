package bg.softuni.travelbudgetplanner.controller;

import bg.softuni.travelbudgetplanner.config.UserSession;
import bg.softuni.travelbudgetplanner.model.dto.UserLoginDTO;
import bg.softuni.travelbudgetplanner.model.dto.UserRegisterDTO;
import bg.softuni.travelbudgetplanner.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    private final UserService userService;
    private final UserSession userSession;

    public UserController(UserService userService, UserSession userSession) {
        this.userService = userService;
        this.userSession = userSession;
    }

    @ModelAttribute("registerData")
    public UserRegisterDTO registerDTO() {
        return new UserRegisterDTO();
    }

    @GetMapping("/register")
    public String register(Model model) {
        if (userSession.isUserLoggedIn()) {
            return "redirect:/";
        }
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(
            @Valid @ModelAttribute("registerData") UserRegisterDTO data,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (userSession.isUserLoggedIn()) {
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("registerData", data);
            model.addAttribute("org.springframework.validation.BindingResult.registerData", bindingResult);
            return "register";
        }

        if (!data.getPassword().equals(data.getConfirmPassword())) {
            model.addAttribute("registerData", data);
            model.addAttribute("passwordError", "Passwords do not match");
            return "register";
        }

        boolean success = userService.register(data);

        if (!success) {
            model.addAttribute("registerData", data);
            model.addAttribute("registrationError", "Username or email already exists");
            return "register";
        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String viewLogin(Model model) {
        if (userSession.isUserLoggedIn()) {
            return "redirect:/";
        }
        model.addAttribute("loginData", new UserLoginDTO()); // Ensure this line is present
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(
            @Valid UserLoginDTO data,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("loginData", data);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.loginData", bindingResult);

            return "redirect:/login";
        }

        boolean success = userService.login(data);

        if (!success) {
            redirectAttributes.addFlashAttribute("loginData", data);
            redirectAttributes.addFlashAttribute("userPassMismatch", true);

            return "redirect:login";
        }

        return "redirect:/home";
    }

    @PostMapping("/logout")
    public String logout() {
        userService.logout();
        return "redirect:/";
    }
}
