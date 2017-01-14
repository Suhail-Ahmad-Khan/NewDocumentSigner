package org.bridgelabz.documentsigner.service;

import org.bridgelabz.documentsigner.model.Token;
import org.bridgelabz.documentsigner.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenService {

	public Token addToken(User user, Token token);

	public Token authToken(String accessToken, String refreshToken);

	public Token getToken(String accessToken);

	public Token getTokenByRefToken(String refreshToken);

	/*public Token generateToken(User user, Token token);*/

}