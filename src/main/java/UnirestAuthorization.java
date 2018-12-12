import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import spark.Spark;

import static spark.Spark.*;

public class UnirestAuthorization {
    //TODO: Make userdata changable from the outside
    private static String username = "Admin";
    private static String password = "Admin";
    //TODO: make this URL changable from the outsite (via cli, enviroment, config-file,...)
    private static PathHandler ph = new PathHandler("http://fuberlinws18.demo.projektron.de");

    public static void main(String[] args) throws UnirestException {


        if (username.equals("") || password.equals("")) {
            System.out.println("Please provide Username and password");
            return;
        }


        PathHandler ph = new PathHandler("http://fuberlinws18.demo.projektron.de");
//		AuthenticationObject ao = authenticate(username, password, ph.getPathForBCSServer("login"));

/*
        getBookings(null, new spark.Response());
*/
        //sets port for restserver
        port(9999);


        // ### GET ###
        get("/rest/timerecording/bookings", (req, res) -> getBookings(req, res));
        get("/rest/mylyn/scrum/sprints/", (req, res) -> null);
        get("/rest/mylyn/scrum/sprints/:oid", (req, res) -> null);
        get("/rest/mylyn/tickets/all", (req, res) -> null);
        get("/rest/mylyn/tickets", (req, res) -> null);
        get("/rest/mylyn/tickets/:oid", (req, res) -> null);

        // ### POST ###
        post("/rest/mylyn/scrum/sprints/:oid/createActivity", (req, res) -> null);
        post("/rest/mylyn/tickets/:ticketOid/createComment", (req, res) -> null);

        // ### PUT ###
        put("/rest/mylyn/scrum/sprints/modifyActivity/:oid", (req, res) -> null);

    }


    private static spark.Response getBookings(spark.Request req, spark.Response res) throws UnirestException {

        //TODO: Maybe better not authenticate in every method? But maybe better so it does not timeout
        AuthenticationObject ao = getAuthenticationObject();
        List<JSONObject> bookings = new ArrayList<JSONObject>();

        HttpResponse<JsonNode> response = ao.prepareRequest(Verbs.GET, ph.getPathForBCSServer("getBookings"));

        try {

            JSONArray bookingsJsonArray = response.getBody().getObject().getJSONArray("bookings");
            res.body(response.getBody().toString());
            for (int i = 0; i < bookingsJsonArray.length(); i++) {
                bookings.add(bookingsJsonArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        bookings.forEach(System.out::println);
        return res;
    }


    private static AuthenticationObject getAuthenticationObject() throws UnirestException {
        return authenticate(username, password, ph.getPathForBCSServer("login"));
    }

    //TODO: Put in fitting class
    private static AuthenticationObject authenticate(String username, String password, String urlBCS) throws UnirestException {

        HashMap<String, String> data = new HashMap<>();
        //Authentication JSON looks different on different BCS instances
        if (urlBCS.contains("demo")) {
            data.put("user", username);
            data.put("pwd", password);
        } else {
            data.put("userLogin", username);
            data.put("userPwd", password);
        }
        //TODO: @Christian, Problem ist hier, dass das JSON wohl nicht richtig ankommt. Bin mir nicht ganz sicher weshalb, der Code ist egientlich noch immer deiner aus Postman.
        // Bekomme gerade 403, was ich auch bekommen habe als das JSON im body falsch war.
        HttpResponse<String> authResponse = Unirest.post(urlBCS)
                .header("Content-Type", "application/json").header("cache-control", "no-cache")
                .header("Content-Encoding", "UTF-8").body(new JSONObject(data).toString()).asString();
        String allCookies = authResponse.getHeaders().get("set-cookie");
        int beginIndex = allCookies.indexOf("MobileAppToken");
        int endIndex = allCookies.substring(beginIndex).indexOf(";");
        return new AuthenticationObject(allCookies.substring(beginIndex, endIndex), authResponse.getHeaders().get("X-CSRF-Token"));
    }


    public enum Verbs {
        GET, POST, PUT
    }


}
