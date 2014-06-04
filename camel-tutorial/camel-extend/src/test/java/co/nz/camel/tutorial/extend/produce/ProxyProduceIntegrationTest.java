package co.nz.camel.tutorial.extend.produce;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.extend.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class ProxyProduceIntegrationTest {

	@Resource
	private ProxyProduce proxyProduce;

	@Test
	public void testPojoProxyProduce() throws Exception {
		final String response = proxyProduce.doSomething("Scott");
		assertEquals("Hello Scott", response);
	}

}
