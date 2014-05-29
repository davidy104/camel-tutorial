package co.nz.camel.tutorial.routing.route.routingslip;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class RoutingSlipRouteBuilder extends RouteBuilder {
	public final static String ROUTING_SLIP_HEADER = "myRoutingSlipHeader";

	@Override
	public void configure() throws Exception {
		from("direct:rsStart").routingSlip(header(ROUTING_SLIP_HEADER));

		from("direct:rsOther").to("mock:rsOther");
	}
}
