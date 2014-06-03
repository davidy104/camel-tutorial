package co.nz.camel.tutorial.transforming.enrich;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.transforming.config.ApplicationConfiguration;
import co.nz.camel.tutorial.transforming.json.JsonTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class EnrichXsltTest {
	@Produce
	private ProducerTemplate template;

	@Resource
	private CamelContext context;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(JsonTest.class);

	@Test
	public void testEnrichXslt() throws Exception {
		final InputStream resource = getClass().getClassLoader()
				.getResourceAsStream("bookstore.xml");
		final String request = context.getTypeConverter().convertTo(
				String.class, resource);

		String response = template.requestBody("direct:xsltEnrichStart",
				request, String.class);

		LOGGER.info("Response = {}", response);
		assertEquals(
				"<books><title lang=\"en\">Apache Camel Developer's Cookbook</title><title lang=\"en\">Learning XML</title></books>",
				response);
	}
}
