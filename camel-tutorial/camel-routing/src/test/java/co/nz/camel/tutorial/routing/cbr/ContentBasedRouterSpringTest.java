package co.nz.camel.tutorial.routing.cbr;

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

/**
 * each test we need to reset spring context to clean every thing in Camel
 * context
 * 
 * @author dyuan
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ContentBasedRouterSpringTest {

	@EndpointInject(uri = "mock:cbrCamel")
	private MockEndpoint mockCamel;

	@EndpointInject(uri = "mock:cbrOther")
	private MockEndpoint mockOther;

	@Produce
	private ProducerTemplate producer;

	@Test
	public void testWhen() throws Exception {
		mockCamel.expectedMessageCount(1);
		mockOther.expectedMessageCount(0);
		producer.sendBody("direct:cbrStart", "Camel Rocks");
		mockCamel.assertIsSatisfied();
		mockOther.assertIsSatisfied();
	}

	@Test
	public void testOther() throws Exception {
		mockCamel.expectedMessageCount(0);
		mockOther.expectedMessageCount(1);
		producer.sendBody("direct:cbrStart", "Hello World");
		mockCamel.assertIsSatisfied();
		mockOther.assertIsSatisfied();
		// mockCamel.reset();
		// mockOther.reset();
	}

}
