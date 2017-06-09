package be.uantwerpen.controller;

import be.uantwerpen.model.Delivery;
import be.uantwerpen.model.Permission;
import be.uantwerpen.model.Role;
import be.uantwerpen.model.User;
import be.uantwerpen.repositories.UserRepository;
import be.uantwerpen.services.PassengerService;
import be.uantwerpen.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frédéric Melaerts on 26/04/2017.
 */
@Controller
public class HomeController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PassengerService passengerService;

    //return the first page, to make a new delivery
    @RequestMapping({"/"})
    public String showMap(Model model) {
        User loginUser = userService.getPrincipalUser();
        model.addAttribute("currentUser", loginUser);
        System.out.println("User logged in: "+loginUser.getUserName());

        return "home_user";
    }

    //return the login page
    @RequestMapping({"/login"})
    public String showLogIn(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }
}