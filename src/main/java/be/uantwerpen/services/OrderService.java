package be.uantwerpen.services;

import be.uantwerpen.model.APIResponse;
import be.uantwerpen.model.Delivery;
import be.uantwerpen.repositories.OrderRepository;
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
public class OrderService {

    private static final Logger logger = LogManager.getLogger(OrderService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrderRepository repository;

    private String basePath;

    public OrderService(@Value("${core.ip}") String coreIp, @Value("${core.port}") int corePort) {
        // build URL using properties
        basePath = "http://" + coreIp + ":" + corePort + "/deliveries/";
    }

    public Iterable<Delivery> findAll() {
        String path = basePath + "getall";
        try {
            ResponseEntity<List<Delivery>> response = restTemplate.exchange(
                    path,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Delivery>>() {
                    });
            return response.getBody();
        } catch (RestClientException e) {
            logger.debug(e);
            return null;
        }
    }

    public boolean save(final Delivery delivery) {
        String path = basePath + "savedelivery";
        //set your headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //set your entity to send
        HttpEntity object = new HttpEntity<>(delivery, headers);

        // send it!
        try {
           APIResponse res = restTemplate.exchange(path, HttpMethod.POST, object, APIResponse.class).getBody();
           if (res.success) {
               logger.info(res.message);
           } else {
               logger.error(res.message);
           }
           return res.success;
        } catch (RestClientException e) {
            logger.warn("Error while saving job " + delivery.getId());
            logger.debug(e);
            return false;
        }
    }

    public Delivery findOne(Long id) {
        String path = basePath + "get/{id}";
        try {
            ResponseEntity<Delivery> exchange = restTemplate.exchange(path,
                    HttpMethod.GET,
                    null,
                    Delivery.class,
                    id
            );
            return exchange.getBody();
        } catch (RestClientException e) {
            logger.debug(e);
            return null;
        }
    }

    public boolean delete(Long id) {
        String path = basePath + "delete/{id}";
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
        String path = basePath + "deleteall";
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

}
