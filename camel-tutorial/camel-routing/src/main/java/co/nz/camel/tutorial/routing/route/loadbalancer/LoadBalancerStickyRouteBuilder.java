package co.nz.camel.tutorial.routing.route.loadbalancer;

import org.apache.camel.builder.RouteBuilder;

public class LoadBalancerStickyRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:lbStart").loadBalance().sticky(header("customerId"))
				.to("mock:lbFirst").to("mock:lbSecond").to("mock:lbThird")
				.end().to("mock:lbOut");
	}
}
