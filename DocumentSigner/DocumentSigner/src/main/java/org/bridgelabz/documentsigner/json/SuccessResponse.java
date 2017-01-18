// POJO Class for returning the various success responses
// encountered in the various modules

package org.bridgelabz.documentsigner.json;

public class SuccessResponse extends Response {
	private String message;

	// Setter and Getter methods

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
