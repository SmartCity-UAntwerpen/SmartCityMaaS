package be.uantwerpen.services;

import be.uantwerpen.model.Link;
import be.uantwerpen.model.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class handles all communication with the backbone.
 */
@Service
public class BackboneService {

    @Value("${core.ip:localhost}")
    private String serverCoreIP;

    @Value("#{new Integer(${core.port}) ?: 10000}")
    private int serverCorePort;

    public List<Link> getLinks() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Link[]> responseList;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://" + serverCoreIP + ":" + serverCorePort + "/link/pathlinks")
                .queryParam("pidstart", 0)
                .queryParam("pidend", 0);
        responseList = restTemplate.getForEntity(builder.build().encode().toUri(), Link[].class);
        return Arrays.asList(responseList.getBody());
    }

    public List<Point> getPoints() {
        return getPointsFromLinks(getLinks());
    }

    public List<Point> getPointsFromLinks(List<Link> linkList) {
        return linkList.stream().flatMap(link -> Stream.of(link.getStartPoint(), link.getStopPoint())).distinct().collect(Collectors.toList());
    }

    public boolean planPath(int startPid, int startMapId, int endPid, int endMapId) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> responseList;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://" + serverCoreIP + ":" + serverCorePort + "/map/planpath")
                .queryParam("startpid", startPid)
                .queryParam("startmapid", startMapId)
                .queryParam("stoppid", endPid)
                .queryParam("stopmapid", endMapId);
        responseList = restTemplate.getForEntity(builder.build().encode().toUri(), Void.class);
        return responseList.getStatusCode().is2xxSuccessful();
    }

}
