package co.nz.camel.tutorial.error.retry;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.error.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class RetrySlowTest {
	@Produce
	private ProducerTemplate template;

	@EndpointInject(uri = "mock:retryResult")
	private MockEndpoint mockResult;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RetrySlowTest.class);

	@Test
	public void testRetry() throws Exception {
		mockResult.expectedMessageCount(1);
		mockResult.allMessages().header(Exchange.REDELIVERED).isEqualTo(true);
		mockResult.allMessages().header(Exchange.REDELIVERY_COUNTER)
				.isEqualTo(1);
		mockResult.allMessages().header(Exchange.REDELIVERY_MAX_COUNTER)
				.isEqualTo(2);
		mockResult.allMessages().header(Exchange.REDELIVERY_DELAY).isNull();

		template.sendBody("direct:retryStart", "Foo");

		mockResult.assertIsSatisfied();
	}

	@Test
	public void testRetryRouteSpecific() throws Exception {
		mockResult.expectedMessageCount(1);
		mockResult.allMessages().header(Exchange.REDELIVERED).isEqualTo(true);
		mockResult.allMessages().header(Exchange.REDELIVERY_COUNTER)
				.isEqualTo(1);
		mockResult.allMessages().header(Exchange.REDELIVERY_MAX_COUNTER)
				.isEqualTo(2);
		mockResult.allMessages().header(Exchange.REDELIVERY_DELAY).isNull();

		template.sendBody("direct:retryRouteSpecific", "Foo");

		mockResult.assertIsSatisfied();
	}

	@Test
	public void testRetryRouteSpecificDelay() throws Exception {
		mockResult.expectedMessageCount(1);
		mockResult.allMessages().header(Exchange.REDELIVERED).isEqualTo(true);
		mockResult.allMessages().header(Exchange.REDELIVERY_COUNTER)
				.isEqualTo(1);
		mockResult.allMessages().header(Exchange.REDELIVERY_MAX_COUNTER)
				.isEqualTo(2);
		mockResult.allMessages().property("SporadicDelay")
				.isGreaterThanOrEqualTo(500);

		template.sendBody("direct:retryRouteSpecificDelay", "Foo");

		LOGGER.info("delay = {}", mockResult.getReceivedExchanges().get(0)
				.getProperty("SporadicDelay", long.class));

		mockResult.assertIsSatisfied();
	}

}
