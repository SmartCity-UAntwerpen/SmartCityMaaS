package be.uantwerpen.controller;

import be.uantwerpen.databaseAccess.MongoDBMethods;
import be.uantwerpen.model.Delivery;
import be.uantwerpen.model.User;
import be.uantwerpen.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
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


    @RequestMapping(value="/users", method= RequestMethod.GET)
    public String showUsers(final ModelMap model){
        model.addAttribute("allUsers", userService.findAll());
        return "users-list";
    }

    @RequestMapping(value="/users/put", method= RequestMethod.GET)
    public String viewCreateUser(final ModelMap model){
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("user",new User("",""));
        return "users-manage";
    }

    @RequestMapping(value="/users/{id}", method= RequestMethod.GET)
    public String viewEditUser(@PathVariable Long id, final ModelMap model){
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("user",userService.findOne(id));
        return "users-manage";
    }

    @RequestMapping(value={"/users/", "/users/{id}"}, method= RequestMethod.POST)
    public String addUser(@Valid User user, BindingResult result, final ModelMap model){
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
        model.addAttribute("allSegments", segmentService.findAll());
        model.addAttribute("allPassengers", passengerService.findAll());
        model.addAttribute("delivery",new Delivery("",""));
        return "delivery-manage";
    }

    @RequestMapping(value={"/deliveries/", "/deliveries/{id}"}, method= RequestMethod.POST)
    public String addDeliver(@Valid Delivery delivery, BindingResult result, final ModelMap model){
        System.out.println(result.getModel());
        if(result.hasErrors()){
            model.addAttribute("allSegments", segmentService.findAll());
            model.addAttribute("allPassengers", passengerService.findAll());
            return "delivery-manage";
        }
        delivery.setType("HumanTransport");
        MongoDBMethods monogDBClient = new MongoDBMethods();
        monogDBClient.putStatement(delivery);
        deliveryService.saveSomeAttributes(delivery);
        return "redirect:/deliveries";
    }
}
