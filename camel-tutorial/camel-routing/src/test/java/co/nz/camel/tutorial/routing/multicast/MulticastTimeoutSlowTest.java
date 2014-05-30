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
public class MulticastTimeoutSlowTest {

	public static final String MESSAGE_BODY = "Message to be multicast";

	@Produce(uri = "direct:multiTostart")
	protected ProducerTemplate template;

	@EndpointInject(uri = "mock:multiTofirst")
	private MockEndpoint mockFirst;

	@EndpointInject(uri = "mock:multiTosecond")
	private MockEndpoint mockSecond;

	@EndpointInject(uri = "mock:multiToafterMulticast")
	private MockEndpoint afterMulticast;

	@Test
	public void testTimedOutMessageNotDeliveredToEndpoints()
			throws InterruptedException {
		afterMulticast.setExpectedMessageCount(1);
		mockFirst.setExpectedMessageCount(1);
		mockSecond.setExpectedMessageCount(0);

		template.sendBody(MESSAGE_BODY);

		afterMulticast.assertIsSatisfied();
		mockFirst.assertIsSatisfied();
		mockSecond.assertIsSatisfied();
	}

}
