package co.nz.camel.tutorial.routing.route.filter;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FilteringRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:filterstart").filter().simple("${body} regex '^C.*'")
				.to("mock:C").end().to("mock:afterC").filter()
				.simple("${body} contains 'amel'").to("mock:amel").end()
				.to("mock:filterOther");
	}
}