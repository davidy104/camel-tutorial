package co.nz.camel.tutorial.routing.model;

import java.io.Serializable;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Simple POJO that allows for leaky state between threads.
 */
@SuppressWarnings("serial")
public class Cheese
// implements Cloneable
		implements Serializable {
	private int age;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Cheese clone() {
		Cheese cheese = new Cheese();
		cheese.setAge(this.getAge());
		return SerializationUtils.clone(cheese);
	}

	// @Override
	// public Cheese clone() {
	// Cheese cheese = new Cheese();
	// cheese.setAge(this.getAge());
	// return cheese;
	// }

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.age, ((Cheese) obj).age).isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.age).toHashCode();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
