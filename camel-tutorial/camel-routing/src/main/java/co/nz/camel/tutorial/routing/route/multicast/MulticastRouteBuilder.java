package co.nz.camel.tutorial.routing.route.multicast;

import org.apache.camel.builder.RouteBuilder;

/**
 * Simple multicast example.
 */
public class MulticastRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:multistart")
				// .autoStartup(false)
				.multicast().to("mock:multifirst").to("mock:multisecond")
				.to("mock:multithird").end();
	}
}
