import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class UnirestAuthorization {

	public static void main(String[] args) throws UnirestException {

		String username = ""; // TODO: fill in blanks
		String password = "";
		List<JSONObject> bookings = getBookings(username, password);

		bookings.forEach(System.out::println);
	}

	private static List<JSONObject> getBookings(String username, String password) throws UnirestException {
		List<JSONObject> bookings = new ArrayList<JSONObject>();

		HashMap<String, String> data = new HashMap<>();
		data.put("userLogin", username);
		data.put("userPwd", password);

		HttpResponse<String> authResponse = Unirest.post("https://fu-projekt.bcs-hosting.de/app/rest/auth/login")
				.header("Content-Type", "application/json").header("cache-control", "no-cache")
				.header("Content-Encoding", "UTF-8").body(new JSONObject(data).toString()).asString();
		String allCookies = authResponse.getHeaders().get("set-cookie");
		int beginIndex = allCookies.indexOf("MobileAppToken");
		int endIndex = allCookies.substring(beginIndex).indexOf(";");
		String mobileAppTokenCookie = allCookies.substring(beginIndex, endIndex);
		String xCsrfToken = authResponse.getHeaders().get("X-CSRF-Token");

		HttpResponse<JsonNode> timeRecordingResponse = Unirest.get(
				"https://fu-projekt.bcs-hosting.de/app/rest/timerecording/bookings?syncStateTimestamp=0&minDate=2018-10-29")
				.header("Cookie", mobileAppTokenCookie).header("X-CSRF-Token", xCsrfToken)
				.header("cache-control", "no-cache").asJson();

		try {

			JSONArray bookingsJsonArray = timeRecordingResponse.getBody().getObject().getJSONArray("bookings");
			for (int i = 0; i < bookingsJsonArray.length(); i++) {
				bookings.add(bookingsJsonArray.getJSONObject(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bookings;
	}

}
