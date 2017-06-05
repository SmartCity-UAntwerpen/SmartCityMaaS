package be.uantwerpen.configuration;

import com.mongodb.MongoClient;
import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.web.client.RestTemplate;

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
    // IP address public centos VM : 143.129.39.159
    // IP address for proxy server : 172.10.0.8
    // Windows VM on own PC : 192.168.10.2
    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {

        // Set credentials
        MongoClient mongoClient = new MongoClient( "192.168.10.2" , 27017 );

        // Mongo DB Factory
        SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(
                mongoClient, "local");

        return simpleMongoDbFactory;
    }

}