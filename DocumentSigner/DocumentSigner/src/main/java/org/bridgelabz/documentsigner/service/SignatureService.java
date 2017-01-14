package org.bridgelabz.documentsigner.service;

import java.io.InputStream;
import java.util.List;

import org.bridgelabz.documentsigner.model.Signature;
import org.springframework.stereotype.Repository;

@Repository
public interface SignatureService {

	public void addSignature(Signature signature, InputStream inputStream);

	public List<Signature> listSignatures(int userId);

	public List<Signature> listSignatureDetails(int id);

	public Signature getSignatureContent(Integer id);

}