package be.uantwerpen.services;

import be.uantwerpen.sc.models.JobList;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;


/**
 * Service in which JobList (Orders) will be saved as well as all functions needed to dispatch jobs to the cores will be foreseen.
 * NV 2018
 */
@Service
public class JobListService {
    private static final Logger logger = LogManager.getLogger(JobListService.class);

    @Autowired
    private RestTemplate restTemplate;

    private String basePath;

    public JobListService(@Value("${core.ip}") String coreIp, @Value("${core.port}") int corePort) {
        // Build URL using properties
        basePath = "http://" + coreIp + ":" + corePort + "/jobs";
    }

    public Iterable<JobList> findAll() {
        String path = basePath + "/findAllJobLists";
        try {
            ResponseEntity<List<JobList>> response = restTemplate.exchange(
                    path,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<JobList>>() {
                    });
            return response.getBody();
        } catch (RestClientException e) {
            logger.debug(e);
            return null;
        }
    }

    public JobList findOneByDelivery(String deliveryId) {
        String path = basePath + "/findOneByDelivery/" + deliveryId;
        try {
            ResponseEntity<JobList> response = restTemplate.exchange(
                    path,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<JobList>() {
                    });
            return response.getBody();
        } catch (RestClientException e) {
            logger.debug(e);
            return null;
        }
    }

    public boolean saveOrder(final JobList joblist) {
        String path = basePath + "/saveOrder";

        //Set your headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //Set your entity to send
        HttpEntity entity = new HttpEntity<>(joblist, headers);

        //Send it!
        try {
            restTemplate.exchange(path, HttpMethod.POST, entity, String.class);
            return true;
        } catch (RestClientException e) {
            logger.warn("Error while saving joblist " + joblist.getId());
            logger.debug(e);
            return false;
        }
    }

    /**
     * delete an order
     *
     * @param id (long) id from the order that needs to be deleted
     */
    public boolean deleteOrder(long id) {
        String path = basePath + "/deleteOrder/{id}";
        try {
            restTemplate.exchange(path,
                    HttpMethod.POST,
                    null,
                    String.class,
                    id
            );
            return true;
        } catch (RestClientException e) {
            logger.warn("Error while deleting jobList " + id);
            return false;
        }
    }

    public boolean deleteAll() {
        String path = basePath + "/deleteAllLists";
        try {
            restTemplate.exchange(path,
                    HttpMethod.POST,
                    null,
                    String.class
            );
            return true;
        } catch (RestClientException e) {
            logger.warn("Error while deleting all job lists and jobs");
            return false;
        }
    }

}



