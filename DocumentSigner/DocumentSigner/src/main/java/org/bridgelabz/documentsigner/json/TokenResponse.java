// POJO Class for returning the various token responses
// encountered in the various modules

package org.bridgelabz.documentsigner.json;

public class TokenResponse extends Response {

	private String accessToken, refreshToken;

	// Setter and Getter methods

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}