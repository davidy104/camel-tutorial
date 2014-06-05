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
public class OnCompletionMultipleSlowTest {
	@Produce
	private ProducerTemplate template;

	@EndpointInject(uri = "mock:completed")
	private MockEndpoint mockCompleted;

	@EndpointInject(uri = "mock:failed")
	private MockEndpoint mockFailed;

	@EndpointInject(uri = "mock:globalCompleted")
	private MockEndpoint mockGlobalCompleted;

	@EndpointInject(uri = "mock:globalFailed")
	private MockEndpoint mockGlobalFailed;

	@Test
	public void testOnCompletionDefinedAtRouteLevel()
			throws InterruptedException {
		// this block is defined first, the definition of the failure only will
		// be considered
		mockCompleted.setExpectedMessageCount(0);
		mockFailed.setExpectedMessageCount(0);
		// neither of the global completion blocks should be triggered
		mockGlobalCompleted.setExpectedMessageCount(0);
		mockGlobalFailed.setExpectedMessageCount(0);

		template.asyncSendBody("direct:in", "this message should be fine");

		mockCompleted.assertIsSatisfied();
		mockFailed.assertIsSatisfied();
		mockGlobalCompleted.assertIsSatisfied();
		mockGlobalFailed.assertIsSatisfied();
	}

	@Test
	public void testOnCompletionFailureAtRouteLevel()
			throws InterruptedException {
		mockFailed.setExpectedMessageCount(1);
		mockFailed.message(0).body().isEqualTo("this message should explode");
		mockCompleted.setExpectedMessageCount(0);
		// neither of the global completion blocks should be triggered
		mockGlobalCompleted.setExpectedMessageCount(0);
		mockGlobalFailed.setExpectedMessageCount(0);

		template.asyncSendBody("direct:in", "this message should explode");

		mockCompleted.assertIsSatisfied();
		mockFailed.assertIsSatisfied();
		mockGlobalCompleted.assertIsSatisfied();
		mockGlobalFailed.assertIsSatisfied();
	}

	@Test
	public void testOnCompletionDefinedAtGlobalLevel()
			throws InterruptedException {
		mockGlobalCompleted.setExpectedMessageCount(1);
		mockGlobalCompleted.message(0).body()
				.isEqualTo("this message should be fine");

		mockGlobalFailed.setExpectedMessageCount(0);

		template.asyncSendBody("direct:global", "this message should be fine");

		mockGlobalCompleted.assertIsSatisfied();
		mockGlobalFailed.assertIsSatisfied();
	}

	@Test
	public void testOnCompletionFailureAtGlobalLevel()
			throws InterruptedException {
		// this block is defined last, the definition of the completion only
		// will be considered
		mockGlobalFailed.setExpectedMessageCount(0);
		mockGlobalCompleted.setExpectedMessageCount(0);

		template.asyncSendBody("direct:in", "this message should explode");

		mockGlobalCompleted.assertIsSatisfied();
		mockGlobalFailed.assertIsSatisfied();
	}

}
