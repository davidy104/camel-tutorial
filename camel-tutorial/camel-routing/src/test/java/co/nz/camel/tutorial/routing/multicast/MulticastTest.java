package co.nz.camel.tutorial.routing.multicast;

import org.apache.camel.EndpointInject;
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
public class MulticastTest {
	@Produce(uri = "direct:multistart")
	protected ProducerTemplate template;

	@EndpointInject(uri = "mock:multifirst")
	private MockEndpoint first;

	@EndpointInject(uri = "mock:multisecond")
	private MockEndpoint second;

	@EndpointInject(uri = "mock:multithird")
	private MockEndpoint third;

	@Test
	public void testMessageRoutedToMulticastEndpoints()
			throws InterruptedException {
		final String messageBody = "Message to be multicast";

		first.setExpectedMessageCount(1);
		first.message(0).body().isEqualTo(messageBody);
		second.setExpectedMessageCount(1);
		second.message(0).body().isEqualTo(messageBody);
		third.setExpectedMessageCount(1);
		third.message(0).body().isEqualTo(messageBody);

		template.sendBody(messageBody);

		first.assertIsSatisfied();
		second.assertIsSatisfied();
		third.assertIsSatisfied();
	}

}
