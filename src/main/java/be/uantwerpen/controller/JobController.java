package be.uantwerpen.controller;

import be.uantwerpen.Models.Job;
import be.uantwerpen.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by Kevin on 17/05/2017.
 */
@Controller
public class JobController {
    @Autowired
    private JobService jobService;

    @RequestMapping(value="/jobs", method= RequestMethod.GET)
    public String showJobs(final ModelMap model){
        model.addAttribute("allJobs", jobService.findAll());
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

}
