package be.uantwerpen.controller;

import be.uantwerpen.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HomeControllerTest {

    @InjectMocks
    private HomeController homeController;
    @Mock
    private UserService userService;

    private MockMvc mvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    @Test
    public void showMap() throws Exception {
        mvc.perform(get("/")).andExpect(view().name("home_user"));
    }

    @Test
    public void showMapError() throws Exception {
        mvc.perform(get("/").param("errorStatus", "500").param("errorMsg", "Error message")).andExpect(view().name("home_user"));
    }

//    @Test
//    public void showLogIn() throws Exception {
//        mvc.perform(get("/login")).andExpect(view().name("login"));
//    }
}