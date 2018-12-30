package authentication;

public class AuthenticationObject {

    enum API_TYPE {
        BOOKINGS, MYLIN
    }

    private final String mobileAppTokenCookie;
    private final String xCsrfToken;
    private final String authenticationMessage;
    private final String jsessionID;
    private final API_TYPE api;

    /**
     * AuthenticationObject containing Cookie-Data
     */
    public AuthenticationObject(String xCsrfToken, String mobileAppTokenCookie, String JSessionID, API_TYPE api) {

        if (api == API_TYPE.BOOKINGS) {
            this.mobileAppTokenCookie = mobileAppTokenCookie;
            this.xCsrfToken = xCsrfToken;
            this.authenticationMessage = "";
            this.jsessionID = null;
            this.api = api;
        } else {
            this.mobileAppTokenCookie = null;
            this.xCsrfToken = xCsrfToken;
            this.jsessionID = JSessionID;
            this.authenticationMessage = "";
            this.api = api;
        }

    }

    /**
     * AuthenticationObject if authentication did not work
     */
    public AuthenticationObject(String message) {
        this.mobileAppTokenCookie = "";
        this.xCsrfToken = "";
        this.jsessionID = null;
        this.authenticationMessage = message;
        this.api = null;
    }


    public String getMobileAppTokenCookie() {
        return mobileAppTokenCookie;
    }

    public String getxCsrfToken() {
        return xCsrfToken;
    }

    public String getJsessionID() {
        return this.jsessionID;
    }

    public boolean isValid() {
        return !(mobileAppTokenCookie.isEmpty() || xCsrfToken.isEmpty());
    }

    public String getAuthenticationMessage() {
        return authenticationMessage;
    }
}
