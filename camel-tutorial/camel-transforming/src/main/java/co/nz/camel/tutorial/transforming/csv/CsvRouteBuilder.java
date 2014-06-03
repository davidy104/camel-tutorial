package co.nz.camel.tutorial.transforming.csv;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;

public class CsvRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		final DataFormat bindy = new BindyCsvDataFormat(
				"co.nz.camel.tutorial.transforming.csv.model");

		from("direct:csvUnmarshal").unmarshal(bindy);
		from("direct:csvMarshal").marshal(bindy);
	}
}
