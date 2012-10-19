package com.tmm.web.security;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tmm.web.service.AccountService;

/**
 * Custom implementation to support Spring Security logon based on custom
 * authentication of persisted databases
 * 
 * @author robert.hinds
 * 
 */
@Service("userService")
public class UserDetailsServiceImpl implements UserDetailsService, InitializingBean {

	@Autowired
	private AccountService accountService;

	public void afterPropertiesSet() throws Exception {}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {
		userName = userName.toLowerCase();
		try {
			// try and load the username entered from the db
			Account account = accountService.loadAccountByUserName(userName);

			// check that the username exists
			if (account == null) {
				throw new UsernameNotFoundException("Could not find username: " + userName + "in the DB.");
			}
			if (!account.isConfirmed()) {
				throw new IllegalStateException("User not yet confirmed email" + userName + " login failed");
			}

			// recurse through set of roles
			GrantedAuthority[] auths = null;
			try {
				auths = new GrantedAuthority[account.getRoles().size()];
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			int count = 0;
			for (Role r : account.getRoles()) {
				auths[count++] = new GrantedAuthorityImpl(r.getRole());
			}

			// create application user
			ApplicationUser user = null;
			try {
				user = new ApplicationUser(new Long(account.getId()), userName, account.getPassword(), true, true, true, true, auths);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			throw new UsernameNotFoundException(userName + "not found", e);
		}
	}

}
