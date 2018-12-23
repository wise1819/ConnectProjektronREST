package mylin;

import authentication.AuthenticationObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;

/**
 * Class for fetching all of the Mylin-Sprints for the given account
 */
public class MylinFetcher {

    public enum RESSOURCES {
        OWN_SPRINTS, ALL_TICKETS, OWN_TICKETS
    }

    private static final String BCS_SERVER = "http://fuberlinws18.demo.projektron.de";
    private static final String OWN_SPRINTS = "/rest/mylyn/scrum/sprints";
    private static final String ALL_TICKETS = "/rest/mylyn/tickets/all";
    private static final String OWN_TICKETS = "/rest/mylyn/tickets";

    public JSONArray fetchMylin(AuthenticationObject auth, RESSOURCES res) {

        String url = "";
        if (res == RESSOURCES.OWN_SPRINTS) {
            url = BCS_SERVER + OWN_SPRINTS;
        } else if (res == RESSOURCES.ALL_TICKETS) {
            url = BCS_SERVER + ALL_TICKETS;
        } else if (res == RESSOURCES.OWN_TICKETS)
        {
            url = BCS_SERVER + OWN_TICKETS;
        }

        try {
            HttpResponse<JsonNode> response = getRequestWithAuthObject(auth, url);

            JsonNode responseBody = response.getBody();
            var json = responseBody.getArray();

            return json;
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        //TODO: Better not return null
        return null;
    }

    private HttpResponse<JsonNode> getRequestWithAuthObject(AuthenticationObject auth, String url)
            throws UnirestException {
        var x = Unirest.get(url).header("X-CSRF-Token", auth.getxCsrfToken()).header("cache-control", "no-cache").header("JSESSIONID", auth.getJsessionID());
        return x.asJson();
    }

}
