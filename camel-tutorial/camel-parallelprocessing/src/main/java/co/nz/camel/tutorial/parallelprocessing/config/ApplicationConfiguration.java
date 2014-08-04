package co.nz.camel.tutorial.parallelprocessing.config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import co.nz.camel.tutorial.config.CamelSpringContextConfig;
import co.nz.camel.tutorial.parallelprocessing.route.CustomThreadPoolProfileRouteBuilder;

@Configuration
@ComponentScan(basePackages = "co.nz.camel.tutorial.parallelprocessing")
@Import(value = { CamelSpringContextConfig.class })
public class ApplicationConfiguration {

	@Resource
	private CamelContext camelContext;

	@Resource
	private CustomThreadPoolProfileRouteBuilder customThreadPoolProfileRouteBuilder;

	@PostConstruct
	public void initializeCamelContext() throws Exception {
		SpringCamelContext springCamelContext = (SpringCamelContext) camelContext;
		springCamelContext.addRoutes(customThreadPoolProfileRouteBuilder);

	}
}
