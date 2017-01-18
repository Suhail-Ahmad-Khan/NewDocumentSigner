// This program controls the time limit for which the user can remain logged on
// in a session. It implements the filter class and makes use of access and 
// refresh token to keep a tab on the session timeout. 

package org.bridgelabz.documentsigner.filter;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bridgelabz.documentsigner.json.ErrorResponse;
import org.bridgelabz.documentsigner.model.Token;
import org.bridgelabz.documentsigner.service.TokenService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class TokenBasedAuthFilter implements Filter {

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		Date currentDate = new Date();

		WebApplicationContext applicationContext = WebApplicationContextUtils
				.getWebApplicationContext(req.getServletContext());

		TokenService tokenService = (TokenService) applicationContext.getBean("tokenService");

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		String accessToken = request.getHeader("access_token");

		if (accessToken == null || accessToken.trim().isEmpty()) {
			Cookie[] cks = request.getCookies();
			if (cks != null) {
				for (Cookie cookie : cks) {
					if (cookie.getName().equals("access_token")) {
						accessToken = cookie.getValue();
						System.out.println(accessToken);
					}
				}
			}
		}

		if (accessToken == null || accessToken.trim().isEmpty()) {

			ErrorResponse er = new ErrorResponse();
			er.setStatus(-1);
			er.setDisplayMessage("Invalid credential");
			er.setErrorMessage("user not found");

			response.setContentType("application/json");
			String jsonResp = "{\"status\":\"-2\",\"errorMessage\":\"Invalid access token\"}";
			response.getWriter().write(jsonResp);
			return;
		}

		Token token = tokenService.getToken(accessToken);
		if (token == null) {

			response.setContentType("application/json");
			String jsonResp = "{\"status\":\"-3\",\"errorMessage\":\"Invalid access token\"}";
			response.getWriter().write(jsonResp);
			return;
		}

		Date date = token.getCreatedOn();

		long diff = currentDate.getTime() - date.getTime();
		long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(diff);
		if (diffInSeconds > 60 * 60) // 60min
		{
			// generate json error response - access token is expired
			response.setContentType("application/json");
			String jsonResp = "{\"status\":\"-4\",\"errorMessage\":\"Access token is expired. Generate new Access Tokens\"}";
			response.getWriter().write(jsonResp);
			return;
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {

	}
}