package be.uantwerpen.controller;

import be.uantwerpen.model.Job;
import be.uantwerpen.localization.astar.Astar;
import be.uantwerpen.model.JobList;
import be.uantwerpen.services.JobService;
import be.uantwerpen.services.JobListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.net.URL;
import java.util.*;


/**
 * Created by Kevin on 17/05/2017.
 */
@Controller
public class JobController {
    @Autowired
    private Astar astar;
    @Autowired
    private JobService jobService;
    @Autowired
    private JobListService jobListService;

    @RequestMapping(value="/initAstar", method= RequestMethod.GET)
    public String initAstar(final ModelMap model){
        astar.init(jobService, jobListService);
        astar.startAStar();
        return "jobs-list";
    }

    @RequestMapping(value="/jobs", method= RequestMethod.GET)
    public String showJobs(final ModelMap model){
        model.addAttribute("allJobs", jobService.findAll());
        model.addAttribute("allJobList", jobListService.findAll());
        jobListService.printJobList();
        return "jobs-list";
    }

    @RequestMapping(value="/jobs/put", method= RequestMethod.GET)
    public String viewCreateJob(final ModelMap model){
        model.addAttribute("job",new Job());
        return "jobs-manage";
    }

    @RequestMapping(value="/jobs/{id}", method= RequestMethod.GET)
    public String viewEditJob(@PathVariable Long id, final ModelMap model){
        model.addAttribute("job",jobService.findOne(id));
        return "jobs-manage";
    }

    @RequestMapping(value={"/jobs/", "/jobs/{id}"}, method= RequestMethod.POST)
    public String addJob(@Valid Job job, BindingResult result, final ModelMap model) {
        System.out.println(result.getModel());
        if (result.hasErrors()) {
            return "jobs-manage";
        }
        jobService.saveSomeAttributes(job);
        return "redirect:/jobs";
    }

    @RequestMapping(value="/jobs/{id}/delete")
    public String deleteJob(@PathVariable Long id, final ModelMap model){
        jobService.delete(id);
        model.clear();
        return "redirect:/jobs";
    }

    @RequestMapping(value ="/createOrder/{A}/{B}")
    public String createOrder(@PathVariable String A, @PathVariable String  B)
    {
        return "";

    }

    @RequestMapping(value="/completeJob/{idJob}", method= RequestMethod.GET)
    public String completeJob (@PathVariable Long idJob) {
        jobService.delete(idJob);
        for (JobList jl: jobListService.findAll()){
            if (jl.getJobs().get(0).getId().equals(idJob) == true) {
                jl.getJobs().get(0).setStatus("done");
                jl.getJobs().remove(0);
            }
            else {
                // do nothing for now
            }
        }
        // TODO roep methode aan om nieuwe job te dispatchen. DIE MOET GE MAKEN IN DE SERVICE

        //TODO 2: nakijken of ORDER nog een job bevat. Zoniet, ORDERN DELETEN!
        return "/dispatchJobs";
    }
}
