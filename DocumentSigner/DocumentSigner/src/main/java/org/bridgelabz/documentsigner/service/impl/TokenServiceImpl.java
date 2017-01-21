// This class defines and implements the functionalities declared in the 
// service interface of the Token.

package org.bridgelabz.documentsigner.service.impl;

import org.bridgelabz.documentsigner.model.Token;
import org.bridgelabz.documentsigner.service.TokenService;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class TokenServiceImpl implements TokenService {

	@Autowired
	private SessionFactory sessionFactory;

	public Token addToken(Token token) {
		Session session = sessionFactory.getCurrentSession();

		try {
			session.save(token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}

	@SuppressWarnings("deprecation")
	public Token getToken(String accToken) {
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria cr = session.createCriteria(Token.class);
			Token token = (Token) cr.add(Restrictions.conjunction().add(Restrictions.eq("accessToken", accToken)))
					.uniqueResult();
			return token;
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public Token getTokenByRefToken(String refreshToken) {
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria cr = session.createCriteria(Token.class);
			Token token = (Token) cr.add(Restrictions.conjunction().add(Restrictions.eq("refreshToken", refreshToken)))
					.uniqueResult();
			return token;
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return null;
	}
}
