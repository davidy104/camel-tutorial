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
public class ExceptionHandlingAggregationStrategy implements
		AggregationStrategy {
	public static final String MULTICAST_EXCEPTION = "multicast_exception";

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ExceptionHandlingAggregationStrategy.class);

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		LOGGER.info("ExceptionHandlingAggregationStrategy aggregate start:{}");
		if (oldExchange == null) {
			if (newExchange.isFailed()) {
				// this block only gets called if stopOnException() is not
				// defined on the multicast
				Exception ex = newExchange.getException();
				newExchange.setException(null);
				newExchange.setProperty(MULTICAST_EXCEPTION, ex);
			}
			return newExchange;
		} else {
			LOGGER.info("oldExchange is not null:{}");
			if (newExchange.isFailed()) {
				// this block only gets called if stopOnException() is not
				// defined on the multicast
				LOGGER.info("exception caught:{}");
				Exception ex = newExchange.getException();
				oldExchange.setProperty(MULTICAST_EXCEPTION, ex);
			}
			// merge the Strings
			String body1 = oldExchange.getIn().getBody(String.class);
			String body2 = newExchange.getIn().getBody(String.class);
			String merged = (body1 == null) ? body2 : body1 + "," + body2;
			oldExchange.getIn().setBody(merged);
			LOGGER.info("ExceptionHandlingAggregationStrategy aggregate end:{}");
			return oldExchange;
		}

	}

}