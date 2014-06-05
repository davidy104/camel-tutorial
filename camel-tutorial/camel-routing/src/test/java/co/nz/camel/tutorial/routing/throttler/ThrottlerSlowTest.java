package co.nz.camel.tutorial.routing.throttler;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.routing.config.ApplicationContextConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationContextConfiguration.class)
public class ThrottlerSlowTest {

	@Produce
	protected ProducerTemplate template;

	@EndpointInject(uri = "mock:trtUnthrottled")
	private MockEndpoint unthrottled;

	@EndpointInject(uri = "mock:trtThrottled")
	private MockEndpoint throttled;

	@EndpointInject(uri = "mock:trtAfter")
	private MockEndpoint after;

	@Test
	public void testThrottle() throws Exception {
		final int throttleRate = 5;
		final int messageCount = throttleRate + 2;

		unthrottled.expectedMessageCount(messageCount);
		throttled.expectedMessageCount(throttleRate);
		after.expectedMessageCount(throttleRate);

		for (int i = 0; i < messageCount; i++) {
			template.asyncSendBody("direct:trtstart", "Camel Rocks");
		}

		// the test will stop once all of the conditions have been met
		// the only way this set of conditions can happen is if 2
		// messages are currently suspended for throttling
		unthrottled.assertIsSatisfied();
		throttled.assertIsSatisfied();
		after.assertIsSatisfied();
	}

}
