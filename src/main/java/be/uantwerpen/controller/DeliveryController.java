package be.uantwerpen.controller;

import be.uantwerpen.model.Delivery;
import be.uantwerpen.model.User;
import be.uantwerpen.services.*;
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

import javax.validation.Valid;

/**
 * This controller handles the user specific request mappings.
 */

@Controller
public class DeliveryController {
    private static final Logger logger = LogManager.getLogger(DeliveryController.class);

    @Value("${core.ip:localhost}")
    private String serverCoreIP;

    @Value("#{new Integer(${core.port}) ?: 1994}")
    private int serverCorePort;

    @Autowired
    private UserService userService;
    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private PassengerService passengerService;
    @Autowired
    public BackendRestTemplate backendRestTemplate;

    @Autowired
    private BackboneService backboneService;
//
//    @Autowired
//    private MongoService mongoService;

    /**
     * Return page with all the deliveries save in the mongoDB database.
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/deliveries", method = RequestMethod.GET)
    public String viewDeliveries(final ModelMap model) {
        logger.info(userService.getPrincipalUser() + " requested /deliveries");
       // Iterable<Delivery> deliveries = mongoService.getAllDeliveries(); // TO BACKBONE
        Iterable<Delivery> deliveries = deliveryService.findAll();
        model.addAttribute("allDeliveries", deliveries);
        User loginUser = userService.getPrincipalUser();
        model.addAttribute("currentUser", loginUser);
        return "delivery-list";
    }

    /**
     * Return the page where a new delivery can be created.
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/deliveries/put", method = RequestMethod.GET)
    public String viewCreateDelivery(final ModelMap model) {
        Delivery del = new Delivery();
        model.addAttribute("delivery", del);
        model.addAttribute("allPassengers", passengerService.findAll());

        User loginUser = userService.getPrincipalUser();
        model.addAttribute("currentUser", loginUser);
        logger.info(loginUser + " requested /deliveries/put");
        return "delivery-manage-user";
    }

    /**
     * Delete a specified id from the mongoDB database.
     *
     * @param idDelivery
     * @return
     */
    @RequestMapping(value = "/deliveries/{idDelivery}/delete", method = RequestMethod.GET)
    public String deleteDelivery(@PathVariable long idDelivery) {
        deliveryService.delete(idDelivery);
        logger.info(userService.getPrincipalUser() + " deleted delivery " + idDelivery);
        return "redirect:/deliveries";
    }

    /**
     * Add a delivery to deliveries in the mongoDB database.
     *
     * @param delivery
     * @param result
     * @param model
     * @return
     */
    @RequestMapping(value = {"/deliveries/", "/deliveries/{id}"}, method = RequestMethod.POST)
    public String addDelivery(@Valid Delivery delivery, BindingResult result, final ModelMap model) {
        logger.info(result.getModel());
        logger.info("Delivery: point A " + delivery.getPointA() + " map " + delivery.getMapA() + ", point B " + delivery.getPointB() + " map " + delivery.getMapB());

        if (result.hasErrors()) {
            model.addAttribute("allPassengers", passengerService.findAll());
            return "delivery-manage2";
        }
        delivery.setType("HumanTransport");

        User loginUser = userService.getPrincipalUser();
        model.addAttribute("currentUser", loginUser);

        try {
            int backboneId = backboneService.planPath(Integer.parseInt(delivery.getPointA()), delivery.getMapA(), Integer.parseInt(delivery.getPointB()), delivery.getMapB());
            delivery.setBackboneID(backboneId);
            delivery =  deliveryService.save(delivery);
            if (delivery == null)
                return "delivery-manage-user";
            logger.info("Job has been created by " + loginUser);

            model.addAttribute("delivery", delivery);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "delivery-manage-user";
        }

        return "delivery-navigate-user";
    }


    // TODO: SET DELIVERY DONE (MongoDBMethods)


    /**
     * Return the page with th visualization of the smartcity vehicles on the map.
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/visualization", method = RequestMethod.GET)
    public String getSimulation(final ModelMap model) {
        User loginUser = userService.getPrincipalUser();
        model.addAttribute("currentUser", loginUser);
        logger.info(loginUser + " requested /visualization");

        // The following code is equal to the function getAllVehicles from the datacontroller
        // with the exception that this functions returns more data of the vehicles.
        model.addAttribute("currentUser", loginUser);
        try {
            // DEPRECATED
            //List<DummyVehicle> vehicles = getAllSimData();
            //model.addAttribute("vehiclesInfo", vehicles);
            model.addAttribute("deliveries", deliveryService.findAll());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            logger.error(e);
        }

        return "visualization_map";
    }

}

