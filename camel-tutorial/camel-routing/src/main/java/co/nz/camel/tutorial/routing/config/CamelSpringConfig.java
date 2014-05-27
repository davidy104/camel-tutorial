package co.nz.camel.tutorial.routing.config;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.ThreadPoolRejectedPolicy;
import org.apache.camel.spi.ThreadPoolProfile;
import org.apache.camel.spring.CamelBeanPostProcessor;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.nz.camel.tutorial.routing.route.cbr.ContentBasedRouterRouteBuilder;
import co.nz.camel.tutorial.routing.route.filter.FilteringRouteBuilder;

@Configuration
public class CamelSpringConfig {

	@Inject
	private ApplicationContext context;

	@Resource
	private ContentBasedRouterRouteBuilder contentBasedRouterRouteBuilder;

	@Resource
	private FilteringRouteBuilder filteringRouteBuilder;

	@Bean
	public CamelBeanPostProcessor camelBeanPostProcessor() {
		CamelBeanPostProcessor camelBeanPostProcessor = new CamelBeanPostProcessor();
		camelBeanPostProcessor.setApplicationContext(context);
		return camelBeanPostProcessor;
	}

	@Bean
	public CamelContext camelContext() throws Exception {
		SpringCamelContext camelContext = new SpringCamelContext(context);
		camelContext.getExecutorServiceManager().registerThreadPoolProfile(
				genericThreadPoolProfile());
		camelContext.addRoutes(contentBasedRouterRouteBuilder);
		camelContext.addRoutes(filteringRouteBuilder);
		return camelContext;
	}

	@Bean
	public ThreadPoolProfile genericThreadPoolProfile() {
		ThreadPoolProfile profile = new ThreadPoolProfile();
		profile.setId("genericThreadPool");
		profile.setKeepAliveTime(120L);
		profile.setPoolSize(2);
		profile.setMaxPoolSize(10);
		profile.setTimeUnit(TimeUnit.SECONDS);
		profile.setRejectedPolicy(ThreadPoolRejectedPolicy.Abort);
		return profile;
	}

}
