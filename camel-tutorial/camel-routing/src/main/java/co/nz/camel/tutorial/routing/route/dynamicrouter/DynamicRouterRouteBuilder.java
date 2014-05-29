package co.nz.camel.tutorial.routing.route.dynamicrouter;

import org.apache.camel.builder.RouteBuilder;

public class DynamicRouterRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:dymStart").dynamicRouter(
				method(MyDynamicRouter.class, "routeMe"));

		from("direct:dymOther").to("mock:dymOther");
	}
}
