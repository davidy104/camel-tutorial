package co.nz.camel.tutorial.error.retryconditional;

import org.apache.camel.builder.RouteBuilder;

import co.nz.camel.tutorial.error.shared.SporadicProcessor;

public class RetryConditionalRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		errorHandler(defaultErrorHandler()
				.retryWhile(
						simple("${header.CamelRedeliveryCounter} < 2 or ${date:now:EEE} contains 'Tue'")));

		from("direct:retryconditionalStart").bean(SporadicProcessor.class).to(
				"mock:retryconditionalResult");
	}
}
