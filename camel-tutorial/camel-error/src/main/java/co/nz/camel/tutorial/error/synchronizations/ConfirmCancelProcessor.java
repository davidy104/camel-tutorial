package co.nz.camel.tutorial.error.synchronizations;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.spi.Synchronization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Processor that starts a mock remote operation, then commits or cancels it
 * depending on whether the Exchange was successfully processed through the rest
 * of the route.
 */
public class ConfirmCancelProcessor implements Processor {
	private final Logger LOGGER = LoggerFactory
			.getLogger(ConfirmCancelProcessor.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		LOGGER.info("Starting two-phase operation");

		final ProducerTemplate producerTemplate = exchange.getContext()
				.createProducerTemplate();
		producerTemplate.send("mock:synchronizationsStart", exchange);

		exchange.addOnCompletion(new Synchronization() {
			@Override
			public void onComplete(Exchange exchange) {
				LOGGER.info("Completed - confirming");
				producerTemplate.send("mock:synchronizationsConfirm", exchange);
			}

			@Override
			public void onFailure(Exchange exchange) {
				LOGGER.info("Failed - cancelling");
				producerTemplate.send("mock:synchronizationsCancel", exchange);
			}
		});
	}
}