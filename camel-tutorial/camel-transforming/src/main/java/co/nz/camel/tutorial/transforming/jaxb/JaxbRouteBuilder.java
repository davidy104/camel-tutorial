package co.nz.camel.tutorial.transforming.jaxb;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.spi.DataFormat;

public class JaxbRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		DataFormat myJaxb = new JaxbDataFormat(
				"co.nz.camel.tutorial.transforming.myschema");

		from("direct:jaxbMarshal").marshal(myJaxb).to("mock:jaxbMarshalResult");

		from("direct:jaxbUnmarshal").unmarshal(myJaxb).to(
				"mock:jaxbUnmarshalResult");
	}
}