package co.nz.camel.tutorial.error.logging;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

import co.nz.camel.tutorial.error.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class LoggingTest {
	@Produce
	private ProducerTemplate template;

	@EndpointInject(uri = "mock:loggingResult")
	private MockEndpoint mockResult;

	@Test
	public void testLogging() throws Exception {
		mockResult.expectedMessageCount(1);

		try {
			template.sendBody("direct:loggingStart", "Foo");
		} catch (Throwable e) {
			fail("Shouldn't get here");
		}

		boolean threwException = false;
		try {
			template.sendBody("direct:loggingStart", "KaBoom");
		} catch (Throwable e) {
			threwException = true;
		}
		assertTrue(threwException);
		mockResult.assertIsSatisfied();
	}

	@Test
	public void testLoggingRouteSpecific() throws Exception {
		mockResult.expectedMessageCount(1);

		try {
			template.sendBody("direct:loggingRouteSpecific", "Foo");
		} catch (Throwable e) {
			fail("Shouldn't get here");
		}

		boolean threwException = false;
		try {
			template.sendBody("direct:loggingRouteSpecific", "KaBoom");
		} catch (Throwable e) {
			threwException = true;
		}
		assertTrue(threwException);
		mockResult.assertIsSatisfied();
	}

}
