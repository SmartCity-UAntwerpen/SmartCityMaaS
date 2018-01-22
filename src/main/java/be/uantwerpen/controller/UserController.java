package be.uantwerpen.controller;

import be.uantwerpen.databaseAccess.MongoDBMethods;
import be.uantwerpen.localization.astar.Astar;
import be.uantwerpen.model.Delivery;
import be.uantwerpen.model.Role;
import be.uantwerpen.model.User;
import be.uantwerpen.services.*;
import be.uantwerpen.visualization.model.DummyVehicle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This controller handles the user specific request mappings.
 */

@Controller
public class UserController {

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
    public BackendRestemplate backendRestemplate;
    @Autowired
    public Astar astarService;
    @Autowired
    private RestTemplate restTemplate;

    @ModelAttribute("user")
    private User getUser(){ return new User();}

    /**
     * Return all the users of the webapp.
     * @param model
     * @return
     */
    @RequestMapping(value="/users", method= RequestMethod.GET)
    public String showUsers(final ModelMap model){
        model.addAttribute("allUsers", userService.findAll());
        User loginUser = userService.getPrincipalUser();
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
        model.addAttribute("user",userService.findOne(id));
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
            return "redirect:/login";
        } else {
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
        userService.delete(id);
        model.clear();
        return "redirect:/users";
    }

    /**
     * Return page with all the deliveries save in the mongoDB database.
     * @param model
     * @return
     */
    @RequestMapping(value="/deliveries", method= RequestMethod.GET)
    public String viewDeliveries(final ModelMap model){
        MongoDBMethods monogDBClient = new MongoDBMethods();
        Iterable<Delivery> deliveries = monogDBClient.getAllDeliveries();
        model.addAttribute("allDeliveries", monogDBClient.getAllDeliveries());
        User loginUser = userService.getPrincipalUser();
        model.addAttribute("currentUser", loginUser);
        return "delivery-list";
    }

    /**
     * Return the page where a new delivery can be created.
     * @param model
     * @return
     */
    @RequestMapping(value="/deliveries/put", method= RequestMethod.GET)
    public String viewCreateDelivery(final ModelMap model){
        Delivery del = new Delivery("","","");
        model.addAttribute("delivery",del);
        model.addAttribute("allPassengers", passengerService.findAll());

        User loginUser = userService.getPrincipalUser();
        model.addAttribute("currentUser", loginUser);
        return "delivery-manage-user";
    }

    /**
     * Delete a specified id from the mongoDB database.
     * @param idDelivery
     * @return
     */
    @RequestMapping(value="/deliveries/{idDelivery}/delete", method= RequestMethod.GET)
    public String deleteDelivery(@PathVariable String idDelivery){
        MongoDBMethods monogDBClient = new MongoDBMethods();
        monogDBClient.deleteDelivery(idDelivery);
        return "redirect:/deliveries" ;
    }

    /**
     * Add a delivery to deliveries in the mongoDB database.
     * @param delivery
     * @param result
     * @param model
     * @return
     */
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
        if(delivery_return.getFirstName() == null) {
            System.out.println("-- !! Could not retrieve last delivery from MongoDB service !! --");
            return "home_user";

        } else {
            System.out.println("-- Retrieve last delivery from MongoDB service --");
            delivery_return.print();
        }

        astarService.init();
        astarService.determinePath2(delivery.getPointA(), delivery.getPointB(),delivery_return.getIdDelivery());
        System.out.println("JOB HAS BEEN CREATED");
        User loginUser = userService.getPrincipalUser();
        model.addAttribute("currentUser", loginUser);
        delivery_return.setPointA(""+backendRestemplate.getValueofKeyHashMap(Integer.parseInt(delivery_return.getPointA())));
        delivery_return.setPointB(""+backendRestemplate.getValueofKeyHashMap(Integer.parseInt(delivery_return.getPointB())));
        model.addAttribute("delivery", delivery_return);
        return "delivery-navigate-user";
    }


    /**
     * Return the page with th visualization of the smartcity vehicles on the map.
     * @param model
     * @return
     */
    @RequestMapping(value="/visualization",method= RequestMethod.GET)
    public String getSimulation(final ModelMap model){
        User loginUser = userService.getPrincipalUser();
        model.addAttribute("currentUser", loginUser);

        // The following code is equal to the function getAllVehicles from the datacontroller
        // with the exception that this functions returns more data of the vehicles.
        List<DummyVehicle> vehicles = getAllSimData();
        model.addAttribute("currentUser", loginUser);
        model.addAttribute("vehiclesInfo", vehicles);
        return "visualization_map";
    }

    /**
     * Retrieve all the vehicle data from the core.
     * This includes their identifier, type, start point, end point, and the %-progress between these two points.
     * @return
     */
    public List<DummyVehicle> getAllSimData() {
        List<DummyVehicle> vehicles = new ArrayList<DummyVehicle>();
        String requestAll = "request all";
        String URL = "http://"+serverCoreIP+":"+serverCorePort+"/bot/getAllVehicles";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        // Get response from the core
        HttpEntity<String> httpResponse = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                entity,
                String.class);
        JSONParser parser = new JSONParser();

        Object obj = null;
        // Parse JSON data from the core.
        try {
            obj = parser.parse(httpResponse.getBody());
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
                idVeh = ((Long)par_jsonObject.get("idVehicle")).intValue();
                idStart = ((Long)par_jsonObject.get("idStart")).intValue();
                idEnd = ((Long)par_jsonObject.get("idEnd")).intValue();
                percentage = ((Long)par_jsonObject.get("percentage")).intValue();
                type = par_jsonObject.get("type").toString();
                dumVeh.setID(idVeh);
                dumVeh.setStart(idStart);
                dumVeh.setEnd(idEnd);
                dumVeh.setProgress(percentage);
                dumVeh.setType(type);
                vehicles.add(dumVeh);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Vehicle count: " + vehicles.size());
        return vehicles;
    }
}

