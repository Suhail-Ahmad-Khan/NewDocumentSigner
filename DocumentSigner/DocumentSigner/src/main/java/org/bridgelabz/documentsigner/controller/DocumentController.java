// This is the controller section of handling the documents.
// The methods defined here adds the documents in the database,
// lists the documents stored by the user in the database and downloads the
// document on user's insistence.
// The process of encryption during upload and decryption during download is done here.

package org.bridgelabz.documentsigner.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.crypto.SecretKey;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.bridgelabz.documentsigner.json.ErrorResponse;
import org.bridgelabz.documentsigner.json.Response;
import org.bridgelabz.documentsigner.json.SuccessResponse;
import org.bridgelabz.documentsigner.keygenerator.KeyGenerator;
import org.bridgelabz.documentsigner.model.Document;
import org.bridgelabz.documentsigner.model.User;
import org.bridgelabz.documentsigner.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class DocumentController {

	@Autowired
	private DocumentService documentService;

	// Opens up the JSP page for filling up the data
	@RequestMapping(value = "/addDocuments", method = RequestMethod.GET)
	public String getData(Model model) {
		Document document = new Document();
		model.addAttribute("document", document);
		return "addDocuments";
	}

	// Takes the User to the Success page where the various functionalities
	// are present.
	@RequestMapping(value = "/document", method = RequestMethod.GET)
	public String documentPage() {
		return "success";
	}

	// Data filled on the JSP page is saved in the database
	@RequestMapping(value = "/addDocuments", method = RequestMethod.POST)
	public @ResponseBody Response addDocument(@ModelAttribute("document") Document document, BindingResult result,
			MultipartFile file, HttpServletRequest request) {
		HttpSession httpSession = request.getSession();
		User user = (User) httpSession.getAttribute("user");
		document.setUserId(user.getId());
		InputStream io = null;
		try {
			document.setFilename(file.getOriginalFilename());
			document.setContentType(file.getContentType());
			io = file.getInputStream();

			// Encrypts the data before data upload
			KeyGenerator keygen = new KeyGenerator();
			SecretKey documentKey = keygen.generateKey();
			byte[] content = IOUtils.toByteArray(io);
			byte[] encryptData = keygen.encryptFile(documentKey, content);
			io = new ByteArrayInputStream(encryptData);

			documentService.addDocument(document, io);
			io.close();
			SuccessResponse sr = new SuccessResponse();
			sr.setMessage("Document added successfully!");
			sr.setStatus(1);
			return sr;

		} catch (IOException e) {
			e.printStackTrace();
			ErrorResponse er = new ErrorResponse();
			er.setErrorMessage("Error while saving the file" + e.getMessage());
			er.setDisplayMessage("Failed to save file. Please try again");
			er.setStatus(-1);
			return er;
		}
	}

	// Lists the various documents uploaded by the User
	@RequestMapping(value = "/documentList", method = RequestMethod.GET)
	public @ResponseBody List<Document> listAllDocuments(HttpServletRequest request) {
		HttpSession httpSession = request.getSession();
		User user = (User) httpSession.getAttribute("user");
		List<Document> documentInfo = documentService.listDocuments(user.getId());
		System.out.println(documentInfo.size());
		return documentInfo;
	}

	// Enables the User to download the document based on its id.
	@RequestMapping(value = "/downloadDocument", method = RequestMethod.GET)
	public @ResponseBody Response displayDocumentDetails(@RequestParam("id") Integer id, HttpServletRequest request,
			HttpServletResponse response) {

		Document document = documentService.getDocumentContent(id);
		try {
			InputStream io = document.getContent().getBinaryStream();
			if (io == null) {
				ErrorResponse errorresponse = new ErrorResponse();
				errorresponse.setStatus(-1);
				errorresponse.setDisplayMessage("Document not found");
				return errorresponse;
			}

			// Decrypts the data coming from the database
			KeyGenerator keygen = new KeyGenerator();
			SecretKey documentKey = keygen.generateKey();
			byte[] encryptedContent = IOUtils.toByteArray(io);
			byte[] decryptData = keygen.decryptFile(documentKey, encryptedContent);
			io = new ByteArrayInputStream(decryptData);

			byte[] buff = new byte[io.available()];
			response.setContentType(document.getContentType());
			response.setHeader("Content-Disposition", "attachment; filename=" + document.getFilename());
			ServletOutputStream outputStream = response.getOutputStream();
			while (io.available() > 0) {
				int n = io.read(buff);
				outputStream.write(buff, 0, n);
			}
		} catch (Exception e) {

		}
		return null;

	}
}