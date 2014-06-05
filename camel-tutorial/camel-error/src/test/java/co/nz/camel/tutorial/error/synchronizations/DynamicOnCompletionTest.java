package co.nz.camel.tutorial.error.synchronizations;

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
public class DynamicOnCompletionTest {
	public static final String COMPLETING_BODY = "this message should complete";
	public static final String FAILING_BODY = "this message should explode";
	@Produce
	private ProducerTemplate template;

	@EndpointInject(uri = "mock:synchronizationsStart")
	private MockEndpoint mockStart;

	@EndpointInject(uri = "mock:synchronizationsCancel")
	private MockEndpoint mockCancel;

	@EndpointInject(uri = "mock:synchronizationsConfirm")
	private MockEndpoint mockConfirm;

	@Test
	public void testOnCompletionCompleted() throws InterruptedException {
		mockStart.setExpectedMessageCount(1);
		mockStart.message(0).body().isEqualTo(COMPLETING_BODY);
		mockCancel.setExpectedMessageCount(0);
		mockConfirm.setExpectedMessageCount(1);
		mockConfirm.message(0).body().isEqualTo(COMPLETING_BODY);

		template.asyncSendBody("direct:synchronizationsIn", COMPLETING_BODY);

		mockStart.assertIsSatisfied();
		mockCancel.assertIsSatisfied();
		mockConfirm.assertIsSatisfied();
	}

	@Test
	public void testOnCompletionFailed() throws InterruptedException {
		mockStart.setExpectedMessageCount(1);
		mockStart.message(0).body().isEqualTo(FAILING_BODY);

		mockCancel.setExpectedMessageCount(1);
		mockCancel.message(0).body().isEqualTo(FAILING_BODY);

		mockConfirm.setExpectedMessageCount(0);

		template.asyncSendBody("direct:synchronizationsIn", FAILING_BODY);

		mockStart.assertIsSatisfied();
		mockCancel.assertIsSatisfied();
		mockConfirm.assertIsSatisfied();
	}

}
