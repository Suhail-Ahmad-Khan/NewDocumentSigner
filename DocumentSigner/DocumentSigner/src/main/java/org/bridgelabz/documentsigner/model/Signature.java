package org.bridgelabz.documentsigner.model;

import java.io.Serializable;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.util.Date;

import javax.crypto.KeyGenerator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "signature")
public class Signature implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;

	@Column(name = "userId")
	private int userId;

	@Column(name = "description")
	private String description;

	@Column(name = "imageName")
	private String imageName;

	@Column(name = "content")
	@Lob
	private Blob content;

	@Column(name = "contentType")
	private String contentType;

	@Column(name = "created")
	private Date createdDate;

	private Key signatureKey;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Blob getContent() {
		return content;
	}

	public void setContent(Blob content) {
		this.content = content;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Key getSignatureKey() {
		KeyGenerator keyGenerator = null;
		try {
			keyGenerator = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		keyGenerator.init(128);
		signatureKey = keyGenerator.generateKey();
		return signatureKey;
	}

	public void setSignatureKey(Key signatureKey) {
		this.signatureKey = signatureKey;
	}
}
