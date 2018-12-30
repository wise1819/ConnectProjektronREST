package authentication;

import java.util.HashMap;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Authenticator {

    public static final AuthenticationObject INVALID_AUTHENTICATIONOBJECT = new AuthenticationObject("", "", "", null);

    private static final String BASE_BCS_URL = "https://fu-projekt.bcs-hosting.de/app/rest/auth/login";
    private static final String BASE_BCS_DEMO_URL = "http://fuberlinws18.demo.projektron.de";
    private static final String AUTH_ENDPOINT = "/app/rest/auth/login";
    private static final String NEW_AUTH_ENDPOINT = "/rest/auth/login";

    public AuthenticationObject authenticate(String username, String password) {
        String urlBCS = BASE_BCS_URL;

        HashMap<String, String> data = new HashMap<>();
        data.put("userLogin", username);
        data.put("userPwd", password);
        try {
            HttpResponse<String> authResponse = doPostRequest(urlBCS, data);
            if (authResponse.getCode() == 200) {
                String mobileAppTokenCookie = getMobileAppTokenCookie(authResponse);
                String xCsrfToken = getXCsrfToken(authResponse);
                return new AuthenticationObject(mobileAppTokenCookie, xCsrfToken, null, AuthenticationObject.API_TYPE.BOOKINGS);
            } else {
                return new AuthenticationObject("ResponseCode was not 200 but: " + authResponse.getCode()
                        + ". Authorization Response was: " + authResponse.getBody());
            }

        } catch (UnirestException e) {
            e.printStackTrace();
            return new AuthenticationObject("Exception happened during authorization: " + e.getMessage());
        }
    }

    /**
     * @param username     the username for the account that should be logged in
     * @param password     the corresponding password
     * @param newInterface if you want to login to the older bookings set false, if you want the mylin-interface set true
     * @return
     */
    public AuthenticationObject authenticateToDemoServer(String username, String password, boolean newInterface) {
        String urlBCS = BASE_BCS_DEMO_URL;
        HashMap<String, String> data = new HashMap<>();

        if (newInterface) {
            data.put("login", username);
            data.put("pwd", password);
            urlBCS = urlBCS + NEW_AUTH_ENDPOINT;
        } else {
            data.put("userLogin", username);
            data.put("userPwd", password);
            urlBCS = urlBCS + AUTH_ENDPOINT;
        }

        if (newInterface) {
            try {

                HttpResponse<String> authResponse = doPostRequest(urlBCS, data);
                if (authResponse.getCode() == 200) {
                    String xCsrfToken = getXCsrfToken(authResponse);
                    String jSessionID = getJSessionID(authResponse);
                    return new AuthenticationObject(xCsrfToken, null, jSessionID, AuthenticationObject.API_TYPE.MYLIN);
                } else {
                    return INVALID_AUTHENTICATIONOBJECT;
                }

            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return INVALID_AUTHENTICATIONOBJECT;


        } else {
            try {

                HttpResponse<String> authResponse = doPostRequest(urlBCS, data);
                if (authResponse.getCode() == 200) {
                    String xCsrfToken = getXCsrfToken(authResponse);
                    String mobileAppToken = getMobileAppTokenCookie(authResponse);
                    String jSessionId = getJSessionID(authResponse);
                    return new AuthenticationObject(xCsrfToken, mobileAppToken, jSessionId, AuthenticationObject.API_TYPE.BOOKINGS);
                } else {
                    return INVALID_AUTHENTICATIONOBJECT;
                }

            } catch (UnirestException e) {
                e.printStackTrace();
            }
            return INVALID_AUTHENTICATIONOBJECT;

        }
    }

    private HttpResponse<String> doPostRequest(String url, HashMap<String, String> data) throws UnirestException {
        var x = Unirest.post(url).header("Content-Type", "application/json").header("cache-control", "no-cache")
                .header("Content-Encoding", "UTF-8").body(new JSONObject(data).toString());
                return x.asString();
    }

    private String getXCsrfToken(HttpResponse<String> authResponse) {
        return authResponse.getHeaders().get("x-csrf-token");
    }

    private String getMobileAppTokenCookie(HttpResponse<String> authResponse) {
        String cookies = authResponse.getHeaders().get("set-cookie");
        System.out.println(cookies);
        int beginIndex = cookies.indexOf("MobileAppToken");
        int endIndex = cookies.substring(beginIndex).indexOf(";");
        String mobileAppTokenCookie = cookies.substring(beginIndex, endIndex);
        return mobileAppTokenCookie;
    }

    private String getJSessionID(HttpResponse<String> authReponse) {
        String cookies = authReponse.getHeaders().get("set-cookie");
        var cookiesSplit = cookies.split(";");
        var jsession = cookiesSplit[0].substring(11);
        return jsession;

    }

}
