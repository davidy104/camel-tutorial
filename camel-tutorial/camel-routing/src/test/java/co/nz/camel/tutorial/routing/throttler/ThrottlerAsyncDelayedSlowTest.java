package co.nz.camel.tutorial.routing.throttler;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
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
public class ThrottlerAsyncDelayedSlowTest {

	@Produce
	protected ProducerTemplate template;

	@EndpointInject(uri = "mock:trtaUnthrottled")
	private MockEndpoint unthrottled;

	@EndpointInject(uri = "mock:trtaThrottled")
	private MockEndpoint throttled;

	@EndpointInject(uri = "mock:trtaAfter")
	private MockEndpoint after;

	@Test
	public void testAsyncDelayedThrottle() throws Exception {
		final int throttleRate = 5;
		final int messageCount = throttleRate + 2;

		// here we are going to test that of 10 messages sent, the last 5
		// will have been throttled and processed on a different thread

		// let's wait until all of the messages have been processed

		throttled.expectedMessageCount(messageCount);
		unthrottled.expectedMessageCount(messageCount);
		after.expectedMessageCount(messageCount);

		for (int i = 0; i < messageCount; i++) {
			template.asyncSendBody("direct:trtaStart", "Camel Rocks");
		}

		throttled.assertIsSatisfied();

		List<Exchange> exchanges = throttled.getExchanges();
		for (int i = 0; i < exchanges.size(); i++) {
			Exchange exchange = exchanges.get(i);
			String threadName = exchange.getIn().getHeader("threadName",
					String.class);
			if (i < throttleRate) {
				assertTrue(threadName.contains("ProducerTemplate"));
			} else {
				assertTrue(threadName.contains("Throttle"));
			}
		}

		// Thread.sleep(10000);
	}

}
