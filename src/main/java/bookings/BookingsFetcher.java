package bookings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import authentication.AuthenticationObject;

public class BookingsFetcher {

	private static final String BCS_SERVER = "https://fu-projekt.bcs-hosting.de";
	private static final String BOOKINGS_PATH = "/app/rest/timerecording/bookings?syncStateTimestamp=0&minDate=2018-09-29";

	public List<JSONObject> fetchAllBookingsForAccount(AuthenticationObject auth) {
		String url = BCS_SERVER + BOOKINGS_PATH;
		try {
			HttpResponse<JsonNode> response = getRequestWithAuthObject(auth, url);

			List<JSONObject> bookings = new ArrayList<JSONObject>();
			JSONArray bookingsJsonArray = response.getBody().getObject().getJSONArray("bookings");
			for (int i = 0; i < bookingsJsonArray.length(); i++) {
				bookings.add(bookingsJsonArray.getJSONObject(i));
			}
			return bookings;
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Collections.emptyList();
	}

	private HttpResponse<JsonNode> getRequestWithAuthObject(AuthenticationObject auth, String url)
			throws UnirestException {
		return Unirest.get(url).header("Cookie", auth.getMobileAppTokenCookie())
				.header("X-CSRF-Token", auth.getxCsrfToken()).header("cache-control", "no-cache").asJson();
	}

}
