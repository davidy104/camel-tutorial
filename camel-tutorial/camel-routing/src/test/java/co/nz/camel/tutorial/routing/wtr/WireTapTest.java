package co.nz.camel.tutorial.routing.wtr;

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
public class WireTapTest {

	@Produce(uri = "direct:wtStart")
	protected ProducerTemplate template;

	@EndpointInject(uri = "mock:wttapped")
	private MockEndpoint tapped;

	@EndpointInject(uri = "mock:wtout")
	private MockEndpoint out;

	@Test
	public void testMessageRoutedToWireTapEndpoint()
			throws InterruptedException {
		final String messageBody = "Message to be tapped";

		tapped.expectedBodiesReceived(messageBody);
		out.expectedBodiesReceived(messageBody);

		template.sendBody(messageBody);

		tapped.assertIsSatisfied();
		out.assertIsSatisfied();
	}

}
