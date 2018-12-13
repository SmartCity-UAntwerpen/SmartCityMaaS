package be.uantwerpen.services;

import be.uantwerpen.model.Job;
import be.uantwerpen.model.JobList;
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

    public Job[] getJobs() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("http://" + serverCoreIP + ":" + serverCorePort + "/job/service/findalljobs", Job[].class);
    }

    public Job getJobById(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("http://" + serverCoreIP + ":" + serverCorePort + "/job/service/getjob/{id}", Job.class, id);
    }

    public void deleteJobById(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject("http://" + serverCoreIP + ":" + serverCorePort + "/job/service/deletejob/{id}", null, Void.class, id);
    }

    public void deleteAllJobs() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject("http://" + serverCoreIP + ":" + serverCorePort + "/job/service/deletealljobs", null, Void.class);
    }

    public boolean saveJobOrder(JobList jobList) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity("http://" + serverCoreIP + ":" + serverCorePort + "/job/saveorder", jobList, String.class);
        return stringResponseEntity.getStatusCode().is2xxSuccessful();
    }

    public boolean dispatch(JobList joblist) {
        // TODO
        return false;
    }

}
