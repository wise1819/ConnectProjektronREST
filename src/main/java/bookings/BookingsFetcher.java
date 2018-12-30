package bookings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import authentication.Authenticator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import authentication.AuthenticationObject;

public class BookingsFetcher {

	private static final String BOOKINGS_PATH = "/app/rest/timerecording/bookings";
	private String bcs_url;
	private String username;
	private String password;


	public BookingsFetcher(String bcs_url, String username, String password) {
		this.bcs_url = bcs_url;
		this.username = username;
		this.password = password;
	}

	/**
	 * This method returns the bookings-array
	 * @return the bookings contained in the json-attribute "bookings"
	 */
	public List<JSONObject> fetchAllBookingsForAccount() {
		String url = this.bcs_url + BOOKINGS_PATH;

		var authenticator = new Authenticator();
		var auth = authenticator.authenticateToDemoServer(this.username, this.password, false );

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

	//TODO: change to new Cookies
	private HttpResponse<JsonNode> getRequestWithAuthObject(AuthenticationObject auth, String url)
			throws UnirestException {
		var request = Unirest.get(url).header("X-CSRF-Token", auth.getxCsrfToken()).header("cache-control", "no-cache")
				.header("MobileAppToken", auth.getMobileAppTokenCookie());
				return request.asJson();

	}

}
