package be.uantwerpen.services;

import be.uantwerpen.sc.models.Job;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobServiceTest {

    private JobService jobService;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        jobService = new JobService("localhost", 9000);
        ReflectionTestUtils.setField(jobService, "restTemplate", restTemplate);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void findAll() throws JsonProcessingException {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("http://localhost:9000/job/service/findalljobs")
                .build();
        Job[] jobs = new Job[1];
        jobs[0] = new Job();
        mockServer.expect(requestTo(uriComponents.toUri()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(jobs), MediaType.APPLICATION_JSON));
        assertNotNull(jobService.findAll());
        mockServer.verify();
    }

    @Test
    public void findAllFail() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("http://localhost:9000/job/service/findalljobs")
                .build();
        mockServer.expect(requestTo(uriComponents.toUri()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());
        assertNull(jobService.findAll());
        mockServer.verify();
    }

    @Test
    public void save() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("http://localhost:9000/job/service/savejob")
                .build();
        mockServer.expect(requestTo(uriComponents.toUri()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());
        assertTrue(jobService.save(new Job()));
        mockServer.verify();
    }

    @Test
    public void saveFail() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("http://localhost:9000/job/service/savejob")
                .build();
        mockServer.expect(requestTo(uriComponents.toUri()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withServerError());
        assertFalse(jobService.save(new Job()));
        mockServer.verify();
    }

    @Test
    public void findOne() throws JsonProcessingException {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("http://localhost:9000/job/service/getjob/5")
                .build();
        Job job = new Job();
        job.setId(5L);
        mockServer.expect(requestTo(uriComponents.toUri()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(job), MediaType.APPLICATION_JSON));
        assertNotNull(jobService.findOne(5L));
        mockServer.verify();
    }

    @Test
    public void findOneFail() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("http://localhost:9000/job/service/getjob/5")
                .build();
        mockServer.expect(requestTo(uriComponents.toUri()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());
        assertNull(jobService.findOne(5L));
        mockServer.verify();
    }

    @Test
    public void delete() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("http://localhost:9000/job/service/deletejob/5")
                .build();
        mockServer.expect(requestTo(uriComponents.toUri()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());
        assertTrue(jobService.delete(5L));
        mockServer.verify();
    }

    @Test
    public void deleteFail() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("http://localhost:9000/job/service/deletejob/5")
                .build();
        mockServer.expect(requestTo(uriComponents.toUri()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withServerError());
        assertFalse(jobService.delete(5L));
        mockServer.verify();
    }

    @Test
    public void deleteAll() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("http://localhost:9000/job/service/deletealljobs")
                .build();
        mockServer.expect(requestTo(uriComponents.toUri()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());
        assertTrue(jobService.deleteAll());
        mockServer.verify();
    }

    @Test
    public void deleteAllFail() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("http://localhost:9000/job/service/deletealljobs")
                .build();
        mockServer.expect(requestTo(uriComponents.toUri()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withServerError());
        assertFalse(jobService.deleteAll());
        mockServer.verify();
    }

    @Test
    public void saveSomeAttributesExistingJob() throws JsonProcessingException {
        UriComponents uriFind = UriComponentsBuilder.fromHttpUrl("http://localhost:9000/job/service/getjob/5")
                .build();
        UriComponents uriSave = UriComponentsBuilder.fromHttpUrl("http://localhost:9000/job/service/savejob")
                .build();
        Job job = new Job();
        job.setId(5L);
        mockServer.expect(requestTo(uriFind.toUri()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(job), MediaType.APPLICATION_JSON));
        mockServer.expect(requestTo(uriSave.toUri()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());
        assertTrue(jobService.saveSomeAttributes(job));
        mockServer.verify();
    }

    @Test
    public void saveSomeAttributesNewJob() {
        UriComponents uriSave = UriComponentsBuilder.fromHttpUrl("http://localhost:9000/job/service/savejob")
                .build();
        mockServer.expect(requestTo(uriSave.toUri()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());
        Job job = new Job();
        assertTrue(jobService.saveSomeAttributes(job));
        mockServer.verify();
    }
}