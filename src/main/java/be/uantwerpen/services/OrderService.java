package be.uantwerpen.services;

import be.uantwerpen.model.APIResponse;
import be.uantwerpen.model.DBDelivery;
import be.uantwerpen.model.DBOrder;
import be.uantwerpen.model.Order;
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

import java.util.ArrayList;
import java.util.Collections;
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

    private String basePathToBackbone;

    public OrderService(@Value("${core.ip}") String coreIp, @Value("${core.port}") int corePort) {
        // build URL using properties
        basePathToBackbone = "http://" + coreIp + ":" + corePort + "/deliveries/";
    }

    /*public Iterable<Order> findAll() {
        String path = basePathToBackbone + "getall";
        try {
            ResponseEntity<List<DBDelivery>> response = restTemplate.exchange(
                    path,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<DBDelivery>>() {
                    });
            List<DBDelivery> resDeliveries = response.getBody();
            ArrayList<Order> orders = new ArrayList<Order>();
            for (DBDelivery del: resDeliveries) {
                DBOrder tempDbOrder = repository.findOne(del.getOrderID());
                if (tempDbOrder != null) {
                    orders.add(new Order(tempDbOrder, del));
                }
            }
            return orders;
        } catch (RestClientException e) {
            logger.debug(e);
            return null;
        } catch (Exception e) {
            logger.debug(e);
            e.printStackTrace();
            return null;
        }
    }*/

    public Iterable<DBOrder> getAll() {
        return repository.findAll();
    }

    public APIResponse save(final DBDelivery delivery) {
        String path = basePathToBackbone + "createDelivery";

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
            return res;
        } catch (RestClientException e) {
            logger.warn("Error while saving job " + delivery.getOrderID());
            logger.debug(e);
            return null;
        }
    }

    public Long createNewOrder() {
        DBOrder order = repository.save(new DBOrder());
        return order.getId();
    }

    public Long createNewOrderWithDescription(String description, String type) {
        DBOrder tempOrder = new DBOrder();
        tempOrder.description = description;
        tempOrder.type = type;
        DBOrder order = repository.save(tempOrder);
        return order.getId();
    }

    public DBDelivery findOne(Long id) {
        String path = basePathToBackbone + "get/{id}";
        try {
            ResponseEntity<DBDelivery> exchange = restTemplate.exchange(path,
                    HttpMethod.GET,
                    null,
                    DBDelivery.class,
                    id
            );
            return exchange.getBody();
        } catch (RestClientException e) {
            logger.debug(e);
            return null;
        }
    }

    public boolean delete(Long id) {
        String path = basePathToBackbone + "delete/{id}";
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
        String path = basePathToBackbone + "deleteall";
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
