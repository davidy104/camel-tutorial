package co.nz.camel.tutorial.routing.multicast;

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

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class MulticastShallowCopyTest {
	public static final String MESSAGE_BODY = "Message to be multicast";

	@Produce(uri = "direct:multiScstart")
	protected ProducerTemplate template;

	@EndpointInject(uri = "mock:multiScfirst")
	private MockEndpoint mockFirst;

	@EndpointInject(uri = "mock:multiScsecond")
	private MockEndpoint mockSecond;

	@EndpointInject(uri = "mock:multiScafterMulticast")
	private MockEndpoint afterMulticast;

	@Test
	public void testMessageRoutedToMulticastEndpoints()
			throws InterruptedException {
		mockFirst.setExpectedMessageCount(1);
		mockFirst.message(0).body().isEqualTo(MESSAGE_BODY);
		mockFirst.message(0).header("firstModifies").isEqualTo("apple");

		mockSecond.setExpectedMessageCount(1);
		mockSecond.message(0).body().isEqualTo(MESSAGE_BODY);
		mockSecond.message(0).header("secondModifies").isEqualTo("banana");
		mockSecond.message(0).header("firstModifies").isNull();

		afterMulticast.setExpectedMessageCount(1);
		afterMulticast.message(0).body().isEqualTo(MESSAGE_BODY);
		afterMulticast.message(0).header("modifiedBy").isNull();

		template.sendBody(MESSAGE_BODY);

		afterMulticast.assertIsSatisfied();
		mockSecond.assertIsSatisfied();
		mockFirst.assertIsSatisfied();
	}

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
	public void testAllEndpointsReachedBySameThread()
			throws InterruptedException {
		afterMulticast.setExpectedMessageCount(1);
		mockFirst.setExpectedMessageCount(1);
		mockSecond.setExpectedMessageCount(1);

		template.sendBody(MESSAGE_BODY);

		afterMulticast.assertIsSatisfied();
		mockSecond.assertIsSatisfied();
		mockFirst.assertIsSatisfied();

		// check that all of the mock endpoints were reached by the same thread
		String firstThreadName = getExchange(mockFirst).getIn().getHeader(
				"threadName", String.class);
		String secondThreadName = getExchange(mockSecond).getIn().getHeader(
				"threadName", String.class);
		assertEquals(firstThreadName, secondThreadName);
	}

	private Exchange getExchange(MockEndpoint mock) {
		return mock.getExchanges().get(0);
	}

}
