package co.nz.camel.tutorial.routing.config;

import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.nz.camel.tutorial.routing.route.multicast.MulticastExceptionHandlingInStrategyRouteBuilder;
import co.nz.camel.tutorial.routing.route.multicast.MulticastParallelProcessingRouteBuilder;
import co.nz.camel.tutorial.routing.route.multicast.MulticastRouteBuilder;
import co.nz.camel.tutorial.routing.route.multicast.MulticastShallowCopyRouteBuilder;
import co.nz.camel.tutorial.routing.route.multicast.MulticastStopOnExceptionRouteBuilder;
import co.nz.camel.tutorial.routing.route.multicast.MulticastTimeoutRouteBuilder;
import co.nz.camel.tutorial.routing.route.multicast.MulticastWithAggregationRouteBuilder;
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
				new MulticastExceptionHandlingInStrategyRouteBuilder(),
				new MulticastStopOnExceptionRouteBuilder(),
				new MulticastShallowCopyRouteBuilder(),
				new MulticastWithAggregationRouteBuilder(),
				new MulticastTimeoutRouteBuilder());
	}
}
