package co.nz.camel.tutorial.transforming.jaxb;

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
import co.nz.camel.tutorial.transforming.myschema.Book;
import co.nz.camel.tutorial.transforming.myschema.Bookstore;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JaxbTest {
	@Produce
	private ProducerTemplate template;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(JaxbTest.class);

	@Test
	public void testJaxbMarshal() throws Exception {
		Bookstore bookstore = new Bookstore();
		Book book = new Book();
		Book.Title title = new Book.Title();
		title.setValue("Apache Camel Developer's Cookbook");
		book.setTitle(title);
		book.setYear(2013);
		book.setPrice(39.99);
		book.getAuthor().add("Scott Cranton");
		book.getAuthor().add("Jakub Korab");

		bookstore.getBook().add(book);

		String response = template.requestBody("direct:jaxbMarshal", bookstore,
				String.class);

		LOGGER.info(response);
		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
						+ "<bookstore>\n"
						+ "    <book>\n"
						+ "        <title>Apache Camel Developer's Cookbook</title>\n"
						+ "        <author>Scott Cranton</author>\n"
						+ "        <author>Jakub Korab</author>\n"
						+ "        <year>2013</year>\n"
						+ "        <price>39.99</price>\n" + "    </book>\n"
						+ "</bookstore>\n", response);
	}

	@Test
	public void testJaxbUnmarshal() throws Exception {
		final String request = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
				+ "<bookstore>\n"
				+ "    <book>\n"
				+ "        <title>Apache Camel Developer's Cookbook</title>\n"
				+ "        <author>Scott Cranton</author>\n"
				+ "        <author>Jakub Korab</author>\n"
				+ "        <year>2013</year>\n"
				+ "        <price>39.99</price>\n"
				+ "    </book>\n"
				+ "</bookstore>\n";

		Bookstore response = template.requestBody("direct:jaxbUnmarshal",
				request, Bookstore.class);

		Bookstore bookstore = new Bookstore();

		Book book = new Book();

		Book.Title title = new Book.Title();
		title.setValue("Apache Camel Developer's Cookbook");

		book.setTitle(title);
		book.setYear(2013);
		book.setPrice(39.99);
		book.getAuthor().add("Scott Cranton");
		book.getAuthor().add("Jakub Korab");

		bookstore.getBook().add(book);

		assertEquals(bookstore, response);
	}

}
