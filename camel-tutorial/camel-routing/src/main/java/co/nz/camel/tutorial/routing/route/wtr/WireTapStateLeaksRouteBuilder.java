package co.nz.camel.tutorial.routing.route.wtr;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import co.nz.camel.tutorial.routing.model.CheeseRipener;

@Component
public class WireTapStateLeaksRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:wtslStart")
				// .autoStartup(false)
				.log("Cheese is ${body.age} months old")
				.wireTap("direct:wtslProcessInBackground")
				.delay(constant(1000)).to("mock:wtslout");

		from("direct:wtslProcessInBackground").bean(CheeseRipener.class,
				"ripen").to("mock:wtsltapped");
	}

}
