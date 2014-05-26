package co.nz.camel.tutorial.routing.test;

import static org.apache.camel.language.simple.SimpleLanguage.simple;

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
import co.nz.camel.tutorial.routing.model.Cheese;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class WireTapStateLeaksTest {

	@Produce(uri = "direct:wtslStart")
	protected ProducerTemplate template;

	@EndpointInject(uri = "mock:wtsltapped")
	private MockEndpoint tapped;

	@EndpointInject(uri = "mock:wtslout")
	private MockEndpoint out;

	@Test
	public void testOutMessageAffectedByTappedRoute()
			throws InterruptedException {
		final Cheese cheese = new Cheese();
		cheese.setAge(1);

		// should receive same object that was sent
		out.expectedBodiesReceived(cheese);
		// since no copy, should have updated age
		out.message(0).expression(simple("${body.age} == 2"));

		// should receive same object that was sent
		tapped.expectedBodiesReceived(cheese);
		tapped.message(0).expression(simple("${body.age} == 2"));
		tapped.setResultWaitTime(1000);

		template.sendBody(cheese);

		out.assertIsSatisfied();
		tapped.assertIsSatisfied();
	}

}
