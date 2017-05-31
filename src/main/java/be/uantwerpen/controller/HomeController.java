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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @ModelAttribute("allUsers")
    public Iterable<User> populateUsers() {
        return this.userService.findAll();
    }

    @RequestMapping({"/"})
    public String showMap() {return "delivery-manage2";}

    @RequestMapping({"/login"})
    public String showLogIn(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @RequestMapping({"/home"})
    @PreAuthorize("hasRole('logon')")
    public String showHomepage(){
        return "homepage";
    }

    /*@RequestMapping(value={"/addUser"}, method=RequestMethod.POST)
    public String addUser(@Validated @ModelAttribute("user") User user){
        User retrievedUser = userService.findByUserName(user.getUserName());
        if(!retrievedUser.equals(null))
        {
            user.setFirstName(retrievedUser.getFirstName());
            user.setLastName(retrievedUser.getLastName());
            user.setPassword(retrievedUser.getPassword());
            user.setUserName(retrievedUser.getUserName());
            Permission p1 = new Permission("logon");
            Role tester = new Role("Tester");
            List<Permission> permissions =  new ArrayList<Permission>();
            permissions.add(p1);
            tester.setPermissions(permissions);
            List<Role> roles = new ArrayList<>();
            roles.add(tester);
            user.setRoles(roles);
            return "okay";
        } else {
            return "error";
        }
    }*/
}

