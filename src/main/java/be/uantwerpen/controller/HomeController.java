package be.uantwerpen.controller;

import be.uantwerpen.model.User;
import be.uantwerpen.repositories.UserRepository;
import be.uantwerpen.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Frédéric Melaerts on 26/04/2017.
 */
@Controller
public class HomeController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @ModelAttribute("allUsers")
    public Iterable<User> populateUsers() {
        return this.userService.findAll();
    }

    @RequestMapping({"/"})
    public String showLogIn() {return "logInPage2";}

    @RequestMapping({"/home"})
    public String showHomepage(){
        return "homepage";
    }
}

