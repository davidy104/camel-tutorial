package co.nz.camel.tutorial.error.oncompletion;

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
public class OnCompletionSlowTest {
	@Produce
	private ProducerTemplate template;

	@EndpointInject(uri = "mock:completed")
	private MockEndpoint mockCompleted;

	@EndpointInject(uri = "mock:failed")
	private MockEndpoint mockFailed;

	@EndpointInject(uri = "mock:global")
	private MockEndpoint mockGlobal;

	@EndpointInject(uri = "mock:outAnotherRouteBuilder")
	private MockEndpoint mockOutAnotherRouteBuilder;

	@Test
	public void testOnCompletionDefinedAtRouteLevel()
			throws InterruptedException {
		mockCompleted.setExpectedMessageCount(1);
		mockCompleted.message(0).body()
				.isEqualTo("this message should be fine");

		template.asyncSendBody("direct:onCompletion",
				"this message should be fine");
		mockCompleted.assertIsSatisfied();
	}

	@Test
	public void testOnCompletionFailureAtRouteLevel()
			throws InterruptedException {
		mockFailed.setExpectedMessageCount(1);
		mockFailed.message(0).body().isEqualTo("this message should explode");
		template.asyncSendBody("direct:onCompletionFailure",
				"this message should explode");
		mockFailed.assertIsSatisfied();
	}

	@Test
	public void testOnCompletionFailureConditional()
			throws InterruptedException {
		mockFailed.setExpectedMessageCount(1);
		mockFailed.message(0).body().isEqualTo("this message should explode");

		template.asyncSendBody("direct:onCompletionFailureConditional",
				"this message should explode");
		mockFailed.assertIsSatisfied();
	}

	@Test
	public void testOnCompletionGlobal() throws InterruptedException {
		mockGlobal.setExpectedMessageCount(1);
		mockGlobal.message(0).body().isEqualTo("this message should explode");
		template.asyncSendBody("direct:noOnCompletion",
				"this message should explode");
		mockGlobal.assertIsSatisfied();
	}

	@Test
	public void testOnCompletionGlobalNotInvokedFromAnotherRouteBuilder()
			throws InterruptedException {
		mockGlobal.setExpectedMessageCount(0);

		mockOutAnotherRouteBuilder.setExpectedMessageCount(1);
		mockOutAnotherRouteBuilder.message(0).body().isEqualTo("test message");

		template.asyncSendBody("direct:inAnotherRouteBuilder", "test message");

		Thread.sleep(100); // give global a chance to kick in, if it will
		mockOutAnotherRouteBuilder.assertIsSatisfied();
		mockGlobal.assertIsSatisfied();
	}

	@Test
	public void testOnCompletionChained() throws InterruptedException {
		mockFailed.setExpectedMessageCount(1);
		mockFailed.message(0).body().isEqualTo("this message should explode");

		mockCompleted.setExpectedMessageCount(1);
		mockCompleted.message(0).body()
				.isEqualTo("this message should complete");

		// here we have 2 onCompletions set - one on a top-level route, and
		// another on a sub-route
		// both should be triggered depending on success or failure
		template.asyncSendBody("direct:chained", "this message should explode");
		template.asyncSendBody("direct:chained", "this message should complete");

		mockFailed.assertIsSatisfied();
		mockCompleted.assertIsSatisfied();
	}

	@Test
	public void testOnCompletionChoice() throws InterruptedException {
		mockFailed.setExpectedMessageCount(1);
		mockFailed.message(0).body().isEqualTo("this message should explode");

		mockCompleted.setExpectedMessageCount(1);
		mockCompleted.message(0).body()
				.isEqualTo("this message should complete");

		// here we have 2 onCompletions set - one on a top-level route, and
		// another on a sub-route
		// both should be triggered depending on success or failure
		template.asyncSendBody("direct:onCompletionChoice",
				"this message should explode");
		template.asyncSendBody("direct:onCompletionChoice",
				"this message should complete");

		mockFailed.assertIsSatisfied();
		mockCompleted.assertIsSatisfied();
	}
}
