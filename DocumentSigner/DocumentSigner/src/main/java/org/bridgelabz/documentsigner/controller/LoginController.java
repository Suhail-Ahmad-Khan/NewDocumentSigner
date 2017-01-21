// This is the controller section of the login page.
// The methods defined here authenticates the user using his login and password 
// stored in the database with the input provided in the login page of the browser.
// The process of token generation is done here.

package org.bridgelabz.documentsigner.controller;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bridgelabz.documentsigner.json.ErrorResponse;
import org.bridgelabz.documentsigner.json.Response;
import org.bridgelabz.documentsigner.json.SuccessResponse;
import org.bridgelabz.documentsigner.json.TokenResponse;
import org.bridgelabz.documentsigner.model.Token;
import org.bridgelabz.documentsigner.model.User;
import org.bridgelabz.documentsigner.service.TokenService;
import org.bridgelabz.documentsigner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

	@Autowired
	UserService userService;

	@Autowired
	private TokenService tokenService;

	// Opens the JSP Login page in the browser
	@RequestMapping("/loginPage")
	public String init(HttpServletRequest request) {
		return "login";
	}

	// The data filled in the login page is validated from the database
	// and proper response is generated.
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody Response login(@RequestBody User pUser, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();
		User user = userService.authenticateUser(pUser.getEmail(), pUser.getPassword());

		String accessToken = UUID.randomUUID().toString().replaceAll("-", "");
		String refreshToken = UUID.randomUUID().toString().replaceAll("-", "");

		if (user == null) {
			ErrorResponse er = new ErrorResponse();
			er.setStatus(-1);
			er.setDisplayMessage("Invalid credential");
			er.setErrorMessage("user not found");
			return er;
		} else {
			Token token = new Token();
			token.setCreatedOn(new Date());
			token.setAccessToken(accessToken);
			token.setRefreshToken(refreshToken);
			token.setUserId(user.getId());
			tokenService.addToken(token);
			session.setAttribute("user", user);

			SuccessResponse er = new SuccessResponse();
			er.setStatus(1);
			er.setMessage("successfully logged in");

			TokenResponse tr = new TokenResponse();
			tr.getAccessToken();
			tr.getRefreshToken();
			tr.setStatus(1);

			Cookie ck = new Cookie("access_token", token.getAccessToken());
			response.addCookie(ck);
			System.out.println(ck.getValue());
			return tr;
		}
	}

	// Redirects the User back to the main page.
	@RequestMapping(value = "/signout", method = RequestMethod.GET)
	public void signout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		session.invalidate();
		session = request.getSession();
		try {
			response.sendRedirect("http://localhost:8080/DocumentSigner/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
