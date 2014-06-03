package co.nz.camel.tutorial.transforming.enrich;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class EnrichRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:enrichStart").enrich("bean:myExpander?method=expand");
	}
}
