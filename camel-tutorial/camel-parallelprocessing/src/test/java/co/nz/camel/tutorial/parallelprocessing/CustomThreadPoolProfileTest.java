package co.nz.camel.tutorial.parallelprocessing;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.parallelprocessing.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class CustomThreadPoolProfileTest {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CustomThreadPoolProfileTest.class);

	@EndpointInject(uri = "mock:out")
	private MockEndpoint mockOut;

	@Produce(uri = "direct:multiTostart")
	protected ProducerTemplate template;

	@Test
	public void testProcessedByCustomThreadPool() throws InterruptedException {
		final int messageCount = 50;
		mockOut.setExpectedMessageCount(messageCount);
		mockOut.setResultWaitTime(6000);

		for (int i = 0; i < messageCount; i++) {
			template.asyncSendBody("direct:in", "Message[" + i + "]");
		}

		mockOut.assertIsSatisfied();
		// no way to check programatically whether the profile was actually
		// engaged, as Camel uses the
		// default naming strategy for threads
	}
}
