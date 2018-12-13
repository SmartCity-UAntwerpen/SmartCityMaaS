package be.uantwerpen.controller;

import be.uantwerpen.model.Job;
import be.uantwerpen.model.User;
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


@Controller
public class JobController {

    private static final Logger logger = LogManager.getLogger(JobController.class);

    @Value("${core.ip:localhost}")
    private String serverCoreIP;

    @Value("#{new Integer(${core.port}) ?: 1994}")
    private int serverCorePort;

    @Autowired
    private UserService userService;

    //get a list for all the jobs
    @RequestMapping(value = "/jobs", method = RequestMethod.GET)
    public String showJobs(final ModelMap model) {
        User loginUser = userService.getPrincipalUser();
        logger.info(loginUser + " requested /jobs");
        model.addAttribute("currentUser", loginUser);
        RestTemplate restTemplate = new RestTemplate();
        Job[] jobs = restTemplate.getForObject("http://" + serverCoreIP + ":" + serverCorePort + "/job/service/findalljobs", Job[].class);
        model.addAttribute("allJobs", jobs);
        // TODO: model.addAttribute("allJobList", jobListService.findAll());
        return "jobs-list";
    }

    //make a new job manually
    @RequestMapping(value = "/jobs/put", method = RequestMethod.GET)
    public String viewCreateJob(final ModelMap model) {
        User loginUser = userService.getPrincipalUser();
        logger.info(loginUser + " requested /jobs/put");
        model.addAttribute("currentUser", loginUser);
        model.addAttribute("job", new Job());
        return "jobs-manage";
    }

    //get a specific job
    @RequestMapping(value = "/jobs/{id}", method = RequestMethod.GET)
    public String viewEditJob(@PathVariable Long id, final ModelMap model) {
        User loginUser = userService.getPrincipalUser();
        logger.info(loginUser + " requested /jobs/" + id);
        model.addAttribute("currentUser", loginUser);
        RestTemplate restTemplate = new RestTemplate();
        Job job = restTemplate.getForObject("http://" + serverCoreIP + ":" + serverCorePort + "/job/service/getjob/{id}", Job.class, id);
        model.addAttribute("job", job);
        return "jobs-manage";
    }

    //make a specific job
    @RequestMapping(value = {"/jobs/", "/jobs/{id}"}, method = RequestMethod.POST)
    public String addJob(@PathVariable Long id, @Valid Job job, BindingResult result, final ModelMap model) {
        User loginUser = userService.getPrincipalUser();
        model.addAttribute("currentUser", loginUser);
        if (result.hasErrors()) {
            logger.error("Creating a new job was unsuccessful");
            return "jobs-manage";
        }
//        jobService.saveSomeAttributes(job);
        // TODO update job in backbone
        logger.info(loginUser + " created new job " + job);
        logger.info("BingindResult model: " + result.getModel());
        return "redirect:/jobs";
    }

    //delete a specific job
    @RequestMapping(value = "/jobs/{id}/delete")
    public String deleteJob(@PathVariable Long id, final ModelMap model) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject("http://" + serverCoreIP + ":" + serverCorePort + "/job/service/deletejob/{id}", null, Job.class, id);
        model.clear();
        logger.info(userService.getPrincipalUser() + " deleted job " + id);
        return "redirect:/jobs";
    }


    @RequestMapping(value = "/jobs/deleteAll")
    public String deleteAllJobs(final ModelMap model) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject("http://" + serverCoreIP + ":" + serverCorePort + "/job/service/deletealljobs", null, Job.class);
        // TODO: delete JobLists
        logger.info(userService.getPrincipalUser() + "  deleted all jobs and joblists.");
        return "redirect:/jobs";
    }

    @RequestMapping(value = "/removeOrders")
    public String removeOrders() {
//        jobListRepository.deleteAll();
//        if (jobListRepository.findAll().size() == 0) {
//            logger.info(userService.getPrincipalUser() + " removed all orders.");
//        }
        // TODO: remove orders in backbone
        return "redirect:/";
    }

}
