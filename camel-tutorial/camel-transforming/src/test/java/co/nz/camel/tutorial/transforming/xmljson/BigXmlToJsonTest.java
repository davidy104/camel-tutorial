package co.nz.camel.tutorial.transforming.xmljson;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Map;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.transforming.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class BigXmlToJsonTest {
	@Produce
	private ProducerTemplate template;

	@EndpointInject(uri = "mock:jacksonUnmarshalResult")
	private MockEndpoint mockResult;

	@EndpointInject(uri = "mock:XmlJsonMarshalResult")
	private MockEndpoint mockXmlJsonResult;

	private String xmlRequest;

	private static final String XML_REQUEST_FILE = "/cenotaph-new.xml";

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BigXmlToJsonTest.class);

	@Before
	public void initial() throws Exception {
		URL url = BigXmlToJsonTest.class.getResource(XML_REQUEST_FILE);
		try (FileInputStream fisTargetFile = new FileInputStream(new File(
				url.getFile()))) {
			xmlRequest = IOUtils.toString(fisTargetFile, "UTF-8");
		}

	}

	@Test
	@Ignore
	public void testJacksonUnmarshal() throws Exception {
		template.requestBody("direct:jacksonUnmarshal", xmlRequest);

		Map<String, Object> rdfProcessResponse = mockResult.getExchanges()
				.get(0).getIn().getBody(Map.class);
	}

	@Test
	public void testXmljsonmarshal() throws Exception {
		template.requestBody("direct:XmlJsonMarshal", xmlRequest);
		Map<String, Object> rdfProcessResponse = mockXmlJsonResult
				.getExchanges().get(0).getIn().getBody(Map.class);
	}

}
