package be.uantwerpen.controller;

import be.uantwerpen.model.Delivery;
import be.uantwerpen.model.User;
import be.uantwerpen.services.BackboneService;
import be.uantwerpen.services.MongoService;
import be.uantwerpen.services.PassengerService;
import be.uantwerpen.services.UserService;
import be.uantwerpen.visualization.model.DummyVehicle;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    private PassengerService passengerService;
    @Autowired
    public BackendRestTemplate backendRestTemplate;

    @Autowired
    private BackboneService backboneService;

    @Autowired
    private MongoService mongoService;

    /**
     * Return page with all the deliveries save in the mongoDB database.
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/deliveries", method = RequestMethod.GET)
    public String viewDeliveries(final ModelMap model) {
        logger.info(userService.getPrincipalUser() + " requested /deliveries");
        Iterable<Delivery> deliveries = mongoService.getAllDeliveries();
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
        Delivery del = new Delivery("", "", "");
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
    public String deleteDelivery(@PathVariable String idDelivery) {
        mongoService.deleteDelivery(idDelivery);
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
    public String addDeliver(@Valid Delivery delivery, BindingResult result, final ModelMap model) {
        logger.info(result.getModel());
        logger.info("Delivery: point A " + delivery.getPointA() + " map " + delivery.getMapA() + ", point B " + delivery.getPointB() + " map " + delivery.getMapB());

        if (result.hasErrors()) {
            model.addAttribute("allPassengers", passengerService.findAll());
            return "delivery-manage2";
        }
        delivery.setType("HumanTransport");

        //delivery.setPointA(""+ backendRestTemplate.getKeyHashMap(Integer.parseInt(delivery.getPointA())));
        //delivery.setPointB(""+ backendRestTemplate.getKeyHashMap(Integer.parseInt(delivery.getPointB())));
        mongoService.putStatement(delivery);
        Delivery delivery_return = mongoService.getLastDelivery();
        if (delivery_return.getFirstName() == null) {
            logger.error("Could not retrieve last delivery from MongoDB service.");
            return "home_user";

        } else {
            logger.info("Retrieve last delivery from MongoDB service.");
            delivery_return.print();
        }

        /*String graphUrl = "http://" + serverCoreIP + ":" + serverCorePort + "/link/pathlinks";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Graph> responseGraph;
        responseGraph = restTemplate.getForEntity(graphUrl, Graph.class);
        Graph graph = responseGraph.getBody();*/

        User loginUser = userService.getPrincipalUser();
        model.addAttribute("currentUser", loginUser);

        try {
            backboneService.planPath(Integer.parseInt(delivery.getPointA()), Integer.parseInt(delivery.getPointB()), delivery.getMapA(), delivery.getMapB());

            logger.info("Job has been created by " + loginUser);
            //delivery_return.setPointA(""+ backendRestTemplate.getValueOfKeyHashMap(Integer.parseInt(delivery_return.getPointA())));
            //delivery_return.setPointB(""+ backendRestTemplate.getValueOfKeyHashMap(Integer.parseInt(delivery_return.getPointB())));
            model.addAttribute("delivery", delivery_return);
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
            List<DummyVehicle> vehicles = getAllSimData();
            model.addAttribute("vehiclesInfo", vehicles);
            model.addAttribute("deliveries", mongoService.getAllBusyDeliveries());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        return "visualization_map";
    }

    /**
     * Retrieve all the vehicle data from the core.
     * This includes their identifier, type, start point, end point, and the %-progress between these two points.
     *
     * @return
     */
    public List<DummyVehicle> getAllSimData() {
        List<DummyVehicle> vehicles = new ArrayList<DummyVehicle>();
        String requestAll = "request all";
        String URL = "http://" + serverCoreIP + ":" + serverCorePort + "/bot/getAllVehicles";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        // Get response from the core
        // COMMENT FOR LOCAL TEST
        /*HttpEntity<String> httpResponse = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                entity,
                String.class);*/

        JSONParser parser = new JSONParser();

        Object obj = null;
        // Parse JSON data from the core.
        try {
            ////// TEST
            obj = parser.parse(new FileReader("testdata/getAllVehicles.txt"));
            ///
            //obj = parser.parse(httpResponse.getBody());
            /////////////
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray virDevices = (JSONArray) jsonObject.get("vehicles");
            Iterator<String> iterator = virDevices.iterator();
            int idVeh = -1;
            int idStart = -1;
            int idEnd = -1;
            int percentage = -1;
            String type = "";
            while (iterator.hasNext()) {
                DummyVehicle dumVeh = new DummyVehicle();
                obj = iterator.next();
                JSONObject par_jsonObject = (JSONObject) obj;
                idVeh = ((Long) par_jsonObject.get("idVehicle")).intValue();
                idStart = ((Long) par_jsonObject.get("idStart")).intValue();
                idEnd = ((Long) par_jsonObject.get("idEnd")).intValue();
                percentage = ((Long) par_jsonObject.get("percentage")).intValue();
                type = par_jsonObject.get("type").toString();
                dumVeh.setID(idVeh);
                dumVeh.setStart(idStart);
                dumVeh.setEnd(idEnd);
                dumVeh.setProgress(percentage);
                dumVeh.setType(type);
                vehicles.add(dumVeh);
            }
        } catch (ParseException | IOException e) { //IOException |
            logger.error("Can't read or parse file.", e);
        }
        logger.info("Vehicle count: " + vehicles.size());
        return vehicles;
    }
}

