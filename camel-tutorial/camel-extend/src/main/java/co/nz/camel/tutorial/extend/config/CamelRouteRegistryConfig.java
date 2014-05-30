package co.nz.camel.tutorial.extend.config;

import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.nz.camel.tutorial.extend.predicate.MyPredicateBeanBindingRouteBuilder;
import co.nz.camel.tutorial.extend.predicate.MyPredicateCompoundRouteBuilder;
import co.nz.camel.tutorial.extend.predicate.MyPredicateInlineRouteBuilder;
import co.nz.camel.tutorial.extend.predicate.MyPredicateRouteBuilder;

/**
 * 
 * @author dyuan
 * 
 */
@Configuration
public class CamelRouteRegistryConfig {

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

	@Bean(initMethod = "initialize")
	public CamelRouteBuildersInitializer setupPredicatedRoutes()
			throws Exception {
		return new CamelRouteBuildersInitializer(camelContext,
				myPredicateRouteBuilder, myPredicateBeanBindingRouteBuilder,
				myPredicateCompoundRouteBuilder, myPredicateInlineRouteBuilder);
	}

}
