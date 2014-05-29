package co.nz.camel.tutorial.routing.dynamicrouter;

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
public class DynamicRouterAnnotatedTest {

	@EndpointInject(uri = "mock:a")
	private MockEndpoint mockA;

	@EndpointInject(uri = "mock:b")
	private MockEndpoint mockB;

	@EndpointInject(uri = "mock:c")
	private MockEndpoint mockC;

	@EndpointInject(uri = "mock:dymOther")
	private MockEndpoint mockOther;

	@EndpointInject(uri = "mock:result")
	private MockEndpoint mockResult;

	@Produce
	private ProducerTemplate template;

	@Test
	public void testDynamicRouterAnnotated() throws Exception {
		mockA.expectedMessageCount(1);
		mockB.expectedMessageCount(1);
		mockC.expectedMessageCount(1);
		mockOther.expectedMessageCount(1);
		mockResult.expectedMessageCount(1);

		template.sendBody("direct:dymaStart", "Camel Rocks");

		mockA.assertIsSatisfied();
		mockB.assertIsSatisfied();
		mockC.assertIsSatisfied();
		mockOther.assertIsSatisfied();
		mockResult.assertIsSatisfied();
	}

}
