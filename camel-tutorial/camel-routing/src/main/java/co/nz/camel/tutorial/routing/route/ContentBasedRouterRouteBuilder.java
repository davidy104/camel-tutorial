package co.nz.camel.tutorial.routing.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ContentBasedRouterRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:cbrStart")
				.choice()
				.when()
				.simple("${body} contains 'Camel'")
				.to("mock:cbrCamel")
				.log("Camel ${body}")
				.otherwise()
				.to("mock:cbrOther")
				.log("Other ${body}")
				.end()
				.log("Message ${body}")
				.to("controlbus:route?routeId=mainRoute&action=stop&async=true")
				.log("Signalled to stop route");
	}
}
