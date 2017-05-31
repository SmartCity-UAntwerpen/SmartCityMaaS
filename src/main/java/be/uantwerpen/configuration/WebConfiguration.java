package be.uantwerpen.configuration;

import com.mongodb.MongoClient;
import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Edwin on 22/10/2015.
 */
@Configuration
public class WebConfiguration {
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
    public MongoDbFactory mongoDbFactory() throws Exception {

        // Set credentials
        MongoClient mongoClient = new MongoClient( "143.129.39.159" , 27017 );

        // Mongo DB Factory
        SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(
                mongoClient, "local");

        return simpleMongoDbFactory;
    }

}