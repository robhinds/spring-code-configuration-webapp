/**
 * 
 */
package com.tmm.web.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Class responsible for persisting user profile details
 * 
 * @author robert.hinds
 * 
 */
@Entity
@DiscriminatorValue("User")
public class UserProfile extends Profile {


}
