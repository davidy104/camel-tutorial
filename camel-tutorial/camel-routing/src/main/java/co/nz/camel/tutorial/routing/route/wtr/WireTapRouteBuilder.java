package co.nz.camel.tutorial.routing.route.wtr;

import org.apache.camel.builder.RouteBuilder;

public class WireTapRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:wtStart")
		// .autoStartup(false)
				.wireTap("mock:wttapped").to("mock:wtout");
	}

}
