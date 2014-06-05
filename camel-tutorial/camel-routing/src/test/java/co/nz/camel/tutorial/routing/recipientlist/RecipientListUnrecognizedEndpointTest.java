package co.nz.camel.tutorial.routing.recipientlist;

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
public class RecipientListUnrecognizedEndpointTest {

	@Produce(uri = "direct:rclustart")
	protected ProducerTemplate template;

	@EndpointInject(uri = "mock:rclufirst")
	private MockEndpoint first;

	@EndpointInject(uri = "mock:rclusecond")
	private MockEndpoint second;

	@Test
	public void testMessageRoutedToMulticastEndpoints()
			throws InterruptedException {
		final String messageBody = "Message to be distributed via recipientList";

		first.setExpectedMessageCount(1);
		first.message(0).body().isEqualTo(messageBody);
		second.setExpectedMessageCount(1);
		second.message(0).body().isEqualTo(messageBody);

		template.sendBody(messageBody);

		first.assertIsSatisfied();
		second.assertIsSatisfied();

	}

}
