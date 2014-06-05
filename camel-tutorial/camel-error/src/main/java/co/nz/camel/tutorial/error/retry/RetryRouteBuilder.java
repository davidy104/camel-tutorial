package co.nz.camel.tutorial.error.retry;

import org.apache.camel.builder.RouteBuilder;

import co.nz.camel.tutorial.error.shared.SporadicProcessor;

public class RetryRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		errorHandler(defaultErrorHandler().maximumRedeliveries(2));

		from("direct:retryStart").bean(SporadicProcessor.class).to(
				"mock:retryResult");

		from("direct:retryRouteSpecific")
				.errorHandler(defaultErrorHandler().maximumRedeliveries(2))
				.bean(SporadicProcessor.class).to("mock:retryResult");

		from("direct:retryRouteSpecificDelay")
				.errorHandler(
						defaultErrorHandler().maximumRedeliveries(2)
								.redeliveryDelay(500))
				.bean(SporadicProcessor.class).to("mock:retryResult");
	}
}
