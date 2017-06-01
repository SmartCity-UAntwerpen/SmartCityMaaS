package be.uantwerpen.Controller;

import be.uantwerpen.model.Permission;
import be.uantwerpen.model.Role;
import be.uantwerpen.model.User;
import be.uantwerpen.repositories.UserRepository;
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

    @RequestMapping({"/"})
    public String showMap() {return "redirect:/home";}

    @RequestMapping({"/login"})
    public String showLogIn(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @RequestMapping({"/home"})
    //@PreAuthorize("hasRole('admin')")
    public String showHomepage(){

        Iterable<User> users = userService.findAll();
        for(User user:users)
        {
            System.out.println("Username : " +user.getUserName());
            List<Role> roles = user.getRoles();
            for(Role role : roles)
            {
                System.out.println("Role : " +role.getName());
                List<Permission> permissions = role.getPermissions();
                for(Permission permission : permissions)
                {
                    System.out.println("Permission : " +permission.getName());
                }
            }
        }
        return "homepage";
    }
}