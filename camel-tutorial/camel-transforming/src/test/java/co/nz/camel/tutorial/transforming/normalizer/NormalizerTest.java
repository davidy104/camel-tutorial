package co.nz.camel.tutorial.transforming.normalizer;

import java.io.InputStream;

import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.transforming.config.ApplicationConfiguration;
import co.nz.camel.tutorial.transforming.myschema.Book;
import co.nz.camel.tutorial.transforming.myschema.Bookstore;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class NormalizerTest {

	@EndpointInject(uri = "mock:normalizerUnknown")
	private MockEndpoint mockUnknown;

	@EndpointInject(uri = "mock:normalizerCsv")
	private MockEndpoint mockCsv;

	@EndpointInject(uri = "mock:normalizerJson")
	private MockEndpoint mockJson;

	@EndpointInject(uri = "mock:normalizerXml")
	private MockEndpoint mockXml;

	@EndpointInject(uri = "mock:normalizerNormalized")
	private MockEndpoint mockNormalized;

	@Produce(uri = "direct:normalizerStart")
	private ProducerTemplate template;

	@Resource
	private CamelContext context;

	@Test
	public void testNormalizeXml() throws Exception {
		final InputStream resource = getClass().getClassLoader()
				.getResourceAsStream("bookstore.xml");
		final String request = context.getTypeConverter().convertTo(
				String.class, resource);

		mockUnknown.setExpectedMessageCount(0);
		mockCsv.setExpectedMessageCount(0);
		mockJson.setExpectedMessageCount(0);

		mockXml.expectedBodiesReceived(getExpectedBookstore());
		mockNormalized.expectedBodiesReceived(getExpectedBookstore());

		template.sendBodyAndHeader(request, Exchange.FILE_NAME, "bookstore.xml");

		mockUnknown.assertIsSatisfied();
		mockCsv.assertIsSatisfied();
		mockJson.assertIsSatisfied();
		mockXml.assertIsSatisfied();
		mockNormalized.assertIsSatisfied();
	}

	@Test
	public void testNormalizeJson() throws Exception {
		final InputStream resource = getClass().getClassLoader()
				.getResourceAsStream("bookstore.json");
		final String request = context.getTypeConverter().convertTo(
				String.class, resource);

		mockUnknown.setExpectedMessageCount(0);
		mockCsv.setExpectedMessageCount(0);
		mockXml.setExpectedMessageCount(0);

		mockJson.expectedBodiesReceived(getExpectedBookstore());
		mockNormalized.expectedBodiesReceived(getExpectedBookstore());

		template.sendBodyAndHeader(request, Exchange.FILE_NAME,
				"bookstore.json");

		mockUnknown.assertIsSatisfied();
		mockCsv.assertIsSatisfied();
		mockJson.assertIsSatisfied();
		mockXml.assertIsSatisfied();
		mockNormalized.assertIsSatisfied();
	}

	@Test
	public void testNormalizeCsv() throws Exception {
		final InputStream resource = getClass().getClassLoader()
				.getResourceAsStream("bookstore.csv");
		final String request = context.getTypeConverter().convertTo(
				String.class, resource);

		mockUnknown.setExpectedMessageCount(0);
		mockJson.setExpectedMessageCount(0);
		mockXml.setExpectedMessageCount(0);

		mockCsv.expectedBodiesReceived(getExpectedBookstore());
		mockNormalized.expectedBodiesReceived(getExpectedBookstore());

		template.sendBodyAndHeader(request, Exchange.FILE_NAME, "bookstore.csv");

		mockUnknown.assertIsSatisfied();
		mockCsv.assertIsSatisfied();
		mockJson.assertIsSatisfied();
		mockXml.assertIsSatisfied();
		mockNormalized.assertIsSatisfied();
	}

	@Test
	public void testNormalizeUnknown() throws Exception {
		mockCsv.setExpectedMessageCount(0);
		mockJson.setExpectedMessageCount(0);
		mockXml.setExpectedMessageCount(0);
		mockNormalized.setExpectedMessageCount(0);

		mockUnknown.expectedBodiesReceived("Unknown Data");
		mockUnknown.expectedHeaderReceived(Exchange.FILE_NAME,
				"bookstore.unknown");

		template.sendBodyAndHeader("Unknown Data", Exchange.FILE_NAME,
				"bookstore.unknown");

		mockUnknown.assertIsSatisfied();
		mockCsv.assertIsSatisfied();
		mockJson.assertIsSatisfied();
		mockXml.assertIsSatisfied();
		mockNormalized.assertIsSatisfied();
	}

	protected Bookstore getExpectedBookstore() {
		final Bookstore bookstore = new Bookstore();

		Book book = new Book();

		book.setCategory("COOKING");

		Book.Title title = new Book.Title();
		title.setValue("Everyday Italian");
		title.setLang("en");

		book.setTitle(title);
		book.getAuthor().add("Giada De Laurentiis");
		book.setYear(2005);
		book.setPrice(30.00);

		bookstore.getBook().add(book);

		book = new Book();

		book.setCategory("CHILDREN");

		title = new Book.Title();
		title.setValue("Harry Potter");
		title.setLang("en");

		book.setTitle(title);
		book.getAuthor().add("J K. Rowling");
		book.setYear(2005);
		book.setPrice(29.99);

		bookstore.getBook().add(book);

		book = new Book();

		book.setCategory("WEB");

		title = new Book.Title();
		title.setValue("Learning XML");
		title.setLang("en");

		book.setTitle(title);
		book.getAuthor().add("Erik T. Ray");
		book.setYear(2003);
		book.setPrice(39.95);

		bookstore.getBook().add(book);

		book = new Book();

		book.setCategory("PROGRAMMING");

		title = new Book.Title();
		title.setValue("Apache Camel Developer's Cookbook");
		title.setLang("en");

		book.setTitle(title);
		book.getAuthor().add("Scott Cranton");
		book.getAuthor().add("Jakub Korab");
		book.setYear(2013);
		book.setPrice(49.99);

		bookstore.getBook().add(book);

		return bookstore;
	}

}
