package be.uantwerpen.controller;

import be.uantwerpen.model.Job;
import be.uantwerpen.localization.astar.Astar;
import be.uantwerpen.model.JobList;
import be.uantwerpen.model.User;
import be.uantwerpen.repositories.JobListRepository;
import be.uantwerpen.services.JobService;
import be.uantwerpen.services.JobListService;
import be.uantwerpen.services.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.jws.WebParam;
import javax.validation.Valid;
import java.net.URL;
import java.util.*;


@Controller
public class JobController {

    private static final Logger logger = LogManager.getLogger(JobController.class);
    @Autowired
    private Astar astar;
    @Autowired
    private JobService jobService;
    @Autowired
    private JobListService jobListService;
    @Autowired
    private JobListRepository jobListRepository;

    @Autowired
    private UserService userService;

    //for testing purposes
    @RequestMapping(value="/initAstar", method= RequestMethod.GET)
    public String initAstar(final ModelMap model){
        astar.init();
        astar.startAStar();
        return "jobs-list";
    }

    //get a list for all the jobs
    @RequestMapping(value="/jobs", method= RequestMethod.GET)
    public String showJobs(final ModelMap model){
        User loginUser = userService.getPrincipalUser();
        model.addAttribute("currentUser", loginUser);
        model.addAttribute("allJobs", jobService.findAll());
        model.addAttribute("allJobList", jobListService.findAll());
        jobListService.printJobList();
        return "jobs-list";
    }

    //make a new job manually
    @RequestMapping(value="/jobs/put", method= RequestMethod.GET)
    public String viewCreateJob(final ModelMap model){
        User loginUser = userService.getPrincipalUser();
        model.addAttribute("currentUser", loginUser);
        model.addAttribute("job",new Job());
        return "jobs-manage";
    }

    //get a specific job
    @RequestMapping(value="/jobs/{id}", method= RequestMethod.GET)
    public String viewEditJob(@PathVariable Long id, final ModelMap model){
        User loginUser = userService.getPrincipalUser();
        model.addAttribute("currentUser", loginUser);
        model.addAttribute("job",jobService.findOne(id));
        return "jobs-manage";
    }

    //make a specific job
    @RequestMapping(value={"/jobs/", "/jobs/{id}"}, method= RequestMethod.POST)
    public String addJob(@PathVariable Long id, @Valid Job job, BindingResult result, final ModelMap model) {
        System.out.println(result.getModel());
        User loginUser = userService.getPrincipalUser();
        model.addAttribute("currentUser", loginUser);
        if (result.hasErrors()) {
            return "jobs-manage";
        }
        jobService.saveSomeAttributes(job);
        return "redirect:/jobs";
    }

    //delete a specific job
    @RequestMapping(value="/jobs/{id}/delete")
    public String deleteJob(@PathVariable Long id, final ModelMap model){
        jobService.delete(id);
        model.clear();
        return "redirect:/jobs";
    }


    @RequestMapping(value="/jobs/deleteAll")
    public String deleteAllJobs( final ModelMap model ) {
        // Delete all jobs and joblists
        jobService.deleteAll();
        jobListService.deleteAll();
        return "redirect:/jobs";
    }

    @RequestMapping(value = "/removeOrders")
    public String removeOrders() {
        jobListRepository.deleteAll();
        if(jobListRepository.findAll().size() == 0) {
            System.out.println("all orders have been cleared");
        }
        return "redirect:/";
    }

    //make an order
    @RequestMapping(value ="/createOrder/{Start}/{Stop}")
    public String createOrder(@PathVariable String Start, @PathVariable String Stop) {
        astar.init();
        astar.makeNode();
        astar.makeEdge();
        astar.testDeterminePath(astar.getGraph(),Start,Stop);
        return "redirect:/jobs";
    }

    @RequestMapping(value="/completeJob/{idJob}", method= RequestMethod.GET)
    public String completeJob (@PathVariable Long idJob) {
        System.out.println("job Complete");
        for (JobList jl: jobListService.findAll()){
            if (jl.getJobs().get(0).getId().equals(idJob)) {
                jl.getJobs().remove(0);
                System.out.println("job deleted");
                jobService.delete(idJob);
                System.out.println("job deleted from joblist");
            }
            if (jl.getJobs().isEmpty()) {
                //TODO need to test to see if this works
                jobListService.deleteOrder(jl.getId());
                System.out.println("delete order");
            }
        }

        if(jobListRepository.findAll().size() != 0) {
            jobListService.dispatch2Core();
            System.out.println("dispatch next job");
        }

        return "redirect:/jobs";
    }
}
