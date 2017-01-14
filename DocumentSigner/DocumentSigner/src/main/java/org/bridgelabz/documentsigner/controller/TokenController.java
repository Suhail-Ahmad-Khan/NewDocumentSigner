package org.bridgelabz.documentsigner.controller;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bridgelabz.documentsigner.json.SuccessResponse;
import org.bridgelabz.documentsigner.json.TokenResponse;
import org.bridgelabz.documentsigner.model.Token;
import org.bridgelabz.documentsigner.model.User;
import org.bridgelabz.documentsigner.service.TokenService;
import org.bridgelabz.documentsigner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TokenController {

	@Autowired
	UserService userService;

	@Autowired
	private TokenService tokenService;

	@RequestMapping(value = "/generateTokens", method = RequestMethod.GET)
	public String generateNewTokens(@ModelAttribute("user") User user, BindingResult result, HttpServletRequest request,
			HttpServletResponse response) {

		/* HttpSession session = request.getSession(); */

		/* Token token = tokenService.getTokenByRefToken(refreshToken); */

		String accessToken = UUID.randomUUID().toString().replaceAll("-", "");
		System.out.println("accessToken: " + accessToken);
		String refreshToken = UUID.randomUUID().toString().replaceAll("-", "");
		System.out.println("refreshToken: " + refreshToken);

		Token token = new Token();
		token.setCreatedOn(new Date());
		token.setAccessToken(accessToken);
		token.setRefreshToken(refreshToken);
		token.setUserId(user.getId());
		tokenService.addToken(user, token);

		/* session.invalidate(); */ // invalidate existing session
		/* session = request.getSession(); */
		/* session.setAttribute("user", user); */

		/* tokenService.generateToken(user, token); */

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
		return "success";
	}
}