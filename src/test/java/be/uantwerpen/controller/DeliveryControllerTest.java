package be.uantwerpen.controller;

import be.uantwerpen.model.Delivery;
import be.uantwerpen.model.User;
import be.uantwerpen.services.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeliveryControllerTest {

    @InjectMocks
    private DeliveryController deliveryController;
    @Mock
    private UserService userService;
    @Mock
    @SuppressWarnings("unused")
    private PassengerService passengerService;
    @Mock
    private MongoService mongoService;

    private MockMvc mvc;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        User loginUser = new User();
        loginUser.setFirstName("Test");
        loginUser.setLastName("Mock");
        loginUser.setUserName("TestAdmin");
        when(userService.getPrincipalUser()).thenReturn(loginUser);

        Delivery delivery = new Delivery();
        delivery.setFirstName("test");
        delivery.setLastName("Mock");
        delivery.setPointA("1");
        delivery.setPointB("2");
        delivery.setMapA(1);
        delivery.setMapB(2);
        when(mongoService.getLastDelivery()).thenReturn(delivery);

        mvc = MockMvcBuilders.standaloneSetup(deliveryController).build();
    }

    @Test
    public void testViewDeliveries() throws Exception {
        mvc.perform(get("/deliveries")).andExpect(view().name("delivery-list"));
    }

    @Test
    public void testViewCreateDelivery() throws Exception {
        mvc.perform(get("/deliveries/put")).andExpect(view().name("delivery-manage-user"));
    }

    @Test
    public void testDeleteDelivery() throws Exception {
        mvc.perform(get("/deliveries/593bc774a8768b4ba04e67aa/delete")).andExpect(view().name("redirect:/deliveries"));
    }

}