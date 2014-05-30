package co.nz.camel.tutorial.extend.predicate;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MyPredicateInlineRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:mpirStart").filter(new Predicate() {
			@Override
			public boolean matches(Exchange exchange) {
				final String body = exchange.getIn().getBody(String.class);
				return ((body != null) && body.contains("Boston"));
			}
		}).to("mock:mpirBoston");
	}
}