package co.nz.camel.tutorial.error.dotry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.camel.CamelExecutionException;
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
import co.nz.camel.tutorial.error.shared.FlakyException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DoTryTest {
	@Produce
	private ProducerTemplate template;

	@EndpointInject(uri = "mock:dotryAfter")
	private MockEndpoint mockAfter;

	@EndpointInject(uri = "mock:dotryFinally")
	private MockEndpoint mockFinally;

	@EndpointInject(uri = "mock:dotryError")
	private MockEndpoint mockError;

	@EndpointInject(uri = "mock:dotryBefore")
	private MockEndpoint mockBefore;

	@Test
	public void testDoTryHappy() throws Exception {
		mockBefore.expectedBodiesReceived("Foo");
		mockError.expectedMessageCount(0);
		mockFinally.expectedBodiesReceived("Made it!");
		mockAfter.expectedBodiesReceived("Made it!");

		String response = null;
		try {
			response = template.requestBody("direct:dotryStart", "Foo",
					String.class);
		} catch (Throwable e) {
			fail("Shouldn't get here");
		}
		assertEquals("Made it!", response);

		mockBefore.assertIsSatisfied();
		mockError.assertIsSatisfied();
		mockFinally.assertIsSatisfied();
		mockAfter.assertIsSatisfied();
	}

	@Test
	public void testDoTryError() throws Exception {
		mockBefore.expectedBodiesReceived("Kaboom");
		mockError.expectedBodiesReceived("Kaboom");
		mockFinally.expectedBodiesReceived("Something Bad Happened!");
		mockAfter.expectedBodiesReceived("Something Bad Happened!");

		String response = null;
		try {
			response = template.requestBody("direct:dotryStart", "Kaboom",
					String.class);
		} catch (Throwable e) {
			fail("Shouldn't get here");
		}
		assertEquals("Something Bad Happened!", response);

		mockBefore.assertIsSatisfied();
		mockError.assertIsSatisfied();
		mockFinally.assertIsSatisfied();
		mockAfter.assertIsSatisfied();
	}

	@Test
	public void testDoTryUnHandled() throws Exception {
		mockBefore.expectedBodiesReceived("Kaboom");
		mockError.expectedBodiesReceived("Kaboom");
		mockFinally.expectedBodiesReceived("Something Bad Happened!");
		mockAfter.expectedMessageCount(0);

		String response = null;
		boolean threwException = false;
		try {
			response = template.requestBody("direct:dotryUnhandled", "Kaboom",
					String.class);
		} catch (Throwable e) {
			threwException = true;
			assertTrue(e instanceof CamelExecutionException);
			CamelExecutionException cee = (CamelExecutionException) e;
			Throwable cause = cee.getCause();
			assertTrue(cause instanceof FlakyException);
		}
		assertTrue(threwException);
		assertNull(response);

		mockBefore.assertIsSatisfied();
		mockError.assertIsSatisfied();
		mockFinally.assertIsSatisfied();
		mockAfter.assertIsSatisfied();
	}

	@Test
	public void testDoTryConditional() throws Exception {
		mockBefore.expectedBodiesReceived("Kaboom", "Kaboom");
		mockError.expectedBodiesReceived("Kaboom");
		mockFinally.expectedBodiesReceived("Something Bad Happened!", "Kaboom");
		mockAfter.expectedBodiesReceived("Something Bad Happened!");

		String response = null;
		try {
			response = template.requestBody("direct:dotryConditional",
					"Kaboom", String.class);
		} catch (Throwable e) {
			fail("Shouldn't get here");
		}
		assertEquals("Something Bad Happened!", response);

		response = null;
		boolean threwException = false;
		try {
			response = template.requestBodyAndHeader("direct:dotryConditional",
					"Kaboom", "jedi",
					"This isn't the Exception you are looking for...",
					String.class);
		} catch (Throwable e) {
			threwException = true;
			assertTrue(e instanceof CamelExecutionException);
			CamelExecutionException cee = (CamelExecutionException) e;
			Throwable cause = cee.getCause();
			assertTrue(cause instanceof FlakyException);
		}
		assertTrue(threwException);
		assertNull(response);

		mockBefore.assertIsSatisfied();
		mockError.assertIsSatisfied();
		mockFinally.assertIsSatisfied();
		mockAfter.assertIsSatisfied();
	}

}
