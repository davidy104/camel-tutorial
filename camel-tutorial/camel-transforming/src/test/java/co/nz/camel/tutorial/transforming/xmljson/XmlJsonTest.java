package co.nz.camel.tutorial.transforming.xmljson;

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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.transforming.config.ApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class XmlJsonTest {
	@Produce
	private ProducerTemplate template;

	@Resource
	private CamelContext context;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(XmlJsonTest.class);

	@Test
	public void testXmlJsonMarshal() throws Exception {
		final InputStream resource = getClass().getClassLoader()
				.getResourceAsStream("bookstore.xml");
		final String request = context.getTypeConverter().convertTo(
				String.class, resource);

		final String response = template.requestBody("direct:XmlJsonMarshal",
				request, String.class);

		LOGGER.info(response);
		assertEquals(
				"["
						+ "{\"@category\":\"COOKING\",\"title\":{\"@lang\":\"en\",\"#text\":\"Everyday Italian\"},\"author\":\"Giada De Laurentiis\",\"year\":\"2005\",\"price\":\"30.00\"},"
						+ "{\"@category\":\"CHILDREN\",\"title\":{\"@lang\":\"en\",\"#text\":\"Harry Potter\"},\"author\":\"J K. Rowling\",\"year\":\"2005\",\"price\":\"29.99\"},"
						+ "{\"@category\":\"WEB\",\"title\":{\"@lang\":\"en\",\"#text\":\"Learning XML\"},\"author\":\"Erik T. Ray\",\"year\":\"2003\",\"price\":\"39.95\"},"
						+ "{\"@category\":\"PROGRAMMING\",\"title\":{\"@lang\":\"en\",\"#text\":\"Apache Camel Developer's Cookbook\"},\"author\":[\"Scott Cranton\",\"Jakub Korab\"],\"year\":\"2013\",\"price\":\"49.99\"}"
						+ "]", response);
	}

	@Test
	public void testXmlJsonUnmarshal() throws Exception {
		final String request = "["
				+ "{\"@category\":\"COOKING\",\"title\":{\"@lang\":\"en\",\"#text\":\"Everyday Italian\"},\"author\":\"Giada De Laurentiis\",\"year\":\"2005\",\"price\":\"30.00\"},"
				+ "{\"@category\":\"CHILDREN\",\"title\":{\"@lang\":\"en\",\"#text\":\"Harry Potter\"},\"author\":\"J K. Rowling\",\"year\":\"2005\",\"price\":\"29.99\"},"
				+ "{\"@category\":\"WEB\",\"title\":{\"@lang\":\"en\",\"#text\":\"Learning XML\"},\"author\":\"Erik T. Ray\",\"year\":\"2003\",\"price\":\"39.95\"},"
				+ "{\"@category\":\"PROGRAMMING\",\"title\":{\"@lang\":\"en\",\"#text\":\"Apache Camel Developer's Cookbook\"},\"author\":[\"Scott Cranton\",\"Jakub Korab\"],\"year\":\"2013\",\"price\":\"49.99\"}"
				+ "]";

		final String response = template.requestBody("direct:XmlJsonUnmarshal",
				request, String.class);

		LOGGER.info(response);
		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
						+ "<a>"
						+ "<e category=\"COOKING\"><author>Giada De Laurentiis</author><price>30.00</price><title lang=\"en\">Everyday Italian</title><year>2005</year></e>"
						+ "<e category=\"CHILDREN\"><author>J K. Rowling</author><price>29.99</price><title lang=\"en\">Harry Potter</title><year>2005</year></e>"
						+ "<e category=\"WEB\"><author>Erik T. Ray</author><price>39.95</price><title lang=\"en\">Learning XML</title><year>2003</year></e>"
						+ "<e category=\"PROGRAMMING\"><author><e>Scott Cranton</e><e>Jakub Korab</e></author><price>49.99</price><title lang=\"en\">Apache Camel Developer's Cookbook</title><year>2013</year></e>"
						+ "</a>\r\n", response);
	}

	@Test
	public void testXmlJsonUnmarshalBookstore() throws Exception {
		final String request = "["
				+ "{\"@category\":\"COOKING\",\"title\":{\"@lang\":\"en\",\"#text\":\"Everyday Italian\"},\"author\":\"Giada De Laurentiis\",\"year\":\"2005\",\"price\":\"30.00\"},"
				+ "{\"@category\":\"CHILDREN\",\"title\":{\"@lang\":\"en\",\"#text\":\"Harry Potter\"},\"author\":\"J K. Rowling\",\"year\":\"2005\",\"price\":\"29.99\"},"
				+ "{\"@category\":\"WEB\",\"title\":{\"@lang\":\"en\",\"#text\":\"Learning XML\"},\"author\":\"Erik T. Ray\",\"year\":\"2003\",\"price\":\"39.95\"},"
				+ "{\"@category\":\"PROGRAMMING\",\"title\":{\"@lang\":\"en\",\"#text\":\"Apache Camel Developer's Cookbook\"},\"author\":[\"Scott Cranton\",\"Jakub Korab\"],\"year\":\"2013\",\"price\":\"49.99\"}"
				+ "]";

		final String response = template.requestBody(
				"direct:XmlJsonUnmarshalBookstore", request, String.class);

		LOGGER.info(response);
		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
						+ "<bookstore>"
						+ "<book category=\"COOKING\"><author>Giada De Laurentiis</author><price>30.00</price><title lang=\"en\">Everyday Italian</title><year>2005</year></book>"
						+ "<book category=\"CHILDREN\"><author>J K. Rowling</author><price>29.99</price><title lang=\"en\">Harry Potter</title><year>2005</year></book>"
						+ "<book category=\"WEB\"><author>Erik T. Ray</author><price>39.95</price><title lang=\"en\">Learning XML</title><year>2003</year></book>"
						+ "<book category=\"PROGRAMMING\"><author>Scott Cranton</author><author>Jakub Korab</author><price>49.99</price><title lang=\"en\">Apache Camel Developer's Cookbook</title><year>2013</year></book>"
						+ "</bookstore>\r\n", response);
	}

}
