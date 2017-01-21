// This is an interface which declares the methods 
// and provides the services to the Token

package org.bridgelabz.documentsigner.service;

import org.bridgelabz.documentsigner.model.Token;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenService {

	public Token addToken(Token token);

	public Token getToken(String accessToken);

	public Token getTokenByRefToken(String refreshToken);
}