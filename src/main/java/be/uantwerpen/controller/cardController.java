package be.uantwerpen.controller;

import be.uantwerpen.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Created by dries on 19/04/2017.
 */
/*
@Controller
public class cardController {
    @RequestMapping({"/test"})
    public String showHomepage(final ModelMap model)
    {
        return "reception";
    }

    @RequestMapping({"/hello"})
    public String showHello(final ModelMap model)
    {
        return "testPage";
    }

    @RequestMapping(value = "/lol", method = RequestMethod.GET)
    public String showAddUserForm(Model model) {

        User user = new User("rob","verschoten");
        model.addAttribute("userForm", user);
        return "lol";
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public String saveOrUpdateUser(@ModelAttribute("userForm") User user,
                                   BindingResult result, Model model) {

        return null;
    }

    @GetMapping("/greeting")
    public String greetingForm(Model model) {
        model.addAttribute("greeting", new Greeting());
        return "greeting";
    }

    @PostMapping("/greeting")
    public String greetingSubmit(@ModelAttribute Greeting greeting) {
        return "result";
    }
    /*@RequestMapping("/tutorial")
    public String greetingSubmit(@ModelAttribute Greeting greeting) {
        return "result";
    }*/
//}
