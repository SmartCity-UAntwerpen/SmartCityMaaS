package be.uantwerpen.controller;

import be.uantwerpen.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Kevin on 17/05/2017.
 */
@Controller
public class JobController {
    @Autowired
    private JobService jobService;

    @RequestMapping(value="/users", method= RequestMethod.GET)
    public String showUsers(final ModelMap model){
        model.addAttribute("allUsers", jobService.findAll());
        return "users-list";
    }

}
