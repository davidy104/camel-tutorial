package co.nz.camel.tutorial.transforming.enrich;

import static org.junit.Assert.assertEquals;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.transforming.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class EnrichTest {

	@Produce
	private ProducerTemplate template;

	@Test
	public void testEnrich() throws Exception {
		String response = template.requestBody("direct:enrichStart", "MA",
				String.class);

		assertEquals("Massachusetts", response);

		response = template.requestBody("direct:enrichStart", "CA",
				String.class);

		assertEquals("California", response);
	}

}
