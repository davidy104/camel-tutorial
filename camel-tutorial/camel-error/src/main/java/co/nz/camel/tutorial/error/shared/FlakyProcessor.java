package co.nz.camel.tutorial.error.shared;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlakyProcessor {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(FlakyProcessor.class);

	public void doSomething(Exchange exchange) throws FlakyException {
		if (exchange.getProperty("optimizeBit", false, boolean.class)) {
			LOGGER.info("FlakyProcessor works with optimizationBit set");
			return;
		}

		if ("KaBoom".equalsIgnoreCase(exchange.getIn().getBody(String.class))) {
			LOGGER.error("Throwing FlakyException");
			throw new FlakyException("FlakyProcessor has gone Flaky");
		}
	}
}
