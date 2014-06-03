package co.nz.camel.tutorial.transforming.xmljson;

import java.util.Arrays;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.xmljson.XmlJsonDataFormat;

public class XmlJsonRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("direct:XmlJsonMarshal").marshal().xmljson()
				.to("mock:XmlJsonMarshalResult");

		from("direct:XmlJsonUnmarshal").unmarshal().xmljson()
				.to("mock:XmlJsonUnmarshalResult");

		XmlJsonDataFormat xmlJsonFormat = new XmlJsonDataFormat();
		xmlJsonFormat.setRootName("bookstore");
		xmlJsonFormat.setElementName("book");
		xmlJsonFormat
				.setExpandableProperties(Arrays.asList("author", "author"));

		from("direct:XmlJsonUnmarshalBookstore").unmarshal(xmlJsonFormat).to(
				"mock:XmlJsonUnmarshalBookstoreResult");
	}
}
