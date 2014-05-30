package co.nz.camel.tutorial.extend.config;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;

public class CamelRouteBuildersInitializer {

	private CamelContext camelContext;

	private RouteBuilder[] routeBuilders;

	public CamelRouteBuildersInitializer(CamelContext camelContext,
			RouteBuilder... routeBuilders) {
		this.camelContext = camelContext;
		this.routeBuilders = routeBuilders;
	}

	public void initialize() throws Exception {
		for (RouteBuilder routeBuilder : routeBuilders) {
			camelContext.addRoutes(routeBuilder);
		}
	}
}
