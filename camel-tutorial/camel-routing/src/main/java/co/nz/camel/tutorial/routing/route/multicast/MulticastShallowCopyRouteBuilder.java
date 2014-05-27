package co.nz.camel.tutorial.routing.route.multicast;

import org.apache.camel.builder.RouteBuilder;

/**
 * Example shows shallow copying of model in multicast.
 */
public class MulticastShallowCopyRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:multiScstart").multicast().to("direct:multiScfirst")
				.to("direct:multiScsecond").end()
				.to("mock:multiScafterMulticast");

		from("direct:multiScfirst").setHeader("firstModifies")
				.constant("apple").setHeader("threadName")
				.simple("${threadName}").to("mock:multiScfirst");

		from("direct:multiScsecond").setHeader("secondModifies")
				.constant("banana").setHeader("threadName")
				.simple("${threadName}").to("mock:multiScsecond");
	}
}
