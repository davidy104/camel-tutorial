package co.nz.camel.tutorial.transforming.xslt;

import org.apache.camel.builder.RouteBuilder;

public class XsltRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:xsltStart").to("xslt:book.xslt");
	}
}