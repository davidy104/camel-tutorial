package co.nz.camel.tutorial.transforming.json;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

public class JsonJacksonRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:jsonJacksonMarshal").marshal().json(JsonLibrary.Jackson)
				.to("mock:jsonJacksonMarshalResult");

		from("direct:jsonJacksonUnmarshal").unmarshal()
				.json(JsonLibrary.Jackson, View.class)
				.to("mock:jsonJacksonUnmarshalResult");
	}
}
