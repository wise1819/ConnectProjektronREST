package authentication;

public class AuthenticationObject {

	private final String mobileAppTokenCookie;
	private final String xCsrfToken;

	public AuthenticationObject(String mobileAppTokenCookie, String xCsrfToken) {
		this.mobileAppTokenCookie = mobileAppTokenCookie;
		this.xCsrfToken = xCsrfToken;
	}

	public String getMobileAppTokenCookie() {
		return mobileAppTokenCookie;
	}

	public String getxCsrfToken() {
		return xCsrfToken;
	}

	public boolean isValid() {
		return !(mobileAppTokenCookie.isEmpty() || xCsrfToken.isEmpty());
	}
}
