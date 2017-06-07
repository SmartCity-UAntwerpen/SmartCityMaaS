package be.uantwerpen.controller;

import be.uantwerpen.databaseAccess.MongoDBMethods;
import be.uantwerpen.localization.astar.Astar;
import be.uantwerpen.model.Delivery;
import be.uantwerpen.model.Role;
import be.uantwerpen.model.User;
import be.uantwerpen.services.*;
import be.uantwerpen.visualization.model.World;
import org.graphstream.graph.Graph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frédéric Melaerts on 27/04/2017.
 */

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private SegmentService segmentService;
    @Autowired
    private PassengerService passengerService;

    @Autowired
    public BackendRestemplate backendRestemplate;
    @Autowired
    public Astar astarService;



    @Autowired
    private JobService jobService;
    @Autowired
    private JobListService jobListService;






    @ModelAttribute("user")
    private User getUser(){ return new User();}

    @RequestMapping(value="/users", method= RequestMethod.GET)
    //@PreAuthorize("hasRole('admin')")
    public String showUsers(final ModelMap model){
        model.addAttribute("allUsers", userService.findAll());
        return "users-list";
    }
    @RequestMapping(value="/map", method= RequestMethod.GET)
    public String showMap(final ModelMap model){
        return "map";
    }

    @RequestMapping(value="/users/put", method= RequestMethod.GET)
    //@PreAuthorize("hasRole('admin') and hasRole('logon')")
    public String viewCreateUser(final ModelMap model){
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("user",new User("",""));
        return "users-manage";
    }

    /*@RequestMapping(value="/addUser", method=RequestMethod.POST)
    public String addRealUser(@Valid User user, BindingResult result final ModelMap model) {
        if(userService.findByUserName(user.getUserName()).equals(null)) {
            List<Role> roles = new ArrayList<>();
            roles.add(roleService.findRole("user"));
            user.setRoles(roles);
            userService.saveSomeAttributes(user);
            return "redirect:/login";
        } else {
            return "error";
        }
    }*/

    @RequestMapping(value="/users/{id}", method= RequestMethod.GET)
    public String viewEditUser(@PathVariable Long id, final ModelMap model){
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("user",userService.findOne(id));
        return "users-manage";
    }

    @RequestMapping(value={"/users/"}, method= RequestMethod.POST)
    public String addUser(@Valid User user, BindingResult result, final ModelMap model){
        System.out.println(result.getModel());
        if(userService.findByUserName(user.getUserName()) == null) {
            List<Role> roles = new ArrayList<>();
            roles.add(roleService.findRole("user"));
            user.setRoles(roles);
            userService.saveSomeAttributes(user);
            return "redirect:/login";
        } else {
            return "redirect:/login?error";
        }
    }

    @RequestMapping(value={"/users/{id}"}, method= RequestMethod.POST)
    public String editUser(@Valid User user, BindingResult result, final ModelMap model){
        System.out.println(result.getModel());
        if(result.hasErrors()){
            model.addAttribute("allRoles", roleService.findAll());
            return "users-manage";
        }
        userService.saveSomeAttributes(user);
        return "redirect:/users";
    }

    @RequestMapping(value="/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, final ModelMap model){
        userService.delete(id);
        model.clear();
        return "redirect:/users";
    }

    @RequestMapping(value="/deliveries", method= RequestMethod.GET)
    public String viewOrders(final ModelMap model){
        model.addAttribute("allDeliveries", deliveryService.findAll());
        return "delivery-list";
    }

    @RequestMapping(value="/deliveries/put", method= RequestMethod.GET)
    public String viewCreateDelivery(final ModelMap model){
        /*model.addAttribute("allSegments", segmentService.findAll());
        model.addAttribute("allPassengers", passengerService.findAll());
        Delivery del = new Delivery("","","");
        model.addAttribute("delivery",del);
        World world = new World(300,300);*/


        Delivery del = new Delivery("","","");
        model.addAttribute("delivery",del);
        model.addAttribute("allPassengers", passengerService.findAll());

        User loginUser = userService.getPrincipalUser();
        model.addAttribute("currentUser", loginUser);
        System.out.println("User logged in: "+loginUser.getUserName());
        return "delivery-manage-user";
    }

    @RequestMapping(value={"/deliveries/", "/deliveries/{id}"}, method= RequestMethod.POST)
    public String addDeliver(@Valid Delivery delivery, BindingResult result, final ModelMap model){
        System.out.println(result.getModel());
        System.out.println("--- Delivery point A "+ delivery.getPointA() + " point B "+ delivery.getPointB()+" ---");

        if(result.hasErrors()){
            model.addAttribute("allPassengers", passengerService.findAll());
            return "delivery-manage2";
        }
        delivery.setType("HumanTransport");



        delivery.setPointA(""+backendRestemplate.getKeyHashMap(Integer.parseInt(delivery.getPointA())));
        delivery.setPointB(""+backendRestemplate.getKeyHashMap(Integer.parseInt(delivery.getPointB())));
        MongoDBMethods monogDBClient = new MongoDBMethods();
        monogDBClient.putStatement(delivery);
        Delivery delivery_return = monogDBClient.getLastDelivery();
        if(delivery_return.getFirstName() == null)
        {
            System.out.println("-- !! Could not retrieve last delivery from MongoDB service !! --");

        }else
        {
            System.out.println("-- Retrieve last delivery from MongoDB service --");
            delivery_return.print();
            deliveryService.saveSomeAttributes(delivery_return);
        }

        /*
            ASTAR gedeelte
         */

        // TODO delivery ID koppelen aan Astarr
        //Astar astar = new Astar();
/*
        astarService.init(jobService, jobListService);
        astarService.determinePath(delivery.getPointA(), delivery.getPointB(),delivery_return.getIdDelivery());
*/


        System.out.println("REDIRECT IS PERFORMED");
        User loginUser = userService.getPrincipalUser();
        System.out.println("User logged in: "+loginUser.getUserName());
        model.addAttribute("currentUser", loginUser);
        model.addAttribute("delivery", delivery_return);
        return "delivery-navigate-user";
        // return "redirect:/deliveries";
    }
    @RequestMapping(value="/simulation",method= RequestMethod.GET)
    public String getSimulation(final ModelMap model){


        model.addAttribute("allSegments", segmentService.findAll());
        model.addAttribute("allPassengers", passengerService.findAll());
        Delivery del = new Delivery("","","");
        model.addAttribute("delivery",del);
        World world = new World(300,300);
        return "simulation";
    }
}

