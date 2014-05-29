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
public class LoadBalancerFailoverTest {

	@Produce(uri = "direct:lbfStart")
	protected ProducerTemplate template;

	@EndpointInject(uri = "mock:lbfFirst")
	private MockEndpoint first;

	@EndpointInject(uri = "mock:lbfThird")
	private MockEndpoint third;

	@EndpointInject(uri = "mock:lbfOut")
	private MockEndpoint out;

	@Test
	public void testMessageLoadBalancedWithFailover()
			throws InterruptedException {
		String messageBody = "Client has bought something";
		first.setExpectedMessageCount(1);
		third.setExpectedMessageCount(1);
		out.setExpectedMessageCount(2);

		template.sendBody(messageBody);
		template.sendBody(messageBody);

		first.assertIsSatisfied();
		third.assertIsSatisfied();
		out.assertIsSatisfied();
	}

}
