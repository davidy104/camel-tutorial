package co.nz.camel.tutorial.error.synchronizations;

import org.apache.camel.builder.RouteBuilder;

/**
 * Route that demonstrates the use of Synchronizations to define completion
 * logic on-the-fly.
 */
public class DynamicOnCompletionRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		from("direct:synchronizationsIn")
				.process(new ConfirmCancelProcessor())
				.choice()
				.when(simple("${body} contains 'explode'"))
				.throwException(
						new IllegalArgumentException(
								"Exchange caused explosion")).endChoice()
				.log("Processed message");
	}
}
