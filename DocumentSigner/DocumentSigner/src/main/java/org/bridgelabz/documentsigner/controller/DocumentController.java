package org.bridgelabz.documentsigner.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bridgelabz.documentsigner.json.ErrorResponse;
import org.bridgelabz.documentsigner.json.Response;
import org.bridgelabz.documentsigner.json.SuccessResponse;
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

	@RequestMapping(value = "/addDocuments", method = RequestMethod.GET)
	public String getData(Model model) {
		Document document = new Document();
		model.addAttribute("document", document);
		return "addDocuments";
	}

	@RequestMapping(value = "/document", method = RequestMethod.GET)
	public String documentPage() {
		return "success";
	}

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

	@RequestMapping(value = "/documentList", method = RequestMethod.GET)
	public @ResponseBody List<Document> listAllDocuments(HttpServletRequest request) {
		HttpSession httpSession = request.getSession();
		User user = (User) httpSession.getAttribute("user");
		List<Document> documentInfo = documentService.listDocuments(user.getId());
		System.out.println(documentInfo.size());
		return documentInfo;
	}

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
			byte[] buff = new byte[10000];
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

/*
 * FileOutputStream fos = new
 * FileOutputStream("/home/bridgeit/"+file.getOriginalFilename()); byte [] buff
 * = new byte[8000]; while( io.available() > 0 ) { int n = io.read(buff);
 * fos.write(buff, 0, n); } fos.close(); io.close();
 */
