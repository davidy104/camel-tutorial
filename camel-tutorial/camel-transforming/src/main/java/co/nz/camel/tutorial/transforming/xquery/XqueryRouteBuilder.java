package co.nz.camel.tutorial.transforming.xquery;

import org.apache.camel.builder.RouteBuilder;
import static org.apache.camel.component.xquery.XQueryBuilder.xquery;


public class XqueryRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:xqStart")
				.transform(
						xquery("<books>{for $x in /bookstore/book where $x/price>30 order by $x/title return $x/title}</books>"));
	}
}