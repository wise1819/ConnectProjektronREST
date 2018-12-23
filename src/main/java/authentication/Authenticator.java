package authentication;

import java.util.HashMap;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Authenticator {

	public static final AuthenticationObject INVALID_AUTHENTICATIONOBJECT = new AuthenticationObject("", "", null);

	private static final String URL_BCS = "https://fu-projekt.bcs-hosting.de/app/rest/auth/login";
	private static final String URL_BCS_DEMO = "http://fuberlinws18.demo.projektron.de/rest/auth/login";

	public AuthenticationObject authenticate(String username, String password) {
		String urlBCS = URL_BCS;

		HashMap<String, String> data = new HashMap<>();
		data.put("userLogin", username);
		data.put("userPwd", password);
		try {
			HttpResponse<String> authResponse = doPostRequest(urlBCS, data);
			if (authResponse.getCode() == 200) {
				String mobileAppTokenCookie = getMobileAppTokenCookie(authResponse);
				String xCsrfToken = getXCsrfToken(authResponse);
				return new AuthenticationObject(mobileAppTokenCookie, xCsrfToken, AuthenticationObject.API_TYPE.BOOKINGS);
			} else {
				return new AuthenticationObject("ResponseCode was not 200 but: " + authResponse.getCode()
						+ ". Authorization Response was: " + authResponse.getBody());
			}

		} catch (UnirestException e) {
			e.printStackTrace();
			return new AuthenticationObject("Exception happened during authorization: " + e.getMessage());
		}
	}

	public AuthenticationObject authenticateToDemoServer(String username, String password) {
		String urlBCS = URL_BCS_DEMO;

		HashMap<String, String> data = new HashMap<>();
		data.put("login", username);
		data.put("pwd", password);

		try {
			HttpResponse<String> authResponse = doPostRequest(urlBCS, data);
			if (authResponse.getCode() == 200) {
				//String mobileAppTokenCookie = getMobileAppTokenCookie(authResponse);
				String xCsrfToken = getXCsrfToken(authResponse);
				String jSessionID = getJSessionID(authResponse);
				return new AuthenticationObject(jSessionID, xCsrfToken, AuthenticationObject.API_TYPE.MYLIN);
			} else {
				return INVALID_AUTHENTICATIONOBJECT;
			}

		} catch (UnirestException e) {
			e.printStackTrace();
		}
		return INVALID_AUTHENTICATIONOBJECT;
	}

	private HttpResponse<String> doPostRequest(String url, HashMap<String, String> data) throws UnirestException {
		return Unirest.post(url).header("Content-Type", "application/json").header("cache-control", "no-cache")
				.header("Content-Encoding", "UTF-8").body(new JSONObject(data).toString()).asString();
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

	private String getJSessionID(HttpResponse<String> authReponse){
		String cookies = authReponse.getHeaders().get("set-cookie");
		var cookiesSplit = cookies.split(";");
		var jsession = cookiesSplit[0].substring(11);
		return jsession;

	}

}
