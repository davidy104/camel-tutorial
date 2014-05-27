package co.nz.camel.tutorial.routing.route.multicast;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aggregation strategy that concatenates String mockreply.
 */
@Component
public class ConcatenatingAggregationStrategy implements AggregationStrategy {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ConcatenatingAggregationStrategy.class);

	@Override
	public Exchange aggregate(Exchange exchange1, Exchange exchange2) {
		LOGGER.info("ConcatenatingAggregationStrategy aggregate start:{}");
		if (exchange1 == null) {
			return exchange2;
		} else {
			String body1 = exchange1.getIn().getBody(String.class);
			String body2 = exchange2.getIn().getBody(String.class);
			String merged = (body1 == null) ? body2 : body1 + "," + body2;
			exchange1.getIn().setBody(merged);
			LOGGER.info("ConcatenatingAggregationStrategy aggregate end:{}");
			return exchange1;
		}

	}
}