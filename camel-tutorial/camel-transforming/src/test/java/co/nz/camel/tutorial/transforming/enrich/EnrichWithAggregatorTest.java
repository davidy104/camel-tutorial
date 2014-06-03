package co.nz.camel.tutorial.transforming.enrich;

import static org.junit.Assert.*;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.transforming.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class EnrichWithAggregatorTest {
	@Produce
	private ProducerTemplate template;

	@Test
	public void testEnrichWithAggregator() throws Exception {
		String response = template.requestBody(
				"direct:enrichWithAggregatorStart", "Hello MA", String.class);

		assertEquals("Hello Massachusetts", response);

		response = template.requestBody("direct:enrichWithAggregatorStart",
				"I like CA", String.class);

		assertEquals("I like California", response);
	}

}
