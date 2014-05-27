package co.nz.camel.tutorial.routing.route.multicast;

import javax.annotation.Resource;

import org.apache.camel.builder.RouteBuilder;

/**
 * Simple multicast example with parallel processing.
 */
public class MulticastWithAggregationRouteBuilder extends RouteBuilder {

	@Resource
	private ConcatenatingAggregationStrategy concatenatingAggregationStrategy;

	@Override
	public void configure() throws Exception {
		from("direct:multiWastart").multicast()
				.aggregationStrategy(concatenatingAggregationStrategy)
				.to("direct:multiWafirst").to("direct:multiWasecond").end()
				// copy the In message to the Out message; this will become the
				// route response
				.transform(body());

		from("direct:multiWafirst").transform(constant("first response"));

		from("direct:multiWasecond").transform(constant("second response"));
	}
}