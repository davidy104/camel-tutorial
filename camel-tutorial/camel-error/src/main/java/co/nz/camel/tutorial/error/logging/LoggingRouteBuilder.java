package co.nz.camel.tutorial.error.logging;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import co.nz.camel.tutorial.error.shared.FlakyProcessor;

public class LoggingRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		errorHandler(loggingErrorHandler().logName("MyLoggingErrorHandler")
				.level(LoggingLevel.ERROR));

		from("direct:loggingStart").bean(FlakyProcessor.class).to(
				"mock:loggingResult");

		from("direct:loggingRouteSpecific")
				.errorHandler(
						loggingErrorHandler().logName("MyRouteSpecificLogging")
								.level(LoggingLevel.ERROR))
				.bean(FlakyProcessor.class).to("mock:loggingResult");
	}
}