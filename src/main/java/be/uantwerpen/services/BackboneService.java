package be.uantwerpen.services;

import be.uantwerpen.controller.DeliveryController;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * This class handles all communication with the backbone.
 */
@Service
public class BackboneService {
    private static final Logger logger = LogManager.getLogger(DeliveryController.class);

    @Value("${core.ip:localhost}")
    private String serverCoreIP;

    @Value("#{new Integer(${core.port}) ?: 10000}")
    private int serverCorePort;

    private RestTemplate restTemplate = new RestTemplate();

    public int planPath(int startPid, int startMapId, int endPid, int endMapId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://" + serverCoreIP + ":" + serverCorePort + "/map/planpath")
                .queryParam("startpid", startPid)
                .queryParam("startmapid", startMapId)
                .queryParam("stoppid", endPid)
                .queryParam("stopmapid", endMapId);
        int deliveryId = 1;

        JSONParser parser = new JSONParser();
        URI test = builder.build().toUri();
        HttpEntity<String> httpResponse = restTemplate.getForEntity(builder.build().toUri(), String.class);
        Object obj = null;
        try {
            obj = parser.parse(httpResponse.getBody());
            JSONObject jsonObject = (JSONObject) obj;
            deliveryId = ((Long) jsonObject.get("deliveryid")).intValue();
        } catch (Exception e){
            logger.error("Can't read or parse file.", e);
        }

        return deliveryId;
        //return responseList.getStatusCode().is2xxSuccessful();
    }

}
