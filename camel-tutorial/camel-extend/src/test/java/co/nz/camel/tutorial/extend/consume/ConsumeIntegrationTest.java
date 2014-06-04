package co.nz.camel.tutorial.extend.consume;

import static org.junit.Assert.assertEquals;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.extend.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class ConsumeIntegrationTest {

	@Produce
	private ProducerTemplate template;

	@Test
	public void testPojoConsume() throws Exception {
		final String response = template.requestBody("activemq:queue:sayhello",
				"Scott", String.class);

		assertEquals("Hello Scott", response);
	}

}
