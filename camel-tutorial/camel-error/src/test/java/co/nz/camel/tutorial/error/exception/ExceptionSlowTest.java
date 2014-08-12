package co.nz.camel.tutorial.error.exception;

import static org.junit.Assert.assertEquals;
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
public class ExceptionSlowTest {

	@Produce
	private ProducerTemplate template;

	@EndpointInject(uri = "mock:exceptionResult")
	private MockEndpoint mockResult;

	@EndpointInject(uri = "mock:eResult")
	private MockEndpoint mockeResult;

	@EndpointInject(uri = "mock:exceptionError")
	private MockEndpoint mockError;

	@EndpointInject(uri = "mock:exceptionGenericerror")
	private MockEndpoint mockGenericError;

	@EndpointInject(uri = "mock:exceptionIgnore")
	private MockEndpoint mockIgnore;

	@EndpointInject(uri = "mock:handleExceptionError")
	private MockEndpoint mockHandleExceptionError;

	@Test
	public void testException() throws Exception {
		mockResult.expectedMessageCount(1);
		mockError.expectedMessageCount(1);
		mockGenericError.expectedMessageCount(0);

		try {
			template.sendBody("direct:exceptionStart", "Foo");
		} catch (Throwable e) {
			fail("Shouldn't get here");
		}

		mockResult.assertIsSatisfied();

		boolean threwException = false;
		try {
			template.sendBody("direct:exceptionStart", "KaBoom");
		} catch (Throwable e) {
			threwException = true;
		}
		assertTrue(threwException);

		mockResult.assertIsSatisfied();
		mockError.assertIsSatisfied();
		mockGenericError.assertIsSatisfied();
	}

	@Test
	public void testExceptionHandled() throws Exception {
//		mockResult.expectedBodiesReceived("All Good");
		mockHandleExceptionError
				.expectedBodiesReceived("Something Bad Happened!");
		mockGenericError.expectedMessageCount(0);

		String response;

		try {
			response = template.requestBody("direct:exceptionHandled", "Foo",
					String.class);
			assertEquals("All Good", response);
		} catch (Throwable e) {
			fail("Shouldn't get here");
		}

		mockResult.assertIsSatisfied();

		try {
			response = template.requestBody("direct:exceptionHandled",
					"KaBoom", String.class);
			assertEquals("Something Bad Happened!", response);
		} catch (Throwable e) {
			fail("Shouldn't get here either");
		}
		
		String result = mockeResult.getExchanges().get(0).getIn().getBody(String.class);
		System.out.println("result: "+result);

//		mockResult.assertIsSatisfied();
		mockHandleExceptionError.assertIsSatisfied();
		mockGenericError.assertIsSatisfied();

	}

	@Test
	public void testExceptionContinue() throws Exception {
		mockResult.expectedBodiesReceived("All Good", "All Good");
		mockIgnore.expectedMessageCount(1);
		mockGenericError.expectedMessageCount(0);

		String response;

		try {
			response = template.requestBody("direct:exceptionContinue", "Foo",
					String.class);
			assertEquals("All Good", response);
		} catch (Throwable e) {
			fail("Shouldn't get here");
		}

		try {
			response = template.requestBody("direct:exceptionContinue",
					"KaBoom", String.class);
			assertEquals("All Good", response);
		} catch (Throwable e) {
			fail("Shouldn't get here either");
		}

		mockResult.assertIsSatisfied();
		mockIgnore.assertIsSatisfied();
		mockGenericError.assertIsSatisfied();
	}

}
