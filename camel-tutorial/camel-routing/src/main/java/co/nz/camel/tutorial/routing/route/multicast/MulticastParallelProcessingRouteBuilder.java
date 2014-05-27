package co.nz.camel.tutorial.routing.route.multicast;

import org.apache.camel.builder.RouteBuilder;

/**
 * Simple multicast example with parallel processing.
 */
public class MulticastParallelProcessingRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:multiPstart").multicast().parallelProcessing()
				.to("direct:multiPfirst").to("direct:multiPsecond").end()
				.setHeader("threadName").simple("${threadName}")
				.to("mock:multiPafterMulticast")
				.transform(constant("response"));

		from("direct:multiPfirst").setHeader("firstModifies").constant("apple")
				.setHeader("threadName").simple("${threadName}")
				.to("mock:multiPfirst");

		from("direct:multiPsecond").setHeader("secondModifies")
				.constant("banana").setHeader("threadName")
				.simple("${threadName}").to("mock:multiPsecond");
	}
}
