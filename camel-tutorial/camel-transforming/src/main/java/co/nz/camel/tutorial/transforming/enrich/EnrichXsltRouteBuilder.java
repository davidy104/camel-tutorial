package co.nz.camel.tutorial.transforming.enrich;

import org.apache.camel.builder.RouteBuilder;

public class EnrichXsltRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:xsltEnrichStart").enrich("xslt:book.xslt");
	}
}