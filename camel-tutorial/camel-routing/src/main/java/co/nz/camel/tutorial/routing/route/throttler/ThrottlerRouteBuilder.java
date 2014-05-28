package co.nz.camel.tutorial.routing.route.throttler;

import org.apache.camel.builder.RouteBuilder;

public class ThrottlerRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:trtstart").to("mock:trtUnthrottled").throttle(5)
				.timePeriodMillis(10000).to("mock:trtThrottled").end()
				.to("mock:trtAfter");
	}
}