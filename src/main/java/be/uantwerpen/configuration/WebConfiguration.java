package be.uantwerpen.configuration;

import com.mongodb.MongoClient;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.web.client.RestTemplate;
/**
 * Configuration of REST-bean and mongoDB connection.
 */
@Configuration
public class WebConfiguration {

    private static final Logger logger = LogManager.getLogger(WebConfiguration.class);

    @Bean
    ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean( new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public MongoDbFactory mongoDbFactory() {

        String host = "143.129.39.127";
        int port = 27017;
        String databaseName = "local";

        // Set credentials
        MongoClient mongoClient = new MongoClient(host, port);

        // Mongo DB Factory
        SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(mongoClient, databaseName);
        logger.info("MongoClient created on " + host + ":" + port + ", database name: " + databaseName);
        return simpleMongoDbFactory;

    }

}