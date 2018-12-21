package be.uantwerpen.controller;

import be.uantwerpen.model.User;
import be.uantwerpen.repositories.UserRepository;
import be.uantwerpen.services.PassengerService;
import be.uantwerpen.services.RoleService;
import be.uantwerpen.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    @Mock
    private PassengerService passengerService;
    @Mock
    public BackendRestTemplate backendRestTemplate;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mvc;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testShowUsers() throws Exception {
        mvc.perform(get("/users")).andExpect(view().name("users-list"));
    }

    @Test
    public void testViewCreateUser() throws Exception {
        mvc.perform(get("/users/put")).andExpect(view().name("users-manage"));
    }

    @Test
    public void testViewEditUser() throws Exception {
        mvc.perform(get("/users/1")).andExpect(view().name("users-manage"));
    }

    @Test
    public void testAddUserNotExisting() throws Exception {
        mvc.perform(post("/users/")).andExpect(view().name("redirect:/login"));
    }

    @Test
    public void testAddUserExisting() throws Exception {
        when(userService.findByUserName(anyString())).thenReturn(new User());
        mvc.perform(post("/users/")).andExpect(view().name("redirect:/login?error"));
    }

    @Test
    public void testEditUserError() throws Exception {
        mvc.perform(post("/users/1")).andExpect(view().name("users-manage"));
    }

    @Test
    public void testEditUser() throws Exception {
        // TODO fix test
        User user = new User();
        user.setId(1L);
        user.setUserName("test");
        user.setFirstName("testName");
        user.setLastName("testLastName");
        user.setPassword("testPass");
//        mvc.perform(post("/users/1").content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON)).andExpect(view().name("redirect:/users"));
    }

    @Test
    public void testDelete() throws Exception {
        mvc.perform(post("/users/1/delete")).andExpect(view().name("redirect:/users"));
    }

}
