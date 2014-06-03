package co.nz.camel.tutorial.transforming.simple;

import org.apache.camel.builder.RouteBuilder;

public class SimpleRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:simpleStart").transform(simple("Hello ${body}"));
	}
}
