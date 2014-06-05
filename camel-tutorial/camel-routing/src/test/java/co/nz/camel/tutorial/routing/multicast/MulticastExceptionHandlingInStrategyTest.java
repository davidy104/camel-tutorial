package co.nz.camel.tutorial.routing.multicast;

import static org.junit.Assert.assertEquals;

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
public class MulticastExceptionHandlingInStrategyTest {

	public static final String MESSAGE_BODY = "Message to be multicast";

	@Produce(uri = "direct:multiEhstart")
	protected ProducerTemplate template;

	@EndpointInject(uri = "mock:multiEhfirst")
	private MockEndpoint mockFirst;

	@EndpointInject(uri = "mock:multiEhsecond")
	private MockEndpoint mockSecond;

	@EndpointInject(uri = "mock:multiEhafterMulticast")
	private MockEndpoint afterMulticast;

	@EndpointInject(uri = "mock:multiEhexceptionHandler")
	private MockEndpoint exceptionHandler;

	@Test
	public void testMessageRoutedToMulticastEndpoints()
			throws InterruptedException {
		mockFirst.setExpectedMessageCount(1);
		mockFirst.message(0).body().isEqualTo(MESSAGE_BODY);

		mockSecond.setExpectedMessageCount(1);

		afterMulticast.setExpectedMessageCount(1);
		afterMulticast.message(0).predicate()
				.simple("${header.multicast_exception} != null");

		exceptionHandler.setExpectedMessageCount(1);

		String response = (String) template.requestBody(MESSAGE_BODY);
		assertEquals("Oops,All OK here", response);

		mockFirst.assertIsSatisfied();
		mockSecond.assertIsSatisfied();
		afterMulticast.assertIsSatisfied();
		exceptionHandler.assertIsSatisfied();
	}

}
