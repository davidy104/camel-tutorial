package co.nz.camel.tutorial.routing.multicast;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
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
public class MulticastParallelProcessingTest {
	public static final String MESSAGE_BODY = "Message to be multicast";

	@Produce(uri = "direct:multiPstart")
	protected ProducerTemplate template;

	@EndpointInject(uri = "mock:multiPfirst")
	private MockEndpoint mockFirst;

	@EndpointInject(uri = "mock:multiPsecond")
	private MockEndpoint mockSecond;

	@EndpointInject(uri = "mock:multiPafterMulticast")
	private MockEndpoint afterMulticast;

	@Test
	public void testAllMessagesParticipateInDifferentTransactions()
			throws InterruptedException {
		afterMulticast.setExpectedMessageCount(1);
		mockFirst.setExpectedMessageCount(1);
		mockSecond.setExpectedMessageCount(1);

		template.sendBody(MESSAGE_BODY);

		afterMulticast.assertIsSatisfied();
		mockSecond.assertIsSatisfied();
		mockFirst.assertIsSatisfied();

		// check that all of the messages participated in different transactions
		assertNotEquals(getExchange(afterMulticast).getUnitOfWork(),
				getExchange(mockFirst).getUnitOfWork());
		assertNotEquals(getExchange(afterMulticast).getUnitOfWork(),
				getExchange(mockSecond).getUnitOfWork());
	}

	@Test
	public void testAllEndpointsReachedByDifferentThreads()
			throws InterruptedException {
		afterMulticast.setExpectedMessageCount(1);
		mockFirst.setExpectedMessageCount(1);
		mockSecond.setExpectedMessageCount(1);

		final String response = (String) template.requestBody(MESSAGE_BODY);
		assertEquals("response", response);

		afterMulticast.assertIsSatisfied();
		mockSecond.assertIsSatisfied();
		mockFirst.assertIsSatisfied();

		// check that all of the mock endpoints were reached by the different
		// threads
		final String mainThreadName = getExchange(afterMulticast).getIn()
				.getHeader("threadName", String.class);
		final String firstThreadName = getExchange(mockFirst).getIn()
				.getHeader("threadName", String.class);
		final String secondThreadName = getExchange(mockSecond).getIn()
				.getHeader("threadName", String.class);

		assertNotEquals(firstThreadName, mainThreadName);
		assertNotEquals(firstThreadName, secondThreadName);
		assertNotEquals(mainThreadName, secondThreadName);
	}

	private Exchange getExchange(MockEndpoint mock) {
		return mock.getExchanges().get(0);
	}
}
