// This class defines and implements the functionalities declared in the 
// service class of the Document.

package org.bridgelabz.documentsigner.service.impl;

import java.io.InputStream;
import java.sql.Blob;
import java.util.Date;
import java.util.List;

import org.bridgelabz.documentsigner.model.Document;
import org.bridgelabz.documentsigner.service.DocumentService;
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
public class DocumentServiceImpl implements DocumentService {

	@Autowired
	private SessionFactory sessionFactory;

	public void addDocument(Document document, InputStream inputStream) {
		Session session = sessionFactory.getCurrentSession();
		try {
			document.setCreatedDate(new Date());
			Blob blob = Hibernate.getLobCreator(session).createBlob(inputStream, inputStream.available());
			document.setContent(blob);
			session.save(document);
			System.out.println("======== ADD Document SAVE========= end===");
		} catch (Exception e) {
			System.out.println("======== ADD Document SAVE========= error===");
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Document> listDocuments(int userId) {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("deprecation")
		List<Document> myDocuments = (List<Document>) session
				.createCriteria(Document.class).setProjection(Projections.projectionList()
						.add(Projections.property("id")).add(Projections.property("filename")))
				.add(Restrictions.eq("userId", userId)).list();
		return myDocuments;
	}

	@SuppressWarnings("unchecked")
	public List<Document> listDocumentDetails(int id) {
		Session session = sessionFactory.getCurrentSession();
		Query<Document> query = session.createQuery("from Document where id=:id");
		query.setParameter("id", id);
		List<Document> documentDetails = query.getResultList();
		return documentDetails;
	}

	public Document getDocumentContent(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		@SuppressWarnings("deprecation")
		Document myDocument = (Document) session.createCriteria(Document.class).add(Restrictions.idEq(id))
				.uniqueResult();
		return myDocument;
	}

}