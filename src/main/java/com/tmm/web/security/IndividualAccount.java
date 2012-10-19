package com.tmm.web.security;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.WordUtils;
import org.hibernate.search.annotations.Indexed;

@Entity
@DiscriminatorValue("User")
@Indexed
public class IndividualAccount extends Account {

	private static final long serialVersionUID = 1L;

	private String firstName;

	private String lastName;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public boolean isIndividual() {
		return true;
	}

	@Override
	public String getDisplayName() {
		return WordUtils.capitalize(getFirstName()) + " " + WordUtils.capitalize(getLastName());
	}
}