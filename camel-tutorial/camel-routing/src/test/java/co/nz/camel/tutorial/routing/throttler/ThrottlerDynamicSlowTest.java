package co.nz.camel.tutorial.routing.throttler;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.routing.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class ThrottlerDynamicTest {

	@Produce
	protected ProducerTemplate template;

	@EndpointInject(uri = "mock:trtdUnthrottled")
	private MockEndpoint unthrottled;

	@EndpointInject(uri = "mock:trtdThrottled")
	private MockEndpoint throttled;

	@EndpointInject(uri = "mock:trtdAfter")
	private MockEndpoint after;

	@EndpointInject(uri = "direct:trtdStart")
	private Endpoint start;

	@Test
	public void testThrottleDynamic() throws Exception {
		final int throttleRate = 3;
		final int messageCount = throttleRate + 2;

		unthrottled.expectedMessageCount(messageCount);
		throttled.expectedMessageCount(throttleRate);
		after.expectedMessageCount(throttleRate);

		for (int i = 0; i < messageCount; i++) {
			Exchange exchange = start.createExchange();
			{
				Message in = exchange.getIn();
				in.setHeader("throttleRate", throttleRate);
				in.setBody("Camel Rocks");
			}
			template.asyncSend("direct:trtdStart", exchange);
		}

		// the test will stop once all of the conditions have been met
		// the only way this set of conditions can happen is if 2
		// messages are currently suspended for throttling
		unthrottled.assertIsSatisfied();
		throttled.assertIsSatisfied();
		after.assertIsSatisfied();
	}
}
