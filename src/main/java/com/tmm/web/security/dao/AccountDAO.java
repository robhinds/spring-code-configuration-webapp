/**
 * 
 */
package com.tmm.web.security.dao;

import static org.hibernate.search.jpa.Search.getFullTextEntityManager;

import java.util.Collection;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tmm.web.core.dao.GenericHibernateDAO;
import com.tmm.web.security.Account;
import com.tmm.web.security.CompanyAccount;
import com.tmm.web.security.IndividualAccount;

/**
 * @author robert.hinds
 * 
 */
@Repository(value = "accountDAO")
public class AccountDAO extends GenericHibernateDAO<Account, Long> {

	@Transactional
	public Account loadUserAccountByUserName(String userName) {
		Query query = getEntityManager().createQuery("select u from Account u where u.userName = ?1");
		query.setParameter(1, userName);
		IndividualAccount user = null;

		try {
			List<Account> users = (List<Account>) query.getResultList();
			if (users == null || users.isEmpty()) {
				return null;
			} else {
				return users.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return user;
	}

	@Transactional
	public Collection<IndividualAccount> searchAccountsByUserName(String searchString){
		FullTextEntityManager em = getFullTextEntityManager(getEntityManager());
		QueryBuilder qb = em.getSearchFactory()
			    .buildQueryBuilder().forEntity( IndividualAccount.class ).get();
			org.apache.lucene.search.Query query = qb
			  .keyword()
			  .onFields("userName")
			  .matching(searchString)
			  .createQuery();

			// wrap Lucene query in a javax.persistence.Query
			javax.persistence.Query persistenceQuery = 
			    em.createFullTextQuery(query, IndividualAccount.class);

			// execute search
			return persistenceQuery.getResultList();
	}
	
	@Transactional
	public Account loadUserAccountByEmail(String email) {
		Query query = getEntityManager().createQuery("select u from Account u where u.email = ?1");
		query.setParameter(1, email);
		Account user = null;

		try {
			List<Account> users = (List<Account>) query.getResultList();
			if (users == null || users.isEmpty() || users.get(0).getUserName().equalsIgnoreCase("admin")) {
				return null;
			} else {
				return users.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return user;
	}

	@Transactional
	public CompanyAccount loadCompanyAccountByUserName(String userName) {
		Query query = getEntityManager().createQuery("select u from CompanyAccount u where u.userName = ?1");
		query.setParameter(1, userName);
		CompanyAccount user = null;

		try {
			List<CompanyAccount> users = (List<CompanyAccount>) query.getResultList();
			if (users == null || users.isEmpty()) {
				return null;
			} else {
				return users.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return user;
	}

	@Transactional
	public Account loadAccountByUserName(String userName) {
		Query query = getEntityManager().createQuery("select u from Account u where u.userName = ?1");
		query.setParameter(1, userName);
		Account user = null;

		try {
			List<Account> users = (List<Account>) query.getResultList();
			if (users == null || users.isEmpty()) {
				return null;
			} else {
				return users.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return user;
	}

	@SuppressWarnings("unchecked")
	public List<IndividualAccount> loadAllIndividualUsers() {
		Query query = getEntityManager().createQuery("select r from Account r order by r.id DESC");
		return (List<IndividualAccount>) query.getResultList();
	}

}
