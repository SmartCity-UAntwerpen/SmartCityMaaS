package be.uantwerpen.services;

import be.uantwerpen.sc.models.JobList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobListServiceTest {

    private JobListService jobListService;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        jobListService = new JobListService("localhost", 9000);
        ReflectionTestUtils.setField(jobListService, "restTemplate", restTemplate);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void findAll() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("http://localhost:9000/jobs/findAllJobLists")
                .build();
        mockServer.expect(requestTo(uriComponents.toUri()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess());
        jobListService.findAll();
        mockServer.verify();
    }

    @Test
    public void findAllFail() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("http://localhost:9000/jobs/findAllJobLists")
                .build();
        mockServer.expect(requestTo(uriComponents.toUri()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());
        assertNull(jobListService.findAll());
        mockServer.verify();
    }

    @Test
    public void findOneByDelivery() throws JsonProcessingException {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("http://localhost:9000/jobs/findOneByDelivery/507f191e810c19729de860ea")
                .build();
        JobList jobList = new JobList();
        jobList.setIdDelivery("507f191e810c19729de860ea");
        mockServer.expect(requestTo(uriComponents.toUri()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(jobList), MediaType.APPLICATION_JSON));
        assertNotNull(jobListService.findOneByDelivery("507f191e810c19729de860ea"));
        mockServer.verify();
    }

    @Test
    public void findOneByDeliveryFail() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("http://localhost:9000/jobs/findOneByDelivery/507f191e810c19729de860ea")
                .build();
        mockServer.expect(requestTo(uriComponents.toUri()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());
        assertNull(jobListService.findOneByDelivery("507f191e810c19729de860ea"));
        mockServer.verify();
    }

    @Test
    public void deleteOrder() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("http://localhost:9000/jobs/deleteOrder/5")
                .build();
        mockServer.expect(requestTo(uriComponents.toUri()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());
        assertTrue(jobListService.deleteOrder(5L));
        mockServer.verify();
    }

    @Test
    public void deleteOrderFail() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("http://localhost:9000/jobs/deleteOrder/5")
                .build();
        mockServer.expect(requestTo(uriComponents.toUri()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withServerError());
        assertFalse(jobListService.deleteOrder(5L));
        mockServer.verify();
    }

    @Test
    public void deleteAll() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("http://localhost:9000/jobs/deleteAllLists")
                .build();
        mockServer.expect(requestTo(uriComponents.toUri()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());
        assertTrue(jobListService.deleteAll());
        mockServer.verify();
    }

    @Test
    public void deleteAllFail() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("http://localhost:9000/jobs/deleteAllLists")
                .build();
        mockServer.expect(requestTo(uriComponents.toUri()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withServerError());
        assertFalse(jobListService.deleteAll());
        mockServer.verify();
    }
}