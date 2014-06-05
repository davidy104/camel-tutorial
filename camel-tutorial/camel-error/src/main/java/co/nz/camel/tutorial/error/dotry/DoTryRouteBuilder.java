package co.nz.camel.tutorial.error.dotry;

import org.apache.camel.builder.RouteBuilder;

import co.nz.camel.tutorial.error.shared.FlakyException;
import co.nz.camel.tutorial.error.shared.FlakyProcessor;

public class DoTryRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:dotryStart").to("mock:dotryBefore").doTry()
				.bean(FlakyProcessor.class).transform(constant("Made it!"))
				.doCatch(FlakyException.class).to("mock:dotryError")
				.transform(constant("Something Bad Happened!")).doFinally()
				.to("mock:dotryFinally").end().to("mock:dotryAfter");

		from("direct:dotryUnhandled").to("mock:dotryBefore").doTry()
				.bean(FlakyProcessor.class).transform(constant("Made it!"))
				.doCatch(FlakyException.class).handled(false)
				.to("mock:dotryError")
				.transform(constant("Something Bad Happened!")).doFinally()
				.to("mock:dotryFinally").end().to("mock:dotryAfter");

		from("direct:dotryConditional").to("mock:dotryBefore").doTry()
				.bean(FlakyProcessor.class).transform(constant("Made it!"))
				.doCatch(FlakyException.class).onWhen(header("jedi").isNull())
				.to("mock:dotryError")
				.transform(constant("Something Bad Happened!")).doFinally()
				.to("mock:dotryFinally").end().to("mock:dotryAfter");
	}
}