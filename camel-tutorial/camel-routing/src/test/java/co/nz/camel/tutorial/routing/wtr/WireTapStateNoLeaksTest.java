package co.nz.camel.tutorial.routing.wtr;

import static org.apache.camel.language.simple.SimpleLanguage.simple;
import static org.junit.Assert.assertNotSame;
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
public class WireTapStateNoLeaksTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(WireTapStateNoLeaksTest.class);

	@Produce(uri = "direct:wtsnlstart")
	protected ProducerTemplate template;

	@EndpointInject(uri = "mock:wtsnltapped")
	private MockEndpoint tapped;

	@EndpointInject(uri = "mock:wtsnlout")
	private MockEndpoint out;

	@Test
	public void testOutMessageUnaffectedByTappedRoute()
			throws InterruptedException {
		final Cheese cheese = new Cheese();
		cheese.setAge(1);

		// should receive same object that was sent
		out.expectedBodiesReceived(cheese);
		out.setExpectedMessageCount(1);
		// since copy was sent to wire tap, age should remain unchanged
		out.message(0).body().isEqualTo(cheese);
		out.message(0).expression(simple("${body.age} == 1"));

		tapped.setExpectedMessageCount(1);
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

		assertNotSame(outCheese, tappedCheese);
		assertSame(outCheese, cheese);
	}

}
