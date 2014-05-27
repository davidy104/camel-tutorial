package co.nz.camel.tutorial.routing.route.multicast;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

/**
 * Example shows multicast stopping on exception.
 */
public class MulticastStopOnExceptionRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:multiSestart").multicast().stopOnException()
				.to("direct:multiSefirst").to("direct:multiSesecond").end()
				// this will never be called
				.log("continuing with ${body}")
				.to("mock:multiSeafterMulticast")
				// copy the In message to the Out message; this will become the
				// route response
				.transform(body());

		from("direct:multiSefirst").onException(Exception.class).handled(true)
				.log("Caught exception").to("mock:multiSeexceptionHandler")
				.transform(constant("Oops")).end().to("mock:multiSefirst")
				.process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						throw new IllegalStateException(
								"something went horribly wrong");
					}
				});

		from("direct:multiSesecond").to("mock:multiSesecond").transform(
				constant("All OK here"));
	}
}
