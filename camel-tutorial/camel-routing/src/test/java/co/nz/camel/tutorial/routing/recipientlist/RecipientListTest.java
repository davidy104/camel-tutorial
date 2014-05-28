package co.nz.camel.tutorial.routing.recipientlist;

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
public class RecipientListTest {

	@Produce(uri = "direct:rclstart")
	protected ProducerTemplate template;

	@EndpointInject(uri = "mock:order.priority")
	private MockEndpoint orderPriority;

	@EndpointInject(uri = "mock:order.normal")
	private MockEndpoint orderNormal;

	@EndpointInject(uri = "mock:billing")
	private MockEndpoint billing;

	@EndpointInject(uri = "mock:unrecognized")
	private MockEndpoint unrecognized;

	@Test
	public void testNormalOrder() throws InterruptedException {
		String messageBody = "book";

		billing.setExpectedMessageCount(1);
		orderNormal.setExpectedMessageCount(1);
		orderPriority.setExpectedMessageCount(0);
		unrecognized.setExpectedMessageCount(0);

		template.sendBodyAndHeader(messageBody, "orderType", "normal");

		billing.assertIsSatisfied();
		orderNormal.assertIsSatisfied();
		orderPriority.assertIsSatisfied();
		unrecognized.assertIsSatisfied();
	}

	@Test
	public void testPriorityOrder() throws InterruptedException {
		String messageBody = "book";

		billing.setExpectedMessageCount(1);
		orderNormal.setExpectedMessageCount(0);
		orderPriority.setExpectedMessageCount(1);
		unrecognized.setExpectedMessageCount(0);

		template.sendBodyAndHeader(messageBody, "orderType", "priority");

		billing.assertIsSatisfied();
		orderNormal.assertIsSatisfied();
		orderPriority.assertIsSatisfied();
		unrecognized.assertIsSatisfied();
	}

	@Test
	public void testUnknownOrder() throws InterruptedException {
		String messageBody = "book";

		billing.setExpectedMessageCount(0);
		orderNormal.setExpectedMessageCount(0);
		orderPriority.setExpectedMessageCount(0);
		unrecognized.setExpectedMessageCount(1);

		template.sendBody(messageBody);

		billing.assertIsSatisfied();
		orderNormal.assertIsSatisfied();
		orderPriority.assertIsSatisfied();
		unrecognized.assertIsSatisfied();
	}

}
