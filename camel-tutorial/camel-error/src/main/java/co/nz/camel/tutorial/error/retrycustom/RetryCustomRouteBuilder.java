package co.nz.camel.tutorial.error.retrycustom;

import javax.annotation.Resource;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import co.nz.camel.tutorial.error.shared.FlakyProcessor;

@Component
public class RetryCustomRouteBuilder extends RouteBuilder {

	@Resource
	private RetryCustomProcessor retryCustomProcessor;

	@Override
	public void configure() throws Exception {
		errorHandler(defaultErrorHandler().onRedelivery(retryCustomProcessor)
				.maximumRedeliveries(2));

		from("direct:retrycustomStart").bean(FlakyProcessor.class).to(
				"mock:retrycustomResult");
	}
}
