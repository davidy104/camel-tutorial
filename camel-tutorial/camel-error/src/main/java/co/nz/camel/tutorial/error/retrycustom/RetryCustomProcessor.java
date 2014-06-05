package co.nz.camel.tutorial.error.retrycustom;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RetryCustomProcessor implements Processor {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RetryCustomProcessor.class);

	@Override
	public void process(Exchange exchange) {
		LOGGER.info("RetryCustomProcessor process:{}");
		exchange.setProperty("optimizeBit", true);
	}
}
