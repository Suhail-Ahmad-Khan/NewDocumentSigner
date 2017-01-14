package org.bridgelabz.documentsigner.controller;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bridgelabz.documentsigner.json.ErrorResponse;
import org.bridgelabz.documentsigner.json.Response;
import org.bridgelabz.documentsigner.json.SuccessResponse;
import org.bridgelabz.documentsigner.json.TokenResponse;
import org.bridgelabz.documentsigner.model.Token;
import org.bridgelabz.documentsigner.service.TokenService;
import org.bridgelabz.documentsigner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TokenController {

	@Autowired
	UserService userService;

	@Autowired
	private TokenService tokenService;

	@RequestMapping(value = "/generateTokens", method = RequestMethod.GET)
	public @ResponseBody Response generateNewTokens(@RequestParam("refresh_token") String refreshToken,
			HttpServletRequest request, HttpServletResponse response) {

		Date currentDate = new Date();

		Token token = tokenService.getTokenByRefToken(refreshToken);

		if (token == null) {

			ErrorResponse er = new ErrorResponse();
			er.setStatus(-5);
			er.setDisplayMessage("Token not found");

			return er;
		}

		Date date = token.getCreatedOn();

		long diff = currentDate.getTime() - date.getTime();
		long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(diff);
		if (diffInSeconds > 60 * 60) // 60min
		{
			ErrorResponse er = new ErrorResponse();
			er.setStatus(-6);
			er.setDisplayMessage("Refresh Token Expired");

			return er;
		}

		String accessToken = UUID.randomUUID().toString().replaceAll("-", "");
		System.out.println("accessToken: " + accessToken);
		String refreshTokenNew = UUID.randomUUID().toString().replaceAll("-", "");
		System.out.println("refreshToken: " + refreshToken);

		Token tokenNew = new Token();
		tokenNew.setCreatedOn(new Date());
		tokenNew.setAccessToken(accessToken);
		tokenNew.setRefreshToken(refreshTokenNew);
		tokenNew.setUserId(token.getUserId());
		tokenService.addToken(tokenNew);

		SuccessResponse er = new SuccessResponse();
		er.setStatus(1);
		er.setMessage("successfully logged in");

		TokenResponse tr = new TokenResponse();
		tr.setAccessToken(tokenNew.getAccessToken());
		tr.setRefreshToken(tokenNew.getRefreshToken());
		tr.setStatus(1);

		Cookie ck = new Cookie("access_token", token.getAccessToken());
		response.addCookie(ck);
		System.out.println(ck.getValue());
		return tr;
	}
}