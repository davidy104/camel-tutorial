package co.nz.camel.tutorial.transforming.enrich;

import javax.annotation.Resource;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class EnrichWithAggregatorRouteBuilder extends RouteBuilder {

	@Resource
	private MergeInReplacementText myMerger;

	@Override
	public void configure() throws Exception {
		from("direct:enrichWithAggregatorStart").bean(myMerger, "setup")
				.enrich("direct:enrichWithAggregatorExpander", myMerger);

		from("direct:enrichWithAggregatorExpander").bean(
				AbbreviationExpander.class, "expand");
	}

}