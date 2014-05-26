package co.nz.camel.tutorial.routing.test;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.routing.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
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
