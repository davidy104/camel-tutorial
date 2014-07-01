package co.nz.camel.tutorial.splitjoin.route.split;

import org.apache.camel.builder.RouteBuilder;

class SplitSimpleExpressionRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:in").split(simple("${body.wrapped}")).to("mock:out").end();
	}
}
