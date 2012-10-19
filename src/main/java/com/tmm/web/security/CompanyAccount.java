package com.tmm.web.security;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.WordUtils;
import org.hibernate.search.annotations.Indexed;

@Entity
@DiscriminatorValue("Company")
@Indexed
public class CompanyAccount extends Account {

	private static final long serialVersionUID = 1L;

	private String companyName;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Override
	public boolean isIndividual() {
		return false;
	}

	@Override
	public String getDisplayName() {
		return WordUtils.capitalize(getCompanyName());
	}
}