package co.nz.camel.tutorial.routing.route.wtr;

import org.apache.camel.builder.RouteBuilder;

import co.nz.camel.tutorial.routing.model.CheeseRipener;
import co.nz.camel.tutorial.routing.processor.CheeseCloningProcessor;

/**
 * Route showing wiretap without state leakage.
 */
public class WireTapStateNoLeaksRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:wtsnlstart")
				// .autoStartup(false)
				.log("Cheese is ${body.age} months old")
				.wireTap("direct:wtsnlprocessInBackground")
				.onPrepare(new CheeseCloningProcessor()).delay(constant(1000))
				.to("mock:wtsnlout");

		from("direct:wtsnlprocessInBackground").bean(CheeseRipener.class,
				"ripen").to("mock:wtsnltapped");
	}
}