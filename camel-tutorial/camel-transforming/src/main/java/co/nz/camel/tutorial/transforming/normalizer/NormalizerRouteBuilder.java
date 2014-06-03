package co.nz.camel.tutorial.transforming.normalizer;

import java.util.Arrays;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.dataformat.xmljson.XmlJsonDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.stereotype.Component;

@Component
public class NormalizerRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		final DataFormat bindy = new BindyCsvDataFormat(
				"co.nz.camel.tutorial.transforming.csv.model");
		final DataFormat jaxb = new JaxbDataFormat(
				"co.nz.camel.tutorial.transforming.myschema");

		final XmlJsonDataFormat xmlJsonFormat = new XmlJsonDataFormat();
		xmlJsonFormat.setRootName("bookstore");
		xmlJsonFormat.setElementName("book");
		xmlJsonFormat
				.setExpandableProperties(Arrays.asList("author", "author"));

		from("direct:normalizerStart").choice()
				.when(header(Exchange.FILE_NAME).endsWith(".csv"))
				.unmarshal(bindy).bean(MyNormalizer.class, "bookModelToJaxb")
				.to("mock:normalizerCsv")
				.when(header(Exchange.FILE_NAME).endsWith(".json"))
				.unmarshal(xmlJsonFormat).to("mock:normalizerJson")
				.when(header(Exchange.FILE_NAME).endsWith(".xml"))
				.unmarshal(jaxb).to("mock:normalizerXml").otherwise()
				.to("mock:normalizerUnknown").stop().end()
				.to("mock:normalizerNormalized");
	}
}