package be.uantwerpen.controller;

import be.uantwerpen.services.JobListService;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

/**
 * Created by dries on 9/06/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class DataControllerTest {
    @InjectMocks
    private DataController dataController;


    @Mock
    private JobListService jobListService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    public BackendRestemplate backendRestemplate;

    private MockMvc mvc;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(dataController).build();
    }




}
