package co.nz.camel.tutorial.routing.route.recipientlist;

import org.apache.camel.builder.RouteBuilder;

/**
 * Example showing ignore invalid endpoints.
 */
public class RecipientListUnrecognizedEndpointRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:rclustart")
				.setHeader("multicastTo")
				.constant("direct:rclufirst,direct:rclusecond,websphere:cheese")
				//recipientList controlled by head 'multicastTo'
				.recipientList().header("multicastTo").ignoreInvalidEndpoints();

		from("direct:rclufirst").to("mock:rclufirst");
		from("direct:rclusecond").to("mock:rclusecond");
	}
}
