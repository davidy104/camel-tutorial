package co.nz.camel.tutorial.transforming.csv;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.util.CastUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import co.nz.camel.tutorial.transforming.config.ApplicationConfiguration;
import co.nz.camel.tutorial.transforming.csv.model.BookModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class CsvTest {
	@Produce
	private ProducerTemplate template;

	private static final Logger LOGGER = LoggerFactory.getLogger(CsvTest.class);

	@Test
	public void testCsvMarshal() throws Exception {
		ArrayList<BookModel> books = new ArrayList<BookModel>();

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"MMM-yyyy");

		BookModel book = new BookModel();
		book.setCategory("PROGRAMMING");
		book.setTitle("Camel in Action");
		book.setTitleLanguage("en");
		book.setAuthor1("Claus Ibsen");
		book.setAuthor2("Jon Anstey");
		book.setPublishDate(simpleDateFormat.parse("Dec-2010"));
		book.setPrice(BigDecimal.valueOf(49.99));

		books.add(book);

		book = new BookModel();
		book.setCategory("PROGRAMMING");
		book.setTitle("Apache Camel Developer's Cookbook");
		book.setTitleLanguage("en");
		book.setAuthor1("Scott Cranton");
		book.setAuthor2("Jakub Korab");
		book.setPublishDate(simpleDateFormat.parse("Dec-2013"));
		book.setPrice(BigDecimal.valueOf(49.99));

		books.add(book);

		final String response = template.requestBody("direct:csvMarshal",
				books, String.class);

		LOGGER.info(response);
		final String expects = "PROGRAMMING,Camel in Action,en,Claus Ibsen,Jon Anstey,Dec-2010,49.99\n"
				+ "PROGRAMMING,Apache Camel Developer's Cookbook,en,Scott Cranton,Jakub Korab,Dec-2013,49.99\n";
		assertEquals(expects, response);
	}

	@Test
	public void testCsvUnmarshal() throws Exception {
		final String request = "PROGRAMMING,Camel in Action,en,Claus Ibsen,Jon Anstey,Dec-2010,49.99\n"
				+ "PROGRAMMING,Apache Camel Developer's Cookbook,en,Scott Cranton,Jakub Korab,Dec-2013,49.99\n";

		final List<Map<String, BookModel>> response = CastUtils.cast(template
				.requestBody("direct:csvUnmarshal", request, List.class));

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"MMM-yyyy");

		BookModel book1 = new BookModel();
		book1.setCategory("PROGRAMMING");
		book1.setTitle("Camel in Action");
		book1.setTitleLanguage("en");
		book1.setAuthor1("Claus Ibsen");
		book1.setAuthor2("Jon Anstey");
		book1.setPublishDate(simpleDateFormat.parse("Dec-2010"));
		book1.setPrice(BigDecimal.valueOf(49.99));

		BookModel book2 = new BookModel();
		book2.setCategory("PROGRAMMING");
		book2.setTitle("Apache Camel Developer's Cookbook");
		book2.setTitleLanguage("en");
		book2.setAuthor1("Scott Cranton");
		book2.setAuthor2("Jakub Korab");
		book2.setPublishDate(simpleDateFormat.parse("Dec-2013"));
		book2.setPrice(BigDecimal.valueOf(49.99));

		Map<String, BookModel> response1 = response.get(0);
		assertEquals(1, response1.size());

		// Map of <class name String>, <Model object value>
		Map.Entry<String, BookModel> entry1 = response1.entrySet().iterator()
				.next();
		assertEquals(BookModel.class.getName(), entry1.getKey());
		assertEquals(book1, entry1.getValue());

		Map<String, BookModel> response2 = response.get(1);
		assertEquals(1, response1.size());
		Map.Entry<String, BookModel> entry2 = response2.entrySet().iterator()
				.next();
		assertEquals(BookModel.class.getName(), entry2.getKey());
		assertEquals(book2, entry2.getValue());
	}

}
