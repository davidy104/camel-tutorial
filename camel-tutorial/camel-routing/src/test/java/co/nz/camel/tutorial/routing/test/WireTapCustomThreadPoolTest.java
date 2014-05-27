package co.nz.camel.tutorial.routing.test;

import java.util.List;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.routing.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class WireTapCustomThreadPoolTest {
	@Produce(uri = "direct:wtctstart")
	protected ProducerTemplate template;

	@EndpointInject(uri = "mock:wtcttapped")
	private MockEndpoint tapped;

	@EndpointInject(uri = "mock:wtctout")
	private MockEndpoint out;

	@Test
	public void testMessageWireTappedInOrderBySameThread()
			throws InterruptedException {
		final String messageBody = "Message to be tapped";
		final int messagesToSend = 3;

		tapped.setExpectedMessageCount(messagesToSend);
		tapped.expectsAscending().header("messageCount");

		out.setExpectedMessageCount(messagesToSend);
		out.expectsAscending().header("messageCount");

		for (int messageCount = 0; messageCount < messagesToSend; messageCount++) {
			template.sendBodyAndHeader(messageBody, "messageCount",
					messageCount);
		}

		tapped.assertIsSatisfied();
		out.assertIsSatisfied();

		List<Exchange> exchanges = tapped.getExchanges();
		String firstExchangeThreadName = null;
		for (Exchange exchange : exchanges) {
			Message in = exchange.getIn();
			if (firstExchangeThreadName == null) {
				firstExchangeThreadName = in.getHeader("threadName",
						String.class);
			}
			Assert.assertEquals(firstExchangeThreadName,
					in.getHeader("threadName", String.class));
		}
	}

}
