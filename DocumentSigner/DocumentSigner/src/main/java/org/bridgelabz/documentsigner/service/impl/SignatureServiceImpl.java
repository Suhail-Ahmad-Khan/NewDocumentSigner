// This class defines and implements the functionalities declared in the 
// service class of the Signature.

package org.bridgelabz.documentsigner.service.impl;

import java.io.InputStream;
import java.sql.Blob;
import java.util.Date;
import java.util.List;

import org.bridgelabz.documentsigner.model.Signature;
import org.bridgelabz.documentsigner.service.SignatureService;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SignatureServiceImpl implements SignatureService {

	@Autowired
	private SessionFactory sessionFactory;

	public void addSignature(Signature signature, InputStream inputStream) {
		Session session = sessionFactory.getCurrentSession();
		try {
			signature.setCreatedDate(new Date());
			Blob blob = Hibernate.getLobCreator(session).createBlob(inputStream, inputStream.available());
			signature.setContent(blob);
			session.save(signature);
			System.out.println("======== ADD Signature SAVE========= end===");
		} catch (Exception e) {
			System.out.println("======== ADD Signature SAVE========= error===");
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Signature> listSignatures(int userId) {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("deprecation")
		List<Signature> mySignatures = (List<Signature>) session
				.createCriteria(Signature.class).setProjection(Projections.projectionList()
						.add(Projections.property("id")).add(Projections.property("imageName")))
				.add(Restrictions.eq("userId", userId)).list();
		return mySignatures;
	}

	@SuppressWarnings("unchecked")
	public List<Signature> listSignatureDetails(int id) {
		Session session = sessionFactory.getCurrentSession();
		Query<Signature> query = session.createQuery("from Signature where id=:id");
		query.setParameter("id", id);
		List<Signature> signatureDetails = query.getResultList();
		return signatureDetails;
	}

	public Signature getSignatureContent(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("deprecation")
		Signature mySignature = (Signature) session.createCriteria(Signature.class).add(Restrictions.idEq(id))
				.uniqueResult();
		return mySignature;
	}

}
