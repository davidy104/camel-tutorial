package co.nz.camel.tutorial.routing.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class WireTapRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:wtStart").wireTap("mock:wttapped").to("mock:wtout");
	}

}
