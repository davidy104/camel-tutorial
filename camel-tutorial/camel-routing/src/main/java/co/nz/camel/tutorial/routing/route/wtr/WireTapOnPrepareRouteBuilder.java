package co.nz.camel.tutorial.routing.route.wtr;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

/**
 * Using the <code>onPrepare</code> statement to modify the tapped message
 * during the send.
 */
public class WireTapOnPrepareRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		Processor markProcessed = new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				exchange.getIn().setHeader("processorAction", "triggered");
			}
		};

		from("direct:wtrpstart")
				// .autoStartup(false)
				.wireTap("mock:wtrptapped").onPrepare(markProcessed)
				.to("mock:wtrpout");
	}
}