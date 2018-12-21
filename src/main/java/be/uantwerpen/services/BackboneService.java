package be.uantwerpen.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * This class handles all communication with the backbone.
 */
@Service
public class BackboneService {

    @Value("${core.ip:localhost}")
    private String serverCoreIP;

    @Value("#{new Integer(${core.port}) ?: 10000}")
    private int serverCorePort;

    private RestTemplate restTemplate = new RestTemplate();

    public boolean planPath(int startPid, int startMapId, int endPid, int endMapId) {
        ResponseEntity<Void> responseList;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://" + serverCoreIP + ":" + serverCorePort + "/map/planpath")
                .queryParam("startpid", startPid)
                .queryParam("startmapid", startMapId)
                .queryParam("stoppid", endPid)
                .queryParam("stopmapid", endMapId);
        responseList = restTemplate.getForEntity(builder.build().toUri(), Void.class);
        return responseList.getStatusCode().is2xxSuccessful();
    }

}
