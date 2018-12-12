import java.util.HashMap;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class UnirestAuthorization {

	public static void main(String[] args) throws UnirestException {

		String username = "Admin"; // TODO: fill in blanks
		String password = "Admin";
		String urlBCS = "http://fuberlinws18.demo.projektron.de";
		String urlRESTPath = "/app/rest/timerecording/bookings?syncStateTimestamp=0&minDate=2018-10-29";

		if (username.equals("") || password.equals("")) {
			System.out.println("Please provide Username and password");
			return;
		}

		AuthenticationObject ao = authenticate(username, password, urlBCS);


		HttpResponse<JsonNode> req = ao.prepareRequest(HttpVerb.GET, urlBCS + urlRESTPath );


		//HttpResponse<JsonNode> res = Unirest.get(urlBCS + urlRESTPath);


	}

	private static AuthenticationObject authenticate(String username, String password, String urlBCS) throws UnirestException{

		HashMap<String, String> data = new HashMap<>();
		data.put("userLogin", username);
		data.put("userPwd", password);

		HttpResponse<String> authResponse = Unirest.post(urlBCS + "/app/rest/auth/login")
				.header("Content-Type", "application/json").header("cache-control", "no-cache")
				.header("Content-Encoding", "UTF-8").body(new JSONObject(data).toString()).asString();
		String allCookies = authResponse.getHeaders().get("set-cookie");
		int beginIndex = allCookies.indexOf("MobileAppToken");
		int endIndex = allCookies.substring(beginIndex).indexOf(";");
		return new AuthenticationObject(allCookies.substring(beginIndex, endIndex),authResponse.getHeaders().get("X-CSRF-Token"));
	}


public enum HttpVerb {
		GET,POST,PUT
}


}
