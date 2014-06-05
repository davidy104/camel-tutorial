package co.nz.camel.tutorial.error.retryconditional;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.error.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class RetryConditionalTest {
	@Produce(uri = "direct:retryconditionalStart")
	private ProducerTemplate template;

	@EndpointInject(uri = "mock:retryconditionalResult")
	private MockEndpoint mockResult;

	@Test
	public void testRetry() throws Exception {

		mockResult.expectedMessageCount(1);
		mockResult.allMessages().header(Exchange.REDELIVERED).isEqualTo(true);
		mockResult.allMessages().header(Exchange.REDELIVERY_COUNTER)
				.isEqualTo(1);

		template.sendBody("Foo");

		mockResult.assertIsSatisfied();
	}

}
