package co.nz.camel.tutorial.extend.predicate;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.extend.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class PredicateBeanBindingTest {

	@EndpointInject(uri = "mock:mpbbrBoston")
	private MockEndpoint mockBoston;

	@Produce
	private ProducerTemplate template;

	@Test
	public void testMyPredicateBeanBinding() throws Exception {
		final String newYork = "<someXml><city>New York</city></someXml>";
		final String boston = "<someXml><city>Boston</city></someXml>";

		mockBoston.expectedBodiesReceived(boston);

		template.sendBody("direct:mpbbrStart", newYork);
		template.sendBody("direct:mpbbrStart", boston);

		mockBoston.assertIsSatisfied();
	}

}
