package be.uantwerpen.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BackboneServiceTest {

    private BackboneService backboneService;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @Before
    public void setUp() {
        backboneService = new BackboneService();
        ReflectionTestUtils.setField(backboneService, "serverCoreIP", "localhost");
        ReflectionTestUtils.setField(backboneService, "serverCorePort", 9000);
        ReflectionTestUtils.setField(backboneService, "restTemplate", restTemplate);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void planPath() {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("http://localhost:9000/map/planpath")
                .queryParam("startpid", 40)
                .queryParam("startmapid", 1)
                .queryParam("stoppid", 41)
                .queryParam("stopmapid", 2)
                .build();
        mockServer.expect(requestTo(uriComponents.toUri()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess());
        backboneService.planPath(40, 1, 41, 2);
        mockServer.verify();
    }
}