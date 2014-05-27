package co.nz.camel.tutorial.routing.route.wtr;

import java.util.concurrent.ExecutorService;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.ThreadPoolBuilder;

/**
 * Using a custom thread pool with a wiretap.
 */
public class WireTapCustomThreadPoolRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		ThreadPoolBuilder builder = new ThreadPoolBuilder(getContext());
		ExecutorService oneThreadOnly = builder.poolSize(1).maxPoolSize(1)
				.maxQueueSize(100).build("JustMeDoingTheTapping");

		from("direct:wtctstart")
				// .autoStartup(false)
				.wireTap("direct:wtcttapped").executorService(oneThreadOnly)
				.to("mock:wtctout");

		from("direct:wtcttapped").setHeader("threadName")
				.simple("${threadName}").to("mock:wtcttapped");
	}
}