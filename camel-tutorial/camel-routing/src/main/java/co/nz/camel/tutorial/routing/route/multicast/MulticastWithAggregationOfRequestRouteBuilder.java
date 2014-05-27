package co.nz.camel.tutorial.routing.route.multicast;

import javax.annotation.Resource;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Simple multicast example with parallel processing.
 */
@Component
public class MulticastWithAggregationOfRequestRouteBuilder extends RouteBuilder {

	@Resource
	private ConcatenatingAggregationStrategy concatenatingAggregationStrategy;

	@Override
	public void configure() throws Exception {
		from("direct:multiWarstart").enrich("direct:multiWarperformMulticast",
				concatenatingAggregationStrategy)
		// copy the In message to the Out message; this will become the route
		// response
				.transform(body());

		from("direct:multiWarperformMulticast").multicast()
				.aggregationStrategy(concatenatingAggregationStrategy)
				.to("direct:multiWarfirst").to("direct:multiWarsecond").end();

		from("direct:multiWarfirst").transform(constant("first response"));

		from("direct:multiWarsecond").transform(constant("second response"));
	}
}
