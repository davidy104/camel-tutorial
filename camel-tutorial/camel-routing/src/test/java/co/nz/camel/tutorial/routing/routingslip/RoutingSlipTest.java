package co.nz.camel.tutorial.routing.routingslip;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.routing.config.ApplicationContextConfiguration;
import co.nz.camel.tutorial.routing.route.routingslip.RoutingSlipRouteBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationContextConfiguration.class)
public class RoutingSlipTest {
	@Produce
	protected ProducerTemplate template;

	@EndpointInject(uri = "mock:rsa")
	private MockEndpoint mockA;

	@EndpointInject(uri = "mock:rsOther")
	private MockEndpoint mockOther;

	@Test
	public void testRoutingSlip() throws Exception {
		mockA.expectedMessageCount(1);
		mockOther.expectedMessageCount(1);

		template.sendBodyAndHeader("direct:rsStart", "Camel Rocks",
				RoutingSlipRouteBuilder.ROUTING_SLIP_HEADER,
				"mock:rsa,direct:rsOther");

		mockA.assertIsSatisfied();
		mockOther.assertIsSatisfied();
	}

}
