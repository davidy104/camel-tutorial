package co.nz.camel.tutorial.transforming.enrich;

import org.apache.camel.builder.RouteBuilder;

public class EnrichWithAggregatorRouteBuilder extends RouteBuilder {
	private MergeInReplacementText myMerger;

	@Override
	public void configure() throws Exception {
		from("direct:enrichWithAggregatorStart").bean(myMerger, "setup")
				.enrich("direct:enrichWithAggregatorExpander", myMerger);
	}

	public MergeInReplacementText getMyMerger() {
		return myMerger;
	}

	public void setMyMerger(MergeInReplacementText myMerger) {
		this.myMerger = myMerger;
	}
}