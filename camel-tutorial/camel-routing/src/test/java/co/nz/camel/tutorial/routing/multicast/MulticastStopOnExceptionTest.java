package co.nz.camel.tutorial.routing.multicast;

import static org.junit.Assert.assertEquals;

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
public class MulticastStopOnExceptionTest {

	public static final String MESSAGE_BODY = "Message to be multicast";

	@Produce(uri = "direct:multiSestart")
	protected ProducerTemplate template;

	@EndpointInject(uri = "mock:multiSefirst")
	private MockEndpoint mockFirst;

	@EndpointInject(uri = "mock:multiSesecond")
	private MockEndpoint mockSecond;

	@EndpointInject(uri = "mock:multiSeafterMulticast")
	private MockEndpoint afterMulticast;

	@EndpointInject(uri = "mock:multiSeexceptionHandler")
	private MockEndpoint exceptionHandler;

	@Test
	public void testMessageRoutedToMulticastEndpoints()
			throws InterruptedException {
		mockFirst.setExpectedMessageCount(1);
		mockFirst.message(0).body().isEqualTo(MESSAGE_BODY);

		mockSecond.setExpectedMessageCount(0);

		afterMulticast.setExpectedMessageCount(0);
		exceptionHandler.setExpectedMessageCount(1);

		String response = (String) template.requestBody(MESSAGE_BODY);
		assertEquals("Oops", response);

		mockFirst.assertIsSatisfied();
		mockSecond.assertIsSatisfied();
		afterMulticast.assertIsSatisfied();
		exceptionHandler.assertIsSatisfied();
	}
}