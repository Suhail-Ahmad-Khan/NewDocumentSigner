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

	@SuppressWarnings("deprecation")
	public Token authToken(String accessToken, String refreshToken) {
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria cr = session.createCriteria(Token.class);
			Token token = (Token) cr.add(Restrictions.conjunction().add(Restrictions.eq("accessToken", accessToken))
					.add(Restrictions.eq("refreshToken", refreshToken))).uniqueResult();
			return token;
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Token addToken(Token token) {
		Session session = sessionFactory.getCurrentSession();

		try {
			session.save(token);
			// tr.commit();
		} catch (Exception e) {
			// tr.rollback();
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

	/*
	 * public Token generateToken(User user, Token token) {
	 * 
	 * String accessToken = UUID.randomUUID().toString().replaceAll("-", "");
	 * String refreshToken = UUID.randomUUID().toString().replaceAll("-", "");
	 * token.setAccessToken(accessToken); token.setRefreshToken(refreshToken);
	 * token.setUserId(user.getId()); return token; }
	 */

}
