package co.nz.camel.tutorial.routing.route.recipientlist;

import org.apache.camel.builder.RouteBuilder;

/**
 * Simple RecipientList example.
 */
public class RecipientListRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:rclstart").setHeader("endpointsToBeTriggered")
				.method(MessageRouter.class, "getEndpointsToRouteMessageTo")
				//recipientList controlled by head 'endpointsToBeTriggered'
				.recipientList(header("endpointsToBeTriggered"));

		from("direct:order.priority").to("mock:order.priority");
		from("direct:order.normal").to("mock:order.normal");
		from("direct:billing").to("mock:billing");
		from("direct:unrecognized").to("mock:unrecognized");
	}
}
