package co.nz.camel.tutorial.routing.config;

import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.nz.camel.tutorial.routing.route.dynamicrouter.DynamicRouterRouteBuilder;
import co.nz.camel.tutorial.routing.route.multicast.MulticastExceptionHandlingInStrategyRouteBuilder;
import co.nz.camel.tutorial.routing.route.multicast.MulticastParallelProcessingRouteBuilder;
import co.nz.camel.tutorial.routing.route.multicast.MulticastRouteBuilder;
import co.nz.camel.tutorial.routing.route.multicast.MulticastShallowCopyRouteBuilder;
import co.nz.camel.tutorial.routing.route.multicast.MulticastStopOnExceptionRouteBuilder;
import co.nz.camel.tutorial.routing.route.multicast.MulticastTimeoutRouteBuilder;
import co.nz.camel.tutorial.routing.route.multicast.MulticastWithAggregationOfRequestRouteBuilder;
import co.nz.camel.tutorial.routing.route.multicast.MulticastWithAggregationRouteBuilder;
import co.nz.camel.tutorial.routing.route.recipientlist.RecipientListRouteBuilder;
import co.nz.camel.tutorial.routing.route.recipientlist.RecipientListUnrecognizedEndpointRouteBuilder;
import co.nz.camel.tutorial.routing.route.throttler.ThrottlerAsyncDelayedRouteBuilder;
import co.nz.camel.tutorial.routing.route.throttler.ThrottlerDynamicRouteBuilder;
import co.nz.camel.tutorial.routing.route.throttler.ThrottlerRouteBuilder;
import co.nz.camel.tutorial.routing.route.wtr.WireTapCustomThreadPoolRouteBuilder;
import co.nz.camel.tutorial.routing.route.wtr.WireTapOnPrepareRouteBuilder;
import co.nz.camel.tutorial.routing.route.wtr.WireTapRouteBuilder;
import co.nz.camel.tutorial.routing.route.wtr.WireTapStateLeaksRouteBuilder;
import co.nz.camel.tutorial.routing.route.wtr.WireTapStateNoLeaksRouteBuilder;

/**
 * 
 * @author dyuan
 * 
 */
@Configuration
public class CamelRouteRegistryConfig {

	@Resource
	private CamelContext camelContext;

	// MulticastWithAggregationOfRequestRouteBuilder inside using spring
	// annotation to refer other object,
	// so we should not use keyword new to initial its instance
	@Resource
	private MulticastWithAggregationOfRequestRouteBuilder multicastWithAggregationOfRequestRouteBuilder;

	@Resource
	private MulticastExceptionHandlingInStrategyRouteBuilder multicastExceptionHandlingInStrategyRouteBuilder;

	@Resource
	private MulticastWithAggregationRouteBuilder multicastWithAggregationRouteBuilder;

	@Resource
	private ThrottlerAsyncDelayedRouteBuilder throttlerAsyncDelayedRouteBuilder;

	@Bean(initMethod = "initialize")
	public CamelRouteBuildersInitializer setupWireTapRoutes() throws Exception {
		return new CamelRouteBuildersInitializer(camelContext,
				new WireTapRouteBuilder(), new WireTapStateLeaksRouteBuilder(),
				new WireTapStateNoLeaksRouteBuilder(),
				new WireTapOnPrepareRouteBuilder(),
				new WireTapCustomThreadPoolRouteBuilder());
	}

	/**
	 * setup All Multicast Routes
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean(initMethod = "initialize")
	public CamelRouteBuildersInitializer setupMulticastRoutes()
			throws Exception {
		return new CamelRouteBuildersInitializer(camelContext,
				new MulticastRouteBuilder(),
				new MulticastParallelProcessingRouteBuilder(),
				multicastExceptionHandlingInStrategyRouteBuilder,
				new MulticastStopOnExceptionRouteBuilder(),
				new MulticastShallowCopyRouteBuilder(),
				multicastWithAggregationRouteBuilder,
				new MulticastTimeoutRouteBuilder(),
				multicastWithAggregationOfRequestRouteBuilder);
	}

	// recipientlist
	@Bean(initMethod = "initialize")
	public CamelRouteBuildersInitializer setupRecipientlistRoutes()
			throws Exception {
		return new CamelRouteBuildersInitializer(camelContext,
				new RecipientListRouteBuilder(),
				new RecipientListUnrecognizedEndpointRouteBuilder());
	}

	// Throttler
	@Bean(initMethod = "initialize")
	public CamelRouteBuildersInitializer setupThrottlerRoutes()
			throws Exception {
		return new CamelRouteBuildersInitializer(camelContext,
				new ThrottlerRouteBuilder(),
				new ThrottlerDynamicRouteBuilder(),
				throttlerAsyncDelayedRouteBuilder);
	}

	// DynamicRoute
	@Bean(initMethod = "initialize")
	public CamelRouteBuildersInitializer setupDynamicRoutes() throws Exception {
		return new CamelRouteBuildersInitializer(camelContext,
				new DynamicRouterRouteBuilder());
	}
}
