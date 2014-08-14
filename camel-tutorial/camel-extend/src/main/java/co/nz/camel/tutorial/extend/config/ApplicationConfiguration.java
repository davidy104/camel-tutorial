package co.nz.camel.tutorial.extend.config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import co.nz.camel.tutorial.config.CamelActivemqConfig;
import co.nz.camel.tutorial.config.CamelSpringContextConfig;
import co.nz.camel.tutorial.extend.predicate.MyPredicateBeanBindingRouteBuilder;
import co.nz.camel.tutorial.extend.predicate.MyPredicateCompoundRouteBuilder;
import co.nz.camel.tutorial.extend.predicate.MyPredicateInlineRouteBuilder;
import co.nz.camel.tutorial.extend.predicate.MyPredicateRouteBuilder;

@Configuration
@ComponentScan(basePackages = "co.nz.camel.tutorial.extend")
@Import(value = { CamelSpringContextConfig.class, CamelActivemqConfig.class })
public class ApplicationConfiguration {
	@Resource
	private CamelContext camelContext;

	@Resource
	private MyPredicateRouteBuilder myPredicateRouteBuilder;

	@Resource
	private MyPredicateBeanBindingRouteBuilder myPredicateBeanBindingRouteBuilder;

	@Resource
	private MyPredicateCompoundRouteBuilder myPredicateCompoundRouteBuilder;

	@Resource
	private MyPredicateInlineRouteBuilder myPredicateInlineRouteBuilder;

	@PostConstruct
	public void initializeRoutes() throws Exception {
		camelContext.addRoutes(myPredicateRouteBuilder);
		camelContext.addRoutes(myPredicateBeanBindingRouteBuilder);
		camelContext.addRoutes(myPredicateCompoundRouteBuilder);
		camelContext.addRoutes(myPredicateInlineRouteBuilder);
	}
	
	 
}
