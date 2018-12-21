package be.uantwerpen.controller;

import be.uantwerpen.model.User;
import be.uantwerpen.services.JobListService;
import be.uantwerpen.services.JobService;
import be.uantwerpen.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobControllerTest {

    @InjectMocks
    private JobController jobController;

    @Mock
    private JobService jobService;

    @Mock
    private JobListService jobListService;

    @Mock
    private UserService userService;

    private MockMvc mvc;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(jobController).build();

        User loginUser = new User();
        loginUser.setFirstName("Test");
        loginUser.setLastName("Mock");
        loginUser.setUserName("TestAdmin");
        when(userService.getPrincipalUser()).thenReturn(loginUser);
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
    public void testCreateJob() throws Exception {
        mvc.perform(post("/jobs/")).andExpect(view().name("redirect:/jobs"));
    }

    @Test
    public void testDeleteJob() throws Exception {
        mvc.perform(get("/jobs/1/delete")).andExpect(view().name("redirect:/jobs"));
    }

    @Test
    public void testDeleteAllJobs() throws Exception {
        mvc.perform(get("/jobs/deleteAll")).andExpect(view().name("redirect:/jobs"));
    }

}
