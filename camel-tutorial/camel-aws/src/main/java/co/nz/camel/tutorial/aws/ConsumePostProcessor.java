package co.nz.camel.tutorial.aws;

import org.apache.camel.Exchange;

public interface ConsumePostProcessor {

	void process(Exchange exchange) throws Exception;
}
