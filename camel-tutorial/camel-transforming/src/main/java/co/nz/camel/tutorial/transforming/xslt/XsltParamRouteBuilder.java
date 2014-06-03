package co.nz.camel.tutorial.transforming.xslt;

import org.apache.camel.builder.RouteBuilder;

public class XsltParamRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:xsltpStart").to("xslt:book-param.xslt");
	}
}