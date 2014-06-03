package co.nz.camel.tutorial.transforming.json;

import org.apache.camel.builder.RouteBuilder;

public class JsonRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:jsonMarshal").marshal().json()
				.to("mock:jsonMarshalResult");

		from("direct:jsonUnmarshal").unmarshal().json()
				.to("mock:jsonUnmarshalResult");
	}
}