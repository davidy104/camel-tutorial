package co.nz.camel.tutorial.routing.route.multicast;

import org.apache.camel.builder.RouteBuilder;

/**
 * Simple multicast example with timeout.
 */
public class MulticastTimeoutRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:multiTostart").multicast().parallelProcessing()
				.timeout(3000).to("direct:multiTofirst")
				.to("direct:multiTosecond").end().setHeader("threadName")
				.simple("${threadName}").to("mock:multiToafterMulticast")
				.transform(constant("response"));

		from("direct:multiTofirst").setHeader("firstModifies")
				.constant("apple").setHeader("threadName")
				.simple("${threadName}").to("mock:multiTofirst");

		from("direct:multiTosecond").onCompletion()
				.onWhen(header("timedOut").isNull())
				.log("operation rolling back").end()
				.setHeader("secondModifies").constant("banana")
				.setHeader("threadName").simple("${threadName}").delay(5000)
				.to("mock:multiTosecond")
				.filter(property("CamelMulticastComplete").isEqualTo(false))
				.setHeader("timedOut", constant("false"));
	}
}
