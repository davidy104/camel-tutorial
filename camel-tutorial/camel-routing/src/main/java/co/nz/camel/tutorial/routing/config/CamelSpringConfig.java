package co.nz.camel.tutorial.routing.config;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.ThreadPoolRejectedPolicy;
import org.apache.camel.builder.ThreadPoolProfileBuilder;
import org.apache.camel.spi.ThreadPoolProfile;
import org.apache.camel.spring.CamelBeanPostProcessor;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.nz.camel.tutorial.routing.route.cbr.ContentBasedRouterRouteBuilder;
import co.nz.camel.tutorial.routing.route.filter.FilteringRouteBuilder;
import co.nz.camel.tutorial.routing.route.routingslip.RoutingSlipRouteBuilder;

@Configuration
public class CamelSpringConfig {

	@Inject
	private ApplicationContext context;

	@Resource
	private ContentBasedRouterRouteBuilder contentBasedRouterRouteBuilder;

	@Resource
	private FilteringRouteBuilder filteringRouteBuilder;

	@Resource
	private RoutingSlipRouteBuilder routingSlipRouteBuilder;

	@Bean
	public CamelBeanPostProcessor camelBeanPostProcessor() {
		CamelBeanPostProcessor camelBeanPostProcessor = new CamelBeanPostProcessor();
		camelBeanPostProcessor.setApplicationContext(context);
		return camelBeanPostProcessor;
	}

	@Bean
	public CamelContext camelContext() throws Exception {
		SpringCamelContext camelContext = new SpringCamelContext(context);
		camelContext.getExecutorServiceManager().setDefaultThreadPoolProfile(
				genericThreadPoolProfile());
		camelContext.getExecutorServiceManager().registerThreadPoolProfile(
				throttlerPoolProfile());
		camelContext.addRoutes(contentBasedRouterRouteBuilder);
		camelContext.addRoutes(filteringRouteBuilder);
		camelContext.addRoutes(routingSlipRouteBuilder);
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

	@Bean
	public ThreadPoolProfile throttlerPoolProfile() {
		ThreadPoolProfileBuilder builder = new ThreadPoolProfileBuilder(
				"myThrottler");
		builder.maxQueueSize(5);
		return builder.build();
	}

}
