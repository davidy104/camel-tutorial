package co.nz.camel.tutorial.error.exception;

import org.apache.camel.builder.RouteBuilder;

import co.nz.camel.tutorial.error.shared.FlakyException;
import co.nz.camel.tutorial.error.shared.FlakyProcessor;
import co.nz.camel.tutorial.error.shared.SporadicException;

public class ExceptionRouteBuilder extends RouteBuilder {
	@SuppressWarnings("unchecked")
	@Override
	public void configure() throws Exception {
		onException(FlakyException.class, SporadicException.class).to(
				"mock:exceptionError");
		onException(Exception.class).to("mock:exceptionGenericerror");

		from("direct:exceptionStart").bean(FlakyProcessor.class).to(
				"mock:exceptionResult");

		from("direct:exceptionHandled").onException(FlakyException.class)
				.handled(true).transform(constant("Something Bad Happened!"))
				.to("mock:handleExceptionError").end()
				.bean(FlakyProcessor.class).transform(constant("All Good"))
				.to("mock:exceptionResult");

		from("direct:exceptionContinue").onException(FlakyException.class)
				.continued(true).to("mock:exceptionIgnore").end()
				.bean(FlakyProcessor.class).transform(constant("All Good"))
				.to("mock:exceptionResult");
	}
}
