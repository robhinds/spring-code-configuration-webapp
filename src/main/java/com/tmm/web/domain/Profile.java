/**
 * 
 */
package com.tmm.web.domain;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonProperty;


/**
 * Class responsible for persisting user profile details, including all the
 * connection details to thrid party clients
 * 
 * @author robert.hinds
 * 
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "CV_PROFILE")
public abstract class Profile extends PersistableObject {

	private static final long serialVersionUID = 3458607287170514439L;

//	@OneToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "LINKED_ACCOUNT")
//	private Account linkedAccount;

	private String location;

	private boolean searchable;

	private boolean isPublic;


	public boolean isSearchable() {
		return searchable;
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	@JsonProperty
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Profile other = (Profile) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	

}
