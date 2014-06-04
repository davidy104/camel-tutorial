package co.nz.camel.tutorial.error.dlc;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import co.nz.camel.tutorial.error.shared.FlakyProcessor;

@Component
public class DlcRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		errorHandler(deadLetterChannel("seda:dlcError"));

		from("direct:dlcStart").routeId("myDlcRoute").setHeader("myHeader")
				.constant("changed").bean(FlakyProcessor.class)
				.to("mock:dlcResult");

		from("direct:dlcMultiroute").routeId("myDlcMultistepRoute")
				.setHeader("myHeader").constant("multistep")
				.inOut("seda:dlcFlakyroute").setHeader("myHeader")
				.constant("changed").to("mock:dlcResult");

		from("seda:dlcFlakyroute").routeId("myFlakyRoute")
				.setHeader("myHeader").constant("flaky")
				.bean(FlakyProcessor.class);

		from("direct:dlcMultirouteOriginal")
				.routeId("myDlcMultistepOriginalRoute").setHeader("myHeader")
				.constant("multistep").inOut("seda:dlcFlakyrouteOriginal")
				.setHeader("myHeader").constant("changed").to("mock:dlcResult");

		from("seda:dlcFlakyrouteOriginal")
				.routeId("myFlakyRouteOriginal")
				.errorHandler(
						deadLetterChannel("seda:dlcError").useOriginalMessage())
				.setHeader("myHeader").constant("flaky")
				.bean(FlakyProcessor.class);

		from("direct:dlcRouteSpecific").routeId("myDlcSpecificRoute")
				.errorHandler(deadLetterChannel("seda:dlcRrror"))
				.bean(FlakyProcessor.class).to("mock:dlcResult");

		from("direct:dlcUseOriginal")
				.routeId("myDlcOriginalRoute")
				.errorHandler(
						deadLetterChannel("seda:dlcError").useOriginalMessage())
				.setHeader("myHeader").constant("changed")
				.bean(FlakyProcessor.class).to("mock:dlcResult");

		from("seda:dlcError").routeId("myDlcChannelRoute")
				.to("log:dlc?showAll=true&multiline=true").to("mock:dlcError");
	}
}