package co.nz.camel.tutorial.transforming.csv.model;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@CsvRecord(separator = ",", crlf = "UNIX")
public class BookModel {

	@DataField(pos = 1)
	private String category;

	@DataField(pos = 2)
	private String title;

	@DataField(pos = 3, defaultValue = "en")
	private String titleLanguage;

	@DataField(pos = 4)
	private String author1;

	@DataField(pos = 5)
	private String author2;

	@DataField(pos = 6, pattern = "MMM-yyyy")
	private Date publishDate;

	@DataField(pos = 7, precision = 2)
	private BigDecimal price;

	public String getAuthor1() {
		return author1;
	}

	public void setAuthor1(String author1) {
		this.author1 = author1;
	}

	public String getAuthor2() {
		return author2;
	}

	public void setAuthor2(String author2) {
		this.author2 = author2;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleLanguage() {
		return titleLanguage;
	}

	public void setTitleLanguage(String titleLanguage) {
		this.titleLanguage = titleLanguage;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.author1, ((BookModel) obj).author1)
				.append(this.author2, ((BookModel) obj).author2)
				.append(this.category, ((BookModel) obj).category)
				.append(this.price, ((BookModel) obj).price)
				.append(this.publishDate, ((BookModel) obj).publishDate)
				.append(this.title, ((BookModel) obj).title)
				.append(this.titleLanguage, ((BookModel) obj).titleLanguage)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.author1).append(this.author2)
				.append(this.category).append(this.price)
				.append(this.publishDate).append(this.title)
				.append(this.titleLanguage).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("category", category).append("author1", author1)
				.append("author2", author2).append("title", title)
				.append("titleLanguage", titleLanguage)
				.append("publishDate", publishDate).append("price", price)
				.toString();
	}

}
