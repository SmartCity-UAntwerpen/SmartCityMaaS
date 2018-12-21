package be.uantwerpen.services;

import be.uantwerpen.sc.models.Job;
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
 * Service for the Job class. All interactions will be defined here required to perform the methods for the Job class
 * Changed to get data via REST from backbone
 */
@Service
public class JobService {

    private static final Logger logger = LogManager.getLogger(JobService.class);

    @Autowired
    private RestTemplate restTemplate;

    private String basePath;

    public JobService(@Value("${core.ip}") String coreIp, @Value("${core.port}") int corePort) {
        // build URL using properties
        basePath = "http://" + coreIp + ":" + corePort + "/job/service";
    }

    public Iterable<Job> findAll() {
        String path = basePath + "/findalljobs";
        try {
            ResponseEntity<List<Job>> response = restTemplate.exchange(
                    path,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Job>>() {
                    });
            return response.getBody();
        } catch (RestClientException e) {
            logger.debug(e);
            return null;
        }
    }

    public boolean save(final Job job) {
        String path = basePath + "/savejob";

        //set your headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //set your entity to send
        HttpEntity entity = new HttpEntity<>(job, headers);

        // send it!
        try {
            restTemplate.exchange(path, HttpMethod.POST, entity, String.class);
            return true;
        } catch (RestClientException e) {
            logger.warn("Error while saving job " + job.getId());
            logger.debug(e);
            return false;
        }
    }

    public Job findOne(Long id) {
        String path = basePath + "/getjob/{id}";
        try {
            ResponseEntity<Job> exchange = restTemplate.exchange(path,
                    HttpMethod.GET,
                    null,
                    Job.class,
                    id
            );
            return exchange.getBody();
        } catch (RestClientException e) {
            logger.debug(e);
            return null;
        }
    }

    public boolean delete(Long id) {
        String path = basePath + "/deletejob/{id}";
        try {
            restTemplate.exchange(path,
                    HttpMethod.POST,
                    null,
                    String.class,
                    id
            );
            return true;
        } catch (RestClientException e) {
            logger.warn("Error while deleting job " + id);
            logger.debug(e);
            return false;
        }
    }

    public boolean deleteAll() {
        String path = basePath + "/deletealljobs";
        try {
            restTemplate.exchange(path,
                    HttpMethod.POST,
                    null,
                    String.class
            );
            return true;
        } catch (RestClientException e) {
            logger.warn("Error while deleting all jobs");
            logger.debug(e);
            return false;
        }
    }

    /**
     * Function to save all relevant information concernting the Job class. This is a standard function for service usage
     *
     * @param job (Job) class of which all information needs to be saved
     */
    public boolean saveSomeAttributes(Job job) {
        Job tempJob = job.getId() == null ? null : findOne(job.getId());
        if (tempJob != null) {
            tempJob.setIdStart(job.getIdStart());
            tempJob.setIdEnd(job.getIdEnd());
            tempJob.setStatus(job.getStatus());
            return save(tempJob);
        } else {
            return save(job);
        }
    }
}
