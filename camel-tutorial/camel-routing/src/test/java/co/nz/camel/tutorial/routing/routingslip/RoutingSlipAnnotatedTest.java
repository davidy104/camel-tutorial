package co.nz.camel.tutorial.routing.routingslip;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.routing.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@Ignore
public class RoutingSlipAnnotatedTest {
	@Produce
	protected ProducerTemplate template;

	@EndpointInject(uri = "mock:rsaA")
	private MockEndpoint mockA;

	@EndpointInject(uri = "mock:rsOther")
	private MockEndpoint mockOther;

	@EndpointInject(uri = "mock:rsAnnotatedOneMore")
	private MockEndpoint mockOneMore;

	@Test
	public void testRoutingSlipAnnotated() throws Exception {
		mockA.expectedMessageCount(1);
		mockOther.expectedMessageCount(1);
		mockOneMore.expectedMessageCount(1);

		template.sendBodyAndHeader("direct:rsAnnotatedStart", "Camel Rocks",
				"myRoutingSlipHeader", "mock:rsaA,direct:rsOther");

		mockA.assertIsSatisfied();
		mockOther.assertIsSatisfied();
		mockOneMore.assertIsSatisfied();
	}

}
