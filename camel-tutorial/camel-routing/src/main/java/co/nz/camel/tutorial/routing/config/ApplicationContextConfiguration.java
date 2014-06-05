package co.nz.camel.tutorial.routing.config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.ThreadPoolProfileBuilder;
import org.apache.camel.spi.ThreadPoolProfile;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import co.nz.camel.tutorial.config.CamelSpringContextConfig;
import co.nz.camel.tutorial.routing.route.cbr.ContentBasedRouterRouteBuilder;
import co.nz.camel.tutorial.routing.route.dynamicrouter.DynamicRouterRouteBuilder;
import co.nz.camel.tutorial.routing.route.filter.FilteringRouteBuilder;
import co.nz.camel.tutorial.routing.route.loadbalancer.LoadBalancerFailoverRouteBuilder;
import co.nz.camel.tutorial.routing.route.loadbalancer.LoadBalancerStickyRouteBuilder;
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
import co.nz.camel.tutorial.routing.route.routingslip.RoutingSlipRouteBuilder;
import co.nz.camel.tutorial.routing.route.throttler.ThrottlerAsyncDelayedRouteBuilder;
import co.nz.camel.tutorial.routing.route.throttler.ThrottlerDynamicRouteBuilder;
import co.nz.camel.tutorial.routing.route.throttler.ThrottlerRouteBuilder;
import co.nz.camel.tutorial.routing.route.wtr.WireTapCustomThreadPoolRouteBuilder;
import co.nz.camel.tutorial.routing.route.wtr.WireTapOnPrepareRouteBuilder;
import co.nz.camel.tutorial.routing.route.wtr.WireTapRouteBuilder;
import co.nz.camel.tutorial.routing.route.wtr.WireTapStateLeaksRouteBuilder;
import co.nz.camel.tutorial.routing.route.wtr.WireTapStateNoLeaksRouteBuilder;

@Configuration
@ComponentScan(basePackages = "co.nz.camel.tutorial.routing")
@Import(value = CamelSpringContextConfig.class)
public class ApplicationContextConfiguration {

	@Resource
	private CamelContext camelContext;

	@Resource
	private ContentBasedRouterRouteBuilder contentBasedRouterRouteBuilder;

	@Resource
	private FilteringRouteBuilder filteringRouteBuilder;

	@Resource
	private RoutingSlipRouteBuilder routingSlipRouteBuilder;

	@Resource
	private MulticastWithAggregationOfRequestRouteBuilder multicastWithAggregationOfRequestRouteBuilder;

	@Resource
	private MulticastExceptionHandlingInStrategyRouteBuilder multicastExceptionHandlingInStrategyRouteBuilder;

	@Resource
	private MulticastWithAggregationRouteBuilder multicastWithAggregationRouteBuilder;

	@Resource
	private ThrottlerAsyncDelayedRouteBuilder throttlerAsyncDelayedRouteBuilder;

	@PostConstruct
	public void initializeCamelContext() throws Exception {
		camelContext.getExecutorServiceManager().registerThreadPoolProfile(
				throttlerPoolProfile());
		camelContext.addRoutes(contentBasedRouterRouteBuilder);
		camelContext.addRoutes(filteringRouteBuilder);
		camelContext.addRoutes(routingSlipRouteBuilder);
		initialWireTapRoutes();
		initialMulticastRoutes();
		initialRecipientlistRoutes();
		initialThrottlerRoutes();
		initialDynamicRoutes();
		initialLoadbalancerRoutes();
	}

	private void initialWireTapRoutes() throws Exception {
		camelContext.addRoutes(new WireTapRouteBuilder());
		camelContext.addRoutes(new WireTapStateLeaksRouteBuilder());
		camelContext.addRoutes(new WireTapStateNoLeaksRouteBuilder());
		camelContext.addRoutes(new WireTapOnPrepareRouteBuilder());
		camelContext.addRoutes(new WireTapCustomThreadPoolRouteBuilder());
	}

	private void initialMulticastRoutes() throws Exception {
		camelContext.addRoutes(new MulticastRouteBuilder());
		camelContext.addRoutes(new MulticastParallelProcessingRouteBuilder());
		camelContext
				.addRoutes(multicastExceptionHandlingInStrategyRouteBuilder);
		camelContext.addRoutes(new MulticastStopOnExceptionRouteBuilder());
		camelContext.addRoutes(new MulticastShallowCopyRouteBuilder());

		camelContext.addRoutes(multicastWithAggregationRouteBuilder);
		camelContext.addRoutes(new MulticastTimeoutRouteBuilder());
		camelContext.addRoutes(multicastWithAggregationOfRequestRouteBuilder);
	}

	private void initialRecipientlistRoutes() throws Exception {
		camelContext.addRoutes(new RecipientListRouteBuilder());
		camelContext
				.addRoutes(new RecipientListUnrecognizedEndpointRouteBuilder());
	}

	private void initialThrottlerRoutes() throws Exception {
		camelContext.addRoutes(new ThrottlerRouteBuilder());
		camelContext.addRoutes(new ThrottlerDynamicRouteBuilder());
		camelContext.addRoutes(throttlerAsyncDelayedRouteBuilder);
	}

	private void initialDynamicRoutes() throws Exception {
		camelContext.addRoutes(new DynamicRouterRouteBuilder());

	}

	private void initialLoadbalancerRoutes() throws Exception {
		camelContext.addRoutes(new LoadBalancerFailoverRouteBuilder());
		camelContext.addRoutes(new LoadBalancerStickyRouteBuilder());

	}

	private ThreadPoolProfile throttlerPoolProfile() {
		ThreadPoolProfileBuilder builder = new ThreadPoolProfileBuilder(
				"myThrottler");
		builder.maxQueueSize(5);
		return builder.build();
	}
}
