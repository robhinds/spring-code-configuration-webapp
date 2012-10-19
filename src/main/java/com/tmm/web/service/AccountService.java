package com.tmm.web.service;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tmm.web.security.Account;
import com.tmm.web.security.ApplicationUser;
import com.tmm.web.security.CompanyAccount;
import com.tmm.web.security.IndividualAccount;
import com.tmm.web.security.Role;
import com.tmm.web.security.dao.AccountDAO;
import com.tmm.web.security.dao.RoleDAO;

@Service("accountService")
public class AccountService {
	@Autowired
	private AccountDAO accountDAO;

	@Autowired
	private RoleDAO roleDAO;

	//24 hour timeout - push to config at some point
	private static long RESET_TIMEOUT_MS = 172800000;
	
	public void setRoleDAO(RoleDAO roleDAO) {
		this.roleDAO = roleDAO;
	}

	public AccountService() {
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List<Account> findAllAccounts() {
		return getAccountDAO().find();
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Account loadAccount(long id) {
		return getAccountDAO().read(Account.class, new Long(id));
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Account loadAccountByUserName(String userName) {
		return getAccountDAO().loadAccountByUserName(userName);
	}
	
	@Cacheable("dao.account")
	public Collection<IndividualAccount> searchAccounts(String userName) {
		return getAccountDAO().searchAccountsByUserName(userName);
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Account loadUserAccountByUserName(String userName) {
		return getAccountDAO().loadUserAccountByUserName(userName);
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Account loadUserAccountByEmail(String email) {
		return getAccountDAO().loadUserAccountByEmail(email);
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public CompanyAccount loadCompanyAccountByUserName(String userName) {
		return getAccountDAO().loadCompanyAccountByUserName(userName);
	}

	@Transactional
	public void removeAccount(long id) {
		getAccountDAO().delete(getAccountDAO().read(Account.class, new Long(id)));
	}

	@Transactional
	public Account storeAccount(Account account) {
		return getAccountDAO().merge(account);
	}

	public AccountDAO getAccountDAO() {
		return accountDAO;
	}

	public void setAccountDAO(AccountDAO accountDAO) {
		this.accountDAO = accountDAO;
	}

	public Account createNewBatchUser(String name, String email, String password) {
		return createNewBatchUser(name, email, password, null, null);
	}

//	@Transactional
//	public IndividualAccount createNewUser(String name, String email, String password, String firstName, String lastName, boolean importantNerdAbilityUpdatesEmail) throws NerdStarException {
//		if (loadUserAccountByEmail(email) == null) {
//			if (loadUserAccountByUserName(name) == null) {
//				IndividualAccount account = new IndividualAccount();
//				account.setUserName(name);
//				account.setEmail(email);
//				account.setFirstName(firstName);
//				account.setLastName(lastName);
//				Set<ConstraintViolation<IndividualAccount>> result = validator.validate(account);
//				if ((result.size() == 0)) {
//					Role r = loadOrCreateRole(Role.ROLE_USER);
//					account.addRole(r);
//					UserProfile userProf = new UserProfile();
//					userProf.setLinkedAccount(account);
//					//searchable and public by default.
//					userProf.setSearchable(true);
//					userProf.setIsPublic(true);
//					ContactPreferences contactPrefs = new ContactPreferences();
//					contactPrefs.setImportantNerdAbilityUpdatesEmail(importantNerdAbilityUpdatesEmail);
//					contactPrefs.setMonthlyBlogUpdatesEmail(false);
//					contactPrefs.setWeeklyBlogUpdatesEmail(false);
//					contactPrefs.setAccount(account);
//					account.setContactPreferences(contactPrefs);
//					account.setUserProfile(userProf);
//
//					UUID confirmationId = UUID.randomUUID();
//					account.setConfirmationId(confirmationId.toString());
//
//					getAccountDAO().persist(account);
//
//					// now update password once we have the account created (we
//					// need
//					// the
//					// ID to have been generated)
//					account.setAndEncodePassword(password);
//					// account = getAccountDAO().merge(account);
//
//					return account;
//				}
//				throw new NerdStarException(NerdStarCode.USER004_INVALIDUSERNAME, "Attempting to register user with invalid username");
//			}
//			throw new NerdStarException(NerdStarCode.USER002_DUPLICATEUSER, "Attempting to register user with existing username");
//		}
//		throw new NerdStarException(NerdStarCode.USER003_DUPLICATEEMAIL, "Attempting to register user with existing email");
//
//	}

//	@Transactional
//	public CompanyAccount createNewCompanyUser(String name, String email, String password, String companyName, String website, String industry) throws NerdStarException {
//		if (loadUserAccountByEmail(email) == null) {
//			if (loadUserAccountByUserName(name) == null) {
//				CompanyAccount account = new CompanyAccount();
//				account.setUserName(name);
//				account.setEmail(email);
//				account.setCompanyName(companyName);
//				Set<ConstraintViolation<CompanyAccount>> result = validator.validate(account);
//				if ((result.size() == 0)) {
//					Role r = loadOrCreateRole(Role.ROLE_USER);
//					account.addRole(r);
//					CompanyProfile userProf = new CompanyProfile();
//					userProf.setLinkedAccount(account);
//					account.setUserProfile(userProf);
//					userProf.setCompanyUrl(website);
//					userProf.setIndustry(industry);
//
//					UUID confirmationId = UUID.randomUUID();
//					account.setConfirmationId(confirmationId.toString());
//
//					getAccountDAO().persist(account);
//
//					// now update password once we have the account created (we
//					// need
//					// the
//					// ID to have been generated)
//					account.setAndEncodePassword(password);
//					// account = getAccountDAO().merge(account);
//
//					return account;
//				}
//				throw new NerdStarException(NerdStarCode.USER004_INVALIDUSERNAME, "Attempting to register user with invalid username");
//			}
//			throw new NerdStarException(NerdStarCode.USER002_DUPLICATEUSER, "Attempting to register user with existing username");
//		}
//		throw new NerdStarException(NerdStarCode.USER003_DUPLICATEEMAIL, "Attempting to register user with existing email");
//
//	}

	@Transactional
	public Role loadOrCreateRole(String authority) {
		Role r = roleDAO.loadRoleByAuthority(authority);
		if (r == null) {
			System.out.println("creating role " + authority);
			// create new role
			r = new Role();
			r.setRole(authority);
			roleDAO.persist(r);
		}
		return r;
	}

	@Transactional
	public Account createNewBatchUser(String name, String email, String password, String firstName, String lastName) {
		if (loadUserAccountByUserName(name) == null) {
			IndividualAccount account = new IndividualAccount();
			account.setUserName(name);
			account.setEmail(email);
			Role r = loadOrCreateRole(Role.ROLE_USER);
			account.addRole(r);
			account.setFirstName(firstName);
			account.setLastName(lastName);
			account.setConfirmed(true);

			getAccountDAO().persist(account);

			// now update password once we have the account created (we need the
			// ID to have been generated)
			account.setAndEncodePassword(password);
			// getAccountDAO().merge(account);

			return account;
		}

		return null;
	}

//	@Transactional
//	public Profile getNewestMember() {
//		List<IndividualAccount> allAccounts = getAccountDAO().loadAllIndividualUsers();
//		if (allAccounts.size() == 0) {
//			return null;
//		}
//
//		Profile p = allAccounts.get(0).getUserProfile();
//		return p;
//
//	}
	
	@Transactional
	public void startResetPassword(String input) throws UsernameNotFoundException{
		Account account = loadUserAccountByUserName(input);
		if (account == null) {
			account = loadUserAccountByEmail(input);
		}
		
		if(account != null){
			account.setResetPasswordId(UUID.randomUUID().toString());
			Date date = new Date();
			account.setResetPasswordExpiryTime(new Timestamp(date.getTime() + RESET_TIMEOUT_MS));
			account.setResetComplete(false);
		}else{
			throw new UsernameNotFoundException("The password reset has not been activated for the input:  " + input + "-  Aborting reset password");
		}
	}
	
	@Transactional
	public void completeResetPassword(String username, String newPassword) throws UsernameNotFoundException{
		Account account = loadUserAccountByUserName(username);
		if (account == null) {
			account = loadUserAccountByEmail(username);
		}
		
		if(account != null){
			account.setAndEncodePassword(newPassword);
			account.setResetComplete(true);
		}else{
			throw new UsernameNotFoundException("The password reset failed for the user:  " + username + "-  Aborting reset password");
		}
	}
	/**
	 * Method to retrieve the current user's account from a HTTPRequest
	 * 
	 * @param request
	 * @return
	 */
	public Account getAccount(HttpServletRequest request) {
		String userName = request.getRemoteUser();
		Account acc = loadAccountByUserName(userName);
		return acc;
	}

	/**
	 * Method to retrieve the current user profile from a HTTPRequest
	 * 
	 * @param request
	 * @return
	 */
//	public Profile getProfile(HttpServletRequest request) {
//		Account acc = getAccount(request);
//		if (acc != null) {
//			Profile person = acc.getUserProfile();
//			return person;
//		}
//		return null;
//	}

	@Transactional
	public void setCredentials() {
		Account account = loadUserAccountByUserName("admin");
		if (account == null) {
			account = createNewBatchUser("admin", "", "admin");
		}
		GrantedAuthority[] auths = new GrantedAuthority[1];
		auths[0] = new GrantedAuthorityImpl("ROLE_ADMIN");
		ApplicationUser user = new ApplicationUser(new Long(account.getId()), account.getUserName(), account.getPassword(), true, true, true, true, auths);
		Authentication auth = new TestingAuthenticationToken(user, "ignored", auths);
		auth.setAuthenticated(true);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	public void clearCredentials() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	@Transactional
	public void confirmAccount(long accId) {
		Account acc = loadAccount(accId);
		acc.setConfirmed(true);
	}
	
//	@Transactional
//	public void saveEmailPreferences(String contactName, Boolean importantNAEmailUpdates) throws NerdStarException {
//		Account a = loadAccountByUserName(contactName);
//		ContactPreferences contactPreference= a.getContactPreferences(); 
//		if(contactPreference == null){
//			contactPreference = new ContactPreferences(); 
//			a.setContactPreferences(contactPreference);
//		}
//		
//		 a.getContactPreferences().setImportantNerdAbilityUpdatesEmail(importantNAEmailUpdates);
//	}

//	@Transactional
//	public Map<String, Object> loadEmailOptions(String contactName)
//			throws NerdStarException {
//		Map<String, Object> model = new HashMap<String, Object>();
//		Account a = loadAccountByUserName(contactName);
//		try{
//			model.put("importantNAEmailUpdates", a.getContactPreferences().isImportantNerdAbilityUpdatesEmail());
//		}catch(Exception ex){
//			//this means that its an old account and for some reason wasn't updated with contact prefs
//			ContactPreferences contactPrefs = new ContactPreferences();
//			contactPrefs.setImportantNerdAbilityUpdatesEmail(false);
//			contactPrefs.setMonthlyBlogUpdatesEmail(false);
//			contactPrefs.setWeeklyBlogUpdatesEmail(false);
//			a.setContactPreferences(contactPrefs);
//			model.put("importantNAEmailUpdates", false);
//		}
//		return model;
//	}
}