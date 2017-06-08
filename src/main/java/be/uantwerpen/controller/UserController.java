package be.uantwerpen.controller;

import be.uantwerpen.databaseAccess.MongoDBMethods;
import be.uantwerpen.localization.astar.Astar;
import be.uantwerpen.model.Delivery;
import be.uantwerpen.model.Role;
import be.uantwerpen.model.User;
import be.uantwerpen.services.*;
import be.uantwerpen.visualization.model.DummyVehicle;
import be.uantwerpen.visualization.model.World;
import org.graphstream.graph.Graph;
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
 * Created by Frédéric Melaerts on 27/04/2017.
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
    private RestTemplate restTemplate;


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

    @RequestMapping(value="/users/{id}", method= RequestMethod.GET)
    public String viewEditUser(@PathVariable Long id, final ModelMap model){
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("user",userService.findOne(id));
        return "users-manage";
    }


    @RequestMapping(value={"/users/"}, method= RequestMethod.POST)
    public String addUser(@Valid User user, BindingResult result, final ModelMap model){
        //System.out.println(result.getModel());
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
        //System.out.println(result.getModel());
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
        MongoDBMethods monogDBClient = new MongoDBMethods();
        Iterable<Delivery> deliveries = monogDBClient.getAllDeliveries();
        model.addAttribute("allDeliveries", monogDBClient.getAllDeliveries());
        return "delivery-list";
    }

    @RequestMapping(value="/deliveries/put", method= RequestMethod.GET)
    public String viewCreateDelivery(final ModelMap model){
        Delivery del = new Delivery("","","");
        model.addAttribute("delivery",del);
        model.addAttribute("allPassengers", passengerService.findAll());

        User loginUser = userService.getPrincipalUser();
        model.addAttribute("currentUser", loginUser);
        System.out.println("User logged in: "+loginUser.getUserName());
        return "delivery-manage-user";
    }


    @RequestMapping(value="/deliveries/{idDelivery}/delete", method= RequestMethod.GET)
    public String viewCreateDelivery(@PathVariable String idDelivery){

        MongoDBMethods monogDBClient = new MongoDBMethods();
        monogDBClient.deleteDelivery(idDelivery);
        System.out.println("ID of the DELIVERY "+idDelivery);
        return "redirect:/deliveries" ;
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
//            deliveryService.saveSomeAttributes(delivery_return);
        }

        /*
            ASTAR gedeelte
         */

        // TODO delivery ID koppelen aan Astarr
        //Astar astar = new Astar();

        astarService.init(jobService, jobListService);
        astarService.determinePath2(delivery.getPointA(), delivery.getPointB(),delivery_return.getIdDelivery());

        System.out.println("REDIRECT IS PERFORMED");
        User loginUser = userService.getPrincipalUser();
        System.out.println("User logged in: "+loginUser.getUserName());
        model.addAttribute("currentUser", loginUser);
        model.addAttribute("delivery", delivery_return);
        return "delivery-navigate-user";
        // return "redirect:/deliveries";
    }



    @RequestMapping(value="/visualization",method= RequestMethod.GET)
    public String getSimulation(final ModelMap model){

        //model.addAttribute("allSegments", segmentService.findAll());
        User loginUser = userService.getPrincipalUser();
        model.addAttribute("currentUser", loginUser);

        // The following code is equal to the function getAllVehicles from the datacontroller
        // with the exception that this functions returns more data of the vehicles.

        List<DummyVehicle> vehicles = getAllSimData();

        model.addAttribute("currentUser", loginUser);
        model.addAttribute("vehiclesInfo", vehicles);

        return "visualization_map";
    }

    public List<DummyVehicle> getAllSimData() {
        List<DummyVehicle> vehicles = new ArrayList<DummyVehicle>();
        String requestAll = "request all";
        // String URL = "http://localhost:9000/posAll";
       //  String URL = "http://"+serverCoreIP+":"+serverCorePort+"/posAll";
        String URL = "http://"+serverCoreIP+":"+serverCorePort+"/getAllVehicles";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL);//.queryParam("requestAll", requestAll);
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

        return vehicles;
    }

}

