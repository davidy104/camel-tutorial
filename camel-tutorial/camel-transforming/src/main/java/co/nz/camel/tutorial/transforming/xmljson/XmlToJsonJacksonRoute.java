package co.nz.camel.tutorial.transforming.xmljson;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;

public class XmlToJsonJacksonRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		JacksonDataFormat dataFormat = new JacksonDataFormat();

		from("direct:jacksonUnmarshal").unmarshal(dataFormat).to(
				"mock:jacksonUnmarshalResult");
	}

}
