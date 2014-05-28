package co.nz.camel.tutorial.routing.route.throttler;

import org.apache.camel.builder.RouteBuilder;

public class ThrottlerDynamicRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:trtdStart").to("mock:trtdUnthrottled")
				.throttle(header("ThrottleRate")).timePeriodMillis(10000)
				.to("mock:trtdThrottled").end().to("mock:trtdAfter");
	}
}