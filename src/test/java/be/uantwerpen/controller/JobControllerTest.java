package be.uantwerpen.controller;

import be.uantwerpen.localization.astar.Astar;
import be.uantwerpen.services.JobListService;
import be.uantwerpen.services.JobService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Created by Kevin on 8/06/2017.
 */
public class JobControllerTest {

    @InjectMocks
    private JobController jobController;

    @Mock
    private Astar astar;

    @Mock
    private JobService jobService;

    @Mock
    private JobListService jobListService;

    private MockMvc mvc;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(jobController).build();
    }

    @Test
    public void testShowJobs() throws Exception {
        mvc.perform(get("/jobs")).andExpect(view().name("jobs-list"));
    }

    @Test
    public void testViewCreateJobs() throws Exception {
        mvc.perform(get("/jobs/put")).andExpect(view().name("jobs-manage"));
    }

    @Test
    public void testViewEditJob() throws Exception {
        mvc.perform(get("/jobs/1")).andExpect(view().name("jobs-manage"));
    }

    @Test
    public void testDeleteJob() throws Exception {
        mvc.perform(get("/jobs/1/delete")).andExpect(view().name("redirect:/jobs"));
    }

    @Test
    public void testCreateOrder() throws Exception {
        mvc.perform(get("/createOrder/1/2")).andExpect(view().name("redirect:/jobs"));
    }
}
