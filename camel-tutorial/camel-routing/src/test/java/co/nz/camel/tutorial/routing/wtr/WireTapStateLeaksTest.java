package co.nz.camel.tutorial.routing.wtr;

import static org.apache.camel.language.simple.SimpleLanguage.simple;
import static org.junit.Assert.assertSame;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.routing.config.ApplicationContextConfiguration;
import co.nz.camel.tutorial.routing.model.Cheese;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationContextConfiguration.class)
public class WireTapStateLeaksTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(WireTapStateLeaksTest.class);

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

		final Cheese outCheese = out.getReceivedExchanges().get(0).getIn()
				.getBody(Cheese.class);
		final Cheese tappedCheese = tapped.getReceivedExchanges().get(0)
				.getIn().getBody(Cheese.class);

		LOGGER.info("cheese = {}; out = {}; tapped = {}", cheese, outCheese,
				tappedCheese);

		LOGGER.info("cheese == out = {}", (cheese == outCheese));
		LOGGER.info("cheese == tapped = {}", (cheese == tappedCheese));
		LOGGER.info("out == tapped = {}", (outCheese == tappedCheese));

		assertSame(outCheese, tappedCheese);
		assertSame(outCheese, cheese);
	}

}
