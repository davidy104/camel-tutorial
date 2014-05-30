package co.nz.camel.tutorial.extend.produce;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.extend.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class ProduceTest {

	@Resource
	private CamelContext context;

	@Test
	public void testPojoProduce() throws Exception {
		// lookup our POJO; could also use Spring's
		// ApplicationContext.getBean(...)
		final ProducePojo producer = context.getRegistry().lookupByNameAndType(
				"producer", ProducePojo.class);

		final String response = producer.sayHello("Scott");

		assertEquals("Hello Scott", response);
	}

}
