// This class defines and implements the functionalities declared in the 
// service interface of the User.

package org.bridgelabz.documentsigner.service.impl;

import org.bridgelabz.documentsigner.model.User;
import org.bridgelabz.documentsigner.service.UserService;
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
public class UserServiceImpl implements UserService {

	@Autowired
	private SessionFactory sessionFactory;

	public void addUser(User user) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.save(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public User authenticateUser(String email, String password) {
		Session session = sessionFactory.getCurrentSession();
		try {
			Criteria cr = session.createCriteria(User.class);

			User user = (User) cr.add(Restrictions.conjunction().add(Restrictions.eq("email", email))
					.add(Restrictions.eq("password", password))).uniqueResult();
			return user;
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return null;
	}
}
