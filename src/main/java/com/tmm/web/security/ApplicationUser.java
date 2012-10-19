package com.tmm.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;



/**
 * Implementation of Spring {@link User} for Spring Security authentication
 * 
 * @author robert.hinds
 *
 */
public class ApplicationUser extends User{
	public static final long serialVersionUID = 42L;

	private final Long accountId;
	
	@SuppressWarnings("deprecation")
	public ApplicationUser(Long accountId, String userName, String password, boolean arg2, boolean arg3,
			boolean arg4, boolean arg5, GrantedAuthority[] auths)
			throws IllegalArgumentException{
		super(userName, password, arg2, arg3, arg4, arg5, auths);
		this.accountId = accountId;
	}

	public Long getAccountId(){
		return this.accountId;
	}
}
