package be.uantwerpen.controller;

import be.uantwerpen.model.Role;
import be.uantwerpen.model.User;
import be.uantwerpen.services.PassengerService;
import be.uantwerpen.services.RoleService;
import be.uantwerpen.services.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * This controller handles the user specific request mappings.
 */

@Controller
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Value("${core.ip:localhost}")
    private String serverCoreIP;

    @Value("#{new Integer(${core.port}) ?: 1994}")
    private int serverCorePort;

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PassengerService passengerService;
    @Autowired
    public BackendRestTemplate backendRestTemplate;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Return all the users of the webapp.
     * @param model
     * @return
     */
    @RequestMapping(value="/users", method= RequestMethod.GET)
    public String showUsers(final ModelMap model){
        model.addAttribute("allUsers", userService.findAll());
        User loginUser = userService.getPrincipalUser();
        logger.info(loginUser + " requested /users");
        model.addAttribute("currentUser", loginUser);
        return "users-list";
    }

    /**
     * Return the page where a new user can be created.
     * @param model
     * @return
     */
    @RequestMapping(value="/users/put", method= RequestMethod.GET)
    public String viewCreateUser(final ModelMap model){
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("user",new User("",""));
        User loginUser = userService.getPrincipalUser();
        logger.info(loginUser + " requested /users/put");
        model.addAttribute("currentUser", loginUser);
        return "users-manage";
    }

    /**
     * Return page to edit a created user.
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value="/users/{id}", method= RequestMethod.GET)
    public String viewEditUser(@PathVariable Long id, final ModelMap model){
        model.addAttribute("allRoles", roleService.findAll());
        User user = userService.findOne(id);
        model.addAttribute("user", user);
        User loginUser = userService.getPrincipalUser();
        model.addAttribute("currentUser", loginUser);
        logger.info(loginUser + " is watching user " + user);
        return "users-manage";
    }

    /**
     * Add a user to the list of users on the SQL database.
     * Redirect to the list of all the users.
     * @param user
     * @param result
     * @param model
     * @return
     */
    @RequestMapping(value={"/users/"}, method= RequestMethod.POST)
    public String addUser(@Valid User user, BindingResult result, final ModelMap model){
        if(userService.findByUserName(user.getUserName()) == null) {
            List<Role> roles = new ArrayList<>();
            roles.add(roleService.findRole("user"));
            user.setRoles(roles);
            userService.saveSomeAttributes(user);
            logger.info(userService.getPrincipalUser() + " creates new user: " + user);
            return "redirect:/login";
        } else {
            logger.error(userService.getPrincipalUser() + " failed to create new user");
            return "redirect:/login?error";
        }
    }

    /**
     * Save a user ot the list of users on the database.
     * Redirect to the list of all the users.
     * @param user
     * @param result
     * @param model
     * @return
     */
    @RequestMapping(value={"/users/{id}"}, method= RequestMethod.POST)
    public String editUser(@Valid User user, BindingResult result, final ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("allRoles", roleService.findAll());
            logger.error(userService.getPrincipalUser() + " edited " + user);
            return "users-manage";
        }
        userService.saveSomeAttributes(user);
        return "redirect:/users";
    }

    /**
     * Delete user from database.
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value="/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, final ModelMap model){
        logger.info(userService.findOne(id) + " removed by " + userService.getPrincipalUser());
        userService.delete(id);
        model.clear();
        return "redirect:/users";
    }
}

