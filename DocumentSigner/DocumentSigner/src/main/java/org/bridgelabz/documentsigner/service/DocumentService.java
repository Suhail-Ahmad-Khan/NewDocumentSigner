// This is an interface which declares the methods 
// and provides the services to the Document

package org.bridgelabz.documentsigner.service;

import java.io.InputStream;
import java.util.List;

import org.bridgelabz.documentsigner.model.Document;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentService {

	public void addDocument(Document document, InputStream inputStream);

	public List<Document> listDocuments(int userId);

	public List<Document> listDocumentDetails(int id);

	public Document getDocumentContent(int id);

}
