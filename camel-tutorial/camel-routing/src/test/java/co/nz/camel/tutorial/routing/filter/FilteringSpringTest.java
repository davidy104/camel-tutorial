package co.nz.camel.tutorial.routing.filter;

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

import co.nz.camel.tutorial.routing.config.ApplicationContextConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationContextConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class FilteringSpringTest {

	@EndpointInject(uri = "mock:C")
	private MockEndpoint mockC;

	@EndpointInject(uri = "mock:afterC")
	private MockEndpoint mockAfterC;

	@EndpointInject(uri = "mock:amel")
	private MockEndpoint mockAmel;

	@EndpointInject(uri = "mock:filterOther")
	private MockEndpoint mockOther;

	@Produce
	private ProducerTemplate producer;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FilteringSpringTest.class);

	@Test
	public void testFirstFilter() throws Exception {
		mockC.expectedMessageCount(1);
		mockC.expectedPropertyReceived(Exchange.FILTER_MATCHED, true);

		mockAfterC.expectedMessageCount(1);
		// FILTER_MATCHED set to true if message matched previous Filter
		// Predicate
		mockAfterC.expectedPropertyReceived(Exchange.FILTER_MATCHED, true);

		mockAmel.expectedMessageCount(0);

		mockOther.expectedMessageCount(1);
		// FILTER_MATCHED set to true if message matched previous Filter
		// Predicate
		mockOther.expectedPropertyReceived(Exchange.FILTER_MATCHED, false);
		producer.sendBody("direct:filterstart", "Cooks Rocks");

		String mockOtherBody = mockOther.getExchanges().get(0).getIn()
				.getBody(String.class);
		LOGGER.info("mockOtherBody:{} ", mockOtherBody);

		mockC.assertIsSatisfied();
		mockAfterC.assertIsSatisfied();
		mockAmel.assertIsSatisfied();
		mockOther.assertIsSatisfied();
	}

	@Test
	public void testSecondFilter() throws Exception {
		mockC.expectedMessageCount(0);
		mockAfterC.expectedMessageCount(1);
		// FILTER_MATCHED set to true if message matched previous Filter
		// Predicate
		mockAfterC.expectedPropertyReceived(Exchange.FILTER_MATCHED, false);

		mockAmel.expectedMessageCount(1);
		mockAmel.expectedPropertyReceived(Exchange.FILTER_MATCHED, true);

		mockOther.expectedMessageCount(1);
		// FILTER_MATCHED set to true if message matched previous Filter
		// Predicate
		mockOther.expectedPropertyReceived(Exchange.FILTER_MATCHED, true);
		producer.sendBody("direct:filterstart", "amel is in Belgium");

		mockC.assertIsSatisfied();
		mockAfterC.assertIsSatisfied();
		mockAmel.assertIsSatisfied();
		mockOther.assertIsSatisfied();
	}

	@Test
	public void testOther() throws Exception {
		mockC.expectedMessageCount(0);
		mockAfterC.expectedMessageCount(1);

		// FILTER_MATCHED set to true if message matched previous Filter
		// Predicate
		mockAfterC.expectedPropertyReceived(Exchange.FILTER_MATCHED, false);

		mockAmel.expectedMessageCount(0);

		mockOther.expectedMessageCount(1);

		// FILTER_MATCHED set to true if message matched previous Filter
		// Predicate
		mockOther.expectedPropertyReceived(Exchange.FILTER_MATCHED, false);

		producer.sendBody("direct:filterstart", "Hello World");

		mockC.assertIsSatisfied();
		mockAfterC.assertIsSatisfied();
		mockAmel.assertIsSatisfied();
		mockOther.assertIsSatisfied();
	}

}
