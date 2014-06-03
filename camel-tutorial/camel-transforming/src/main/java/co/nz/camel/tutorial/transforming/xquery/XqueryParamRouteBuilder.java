package co.nz.camel.tutorial.transforming.xquery;

import org.apache.camel.builder.RouteBuilder;
import static org.apache.camel.component.xquery.XQueryBuilder.xquery;

public class XqueryParamRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:xqpStart")
				.transform(
						xquery("declare variable $in.headers.myParamValue as xs:integer external; <books value='{$in.headers.myParamValue}'>{for $x in /bookstore/book where $x/price>$in.headers.myParamValue order by $x/title return $x/title}</books>"));
	}
}
