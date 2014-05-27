package co.nz.camel.tutorial.routing.route.multicast;

import javax.annotation.Resource;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

/**
 * Multicast example with exceptions handled in the AggregationStrategy.
 */
public class MulticastExceptionHandlingInStrategyRouteBuilder extends
		RouteBuilder {

	@Resource
	private ExceptionHandlingAggregationStrategy exceptionHandlingAggregationStrategy;

	@Override
	public void configure() throws Exception {
		from("direct:multiEhstart").multicast()
				.aggregationStrategy(exceptionHandlingAggregationStrategy)
				.to("direct:multiEhfirst").to("direct:multiEhsecond").end()
				.log("continuing with ${body}")
				.to("mock:multiEhafterMulticast").transform(body());
		// copy the In message to the Out message;
		// this will become the route response

		from("direct:multiEhfirst").onException(Exception.class)
				.log("Caught exception").to("mock:multiEhexceptionHandler")
				.transform(constant("Oops")).end().to("mock:multiEhfirst")
				.process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						throw new IllegalStateException(
								"something went horribly wrong");
					}
				});

		from("direct:multiEhsecond").to("mock:multiEhsecond").transform(
				constant("All OK here"));
	}
}
