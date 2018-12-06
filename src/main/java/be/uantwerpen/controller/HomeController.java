package be.uantwerpen.controller;

import be.uantwerpen.model.User;
import be.uantwerpen.repositories.UserRepository;
import be.uantwerpen.services.PassengerService;
import be.uantwerpen.services.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class HomeController {
    private static final Logger logger = LogManager.getLogger(HomeController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PassengerService passengerService;

    //return the first page, to make a new delivery
    @RequestMapping({"/"})
    public String showMap(Model model, @RequestParam(required = false) String errorStatus, @RequestParam(required = false) String errorMsg) {
        User loginUser = userService.getPrincipalUser();
        model.addAttribute("currentUser", loginUser);

        if( errorStatus != null ){
            model.addAttribute("error", "Error " + errorStatus + ": " + errorMsg);
        }
        return "home_user";
    }

    //return the login page
    @RequestMapping({"/login"})
    public String showLogIn(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }
}