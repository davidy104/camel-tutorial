package co.nz.camel.tutorial.transforming.json;

import static org.junit.Assert.assertEquals;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.transforming.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JsonJacksonTest {
	@Produce
	private ProducerTemplate template;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(JsonTest.class);

	@Test
	public void testJsonJacksonMarshal() throws Exception {
		View view = new View();

		view.setAge(29);
		view.setHeight(46);
		view.setWeight(34);

		String response = template.requestBody("direct:jsonJacksonMarshal",
				view, String.class);

		LOGGER.info(response);
		assertEquals("{\"age\":29,\"weight\":34,\"height\":46}", response);
	}

	@Test
	public void testJsonJacksonUnmarshal() throws Exception {
		final String request = "{\"age\":29,\"weight\":34,\"height\":46}";

		View response = template.requestBody("direct:jsonJacksonUnmarshal",
				request, View.class);

		View view = new View();

		view.setAge(29);
		view.setHeight(46);
		view.setWeight(34);

		assertEquals(view, response);
	}

}
