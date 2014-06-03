package co.nz.camel.tutorial.transforming.normalizer;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import co.nz.camel.tutorial.transforming.csv.model.BookModel;
import co.nz.camel.tutorial.transforming.myschema.Book;
import co.nz.camel.tutorial.transforming.myschema.Bookstore;

public class MyNormalizer {
	public Bookstore bookModelToJaxb(List<Map<String, Object>> books) {
		final Bookstore bookstore = new Bookstore();

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");

		for (Map<String, Object> bookEntry : books) {
			final Book book = new Book();
			final Book.Title title = new Book.Title();

			final BookModel bookModel = (BookModel) bookEntry
					.get(BookModel.class.getCanonicalName());

			book.setCategory(bookModel.getCategory());

			title.setLang(bookModel.getTitleLanguage());
			title.setValue(bookModel.getTitle());
			book.setTitle(title);

			book.getAuthor().add(bookModel.getAuthor1());

			final String author2 = bookModel.getAuthor2();
			if ((author2 != null) && !author2.isEmpty()) {
				book.getAuthor().add(author2);
			}

			book.setYear(Integer.parseInt(simpleDateFormat.format(bookModel
					.getPublishDate())));
			book.setPrice(bookModel.getPrice().doubleValue());

			bookstore.getBook().add(book);
		}

		return bookstore;
	}
}