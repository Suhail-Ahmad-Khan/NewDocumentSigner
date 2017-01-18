// This is the controller section of handling the signature(images).
// The methods defined here adds the signatures in the database,
// lists the signatures stored by the user in the database and downloads the
// signature on user's insistence.
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
import org.bridgelabz.documentsigner.model.Signature;
import org.bridgelabz.documentsigner.model.User;
import org.bridgelabz.documentsigner.service.SignatureService;
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
public class SignatureController {

	@Autowired
	private SignatureService signatureService;

	@RequestMapping(value = "/addSignatures", method = RequestMethod.GET)
	public String getData(Model model) {
		Signature signature = new Signature();
		model.addAttribute("signature", signature);
		return "addSignatures";
	}

	@RequestMapping(value = "/signature", method = RequestMethod.GET)
	public String signaturePage() {
		return "success";
	}

	@RequestMapping(value = "/addSignatures", method = RequestMethod.POST)
	public @ResponseBody Response addSignature(@ModelAttribute("signature") Signature signature, BindingResult result,
			MultipartFile file, HttpServletRequest request) {
		HttpSession httpSession = request.getSession();
		User user = (User) httpSession.getAttribute("user");
		signature.setUserId(user.getId());
		InputStream io = null;
		try {
			signature.setImageName(file.getOriginalFilename());
			signature.setContentType(file.getContentType());
			io = file.getInputStream();

			KeyGenerator keygen = new KeyGenerator();
			SecretKey documentKey = keygen.generateKey();
			byte[] content = IOUtils.toByteArray(io);
			byte[] encryptData = keygen.encryptFile(documentKey, content);
			io = new ByteArrayInputStream(encryptData);

			signatureService.addSignature(signature, io);
			io.close();
			SuccessResponse sr = new SuccessResponse();
			sr.setMessage("Signature added successfully!");
			sr.setStatus(1);
			return sr;

		} catch (IOException e) {
			e.printStackTrace();
			ErrorResponse er = new ErrorResponse();
			er.setErrorMessage("Error while saving the Signature" + e.getMessage());
			er.setDisplayMessage("Failed to save Signature. Please try again");
			er.setStatus(-1);
			return er;
		}
	}

	@RequestMapping(value = "/signatureList", method = RequestMethod.GET)
	public @ResponseBody List<Signature> listAllSignatures(HttpServletRequest request) {
		HttpSession httpSession = request.getSession();
		User user = (User) httpSession.getAttribute("user");
		List<Signature> signatureInfo = signatureService.listSignatures(user.getId());
		System.out.println(signatureInfo.size());
		return signatureInfo;
	}

	@RequestMapping(value = "/downloadSignature", method = RequestMethod.GET)
	public @ResponseBody Response displaySignatureDetails(@RequestParam("id") Integer id, HttpServletRequest request,
			HttpServletResponse response) {

		Signature signature = signatureService.getSignatureContent(id);
		try {
			InputStream io = signature.getContent().getBinaryStream();
			if (io == null) {
				ErrorResponse errorresponse = new ErrorResponse();
				errorresponse.setStatus(-1);
				errorresponse.setDisplayMessage("Image not found");
				return errorresponse;
			}

			KeyGenerator keygen = new KeyGenerator();
			SecretKey documentKey = keygen.generateKey();
			byte[] encryptedContent = IOUtils.toByteArray(io);
			byte[] decryptData = keygen.decryptFile(documentKey, encryptedContent);
			io = new ByteArrayInputStream(decryptData);

			byte[] buff = new byte[io.available()];
			response.setContentType(signature.getContentType());
			response.setHeader("Content-Disposition", "attachment; filename=" + signature.getImageName());
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