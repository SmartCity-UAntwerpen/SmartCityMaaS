package be.uantwerpen;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@SpringBootApplication
/*
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})*/
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
public class SupahuberApplication extends WebMvcConfigurerAdapter {

	private static final Logger logger = LogManager.getLogger(SupahuberApplication.class);
	public static void main(String[] args) {
		logger.info("SpringApplication MaaS started.");
		SpringApplication.run(SupahuberApplication.class, args);
		logger.info("SpringApplication MaaS is ready for use.");

		/*System.out.println("#### AStar ####");
		Astar astar = new Astar();
		astar.startAStar();
		System.out.println("#### end AStar ####");
		System.out.println("starting request");
		GraphBuilder graphBuilder = new GraphBuilder();
		ArrayList<Cost> costs = graphBuilder.testRequests((long)1, (long)2);
		for(Cost cost: costs)
		{
			System.out.println("status: " + cost.isStatus() + ", weight: " + cost.getWeight() + ", weightToStart: " + cost.getWeightToStart() + ", vehicleID: " + cost.getIdVehicle());
		}
		System.out.println("the end");*/

	}
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.US);
		return slr;
	}
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

	/*
	* Stores registrations of resource handlers for serving static
	* resources such as images, css files and others through Spring
	* MVC including setting cache headers optimized for efficient
	* loading in a web browser. Resources can be served out of
	* locations under web application root, from the classpath, and
	* others.
	*
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		if (!registry.hasMappingForPattern("/webjars/**")) {
			registry.addResourceHandler("/webjars/**").addResourceLocations(
					"classpath:/META-INF/resources/webjars/");
		}
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry)
	{
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/access").setViewName("access");
	}

	/*
	@Bean
	public MongoDbFactory mongoDbFactory() throws Exception {

		// Set credentials
		MongoClient mongoClient = new MongoClient( "143.129.39.159" , 27017 );

		// Mongo DB Factory
		SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(
				mongoClient, "local");

		return simpleMongoDbFactory;
	}
*/

}
