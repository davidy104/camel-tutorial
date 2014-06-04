package co.nz.camel.tutorial.error.shared;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SporadicProcessor {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(SporadicProcessor.class);

	private long lastCall;

	public void doSomething(Exchange exchange) throws SporadicException {
		if (exchange.getIn().getHeader(Exchange.REDELIVERED, false,
				boolean.class)) {
			exchange.setProperty("SporadicDelay",
					(System.currentTimeMillis() - lastCall));
			LOGGER.info("SporadicProcessor works on retry");
			return;
		}
		lastCall = System.currentTimeMillis();
		LOGGER.info("SporadicProcessor fails");
		throw new SporadicException("SporadicProcessor is unavailable");
	}
}
