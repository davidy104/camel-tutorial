package co.nz.camel.tutorial.routing.route.throttler;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ThrottlerAsyncDelayedRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:trtaStart")
				.to("mock:trtaUnthrottled")
				.to("log:co.nz.camel.tutorial.routing.route.throttler.ThrottlerAsyncDelayedRouteBuilder?showHeaders=true&multiline=true")
				.throttle(5)
				.timePeriodMillis(2000)
				.asyncDelayed()
				.executorServiceRef("myThrottler")
				.setHeader("threadName", simple("${threadName}"))
				.to("mock:trtaThrottled")
				.to("log:co.nz.camel.tutorial.routing.route.throttler.ThrottlerAsyncDelayedRouteBuilder?showHeaders=true&multiline=true")
				.end().to("mock:trtaAfter");
	}
}