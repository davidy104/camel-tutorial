package co.nz.camel.tutorial.routing.wtr;

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
public class WireTapOnPrepareTest {
	@Produce(uri = "direct:wtrpstart")
	protected ProducerTemplate template;

	@EndpointInject(uri = "mock:wtrptapped")
	private MockEndpoint tapped;

	@EndpointInject(uri = "mock:wtrpout")
	private MockEndpoint out;

	@Test
	public void testMessageRoutedToWireTapMarked() throws InterruptedException {
		final String messageBody = "Message to be tapped";

		tapped.expectedBodiesReceived(messageBody);
		tapped.message(0).header("processorAction").isNotNull();

		out.expectedBodiesReceived(messageBody);
		out.message(0).header("processorAction").isNull();

		template.sendBody(messageBody);

		tapped.assertIsSatisfied();
		out.assertIsSatisfied();
	}

}
