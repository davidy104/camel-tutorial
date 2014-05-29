package co.nz.camel.tutorial.routing.loadbalancer;

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
public class LoadBalancerStickyTest {
	@Produce(uri = "direct:lbStart")
	protected ProducerTemplate template;

	@EndpointInject(uri = "mock:lbFirst")
	private MockEndpoint first;

	@EndpointInject(uri = "mock:lbSecond")
	private MockEndpoint second;

	@EndpointInject(uri = "mock:lbThird")
	private MockEndpoint third;

	@EndpointInject(uri = "mock:lbOut")
	private MockEndpoint out;

	@Test
	public void testMessageLoadBalancedToStickyEndpoints()
			throws InterruptedException {
		final String messageBody = "Client has bought something";

		first.setExpectedMessageCount(4);
		first.message(0).header("customerId").isEqualTo(0);
		first.message(1).header("customerId").isEqualTo(3);
		first.message(2).header("customerId").isEqualTo(0);
		first.message(3).header("customerId").isEqualTo(3);

		second.setExpectedMessageCount(2);
		second.message(0).header("customerId").isEqualTo(1);
		second.message(1).header("customerId").isEqualTo(1);

		third.setExpectedMessageCount(2);
		third.message(0).header("customerId").isEqualTo(2);
		third.message(1).header("customerId").isEqualTo(2);

		for (int messageCount = 0; messageCount < 2; messageCount++) {
			for (int customerCount = 0; customerCount < 4; customerCount++) {
				template.sendBodyAndHeader(messageBody, "customerId",
						customerCount);
			}
		}

		first.assertIsSatisfied();
		second.assertIsSatisfied();
		third.assertIsSatisfied();
	}

}
