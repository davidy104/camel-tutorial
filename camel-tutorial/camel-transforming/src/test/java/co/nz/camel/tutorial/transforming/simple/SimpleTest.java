package co.nz.camel.tutorial.transforming.simple;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.transforming.config.ApplicationConfiguration;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class SimpleTest {

	@Produce
	private ProducerTemplate template;

	@Test
	public void testSimple() throws Exception {
		String response = template.requestBody("direct:simpleStart",
				"Camel Rocks", String.class);
		assertEquals("Hello Camel Rocks", response);
	}

}
