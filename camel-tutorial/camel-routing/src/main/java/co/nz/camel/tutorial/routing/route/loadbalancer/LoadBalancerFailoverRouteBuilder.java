package co.nz.camel.tutorial.routing.route.loadbalancer;

import org.apache.camel.builder.RouteBuilder;

public class LoadBalancerFailoverRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:lbfStart").loadBalance().failover(-1, false, true)
				.to("mock:lbfFirst").to("direct:lbfSecond").to("mock:lbfThird")
				.end().to("mock:lbfOut");

		from("direct:lbfSecond").throwException(
				new IllegalStateException("oops"));
	}
}