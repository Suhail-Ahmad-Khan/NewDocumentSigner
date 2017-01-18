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

	@RequestMapping("/loginPage")
	public String init(HttpServletRequest request) {
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	/*
	 * public @ResponseBody Response login(@RequestParam("email") String
	 * email, @RequestParam("password") String password, HttpServletRequest
	 * request, HttpServletResponse response) {
	 */
	public @ResponseBody Response login(@RequestBody User pUser, HttpServletRequest request,
			HttpServletResponse response) {

		/*
		 * System.out.println( lstr ); JSONObject jo = null; try { jo =
		 * (JSONObject) new JSONParser().parse(lstr); } catch (ParseException e)
		 * { // TODO Auto-generated catch block e.printStackTrace(); }
		 */

		HttpSession session = request.getSession();
		User user = userService.authUser(pUser.getEmail(), pUser.getPassword());
		// User user = userService.authUser(jo.get("email").toString(),
		// jo.get("password").toString());

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
