import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;


/**
 * A simple class that constructs base REST-Requests based on the data from the authentication step
 */
public class AuthenticationObject {
    private String mobileAppTokenCookie;
    private String xCsrfToken;

    AuthenticationObject(String mobileAppTokenCookie, String xCsrfToken){
        this.mobileAppTokenCookie = xCsrfToken;
        this.xCsrfToken = mobileAppTokenCookie;
    }

    /**
     *
     * @param type Type of the request you want to make => get, post, put
     * @return A Object that has the necessary authentication data already filled in
     */
    public HttpResponse<JsonNode> prepareRequest(UnirestAuthorization.Verbs type, String url){
        try {
        switch (type) {
            case GET:
                return prepareGetRequest(url);
            case POST:
                return preparePostRequest(url);
            case PUT:
                return preparePutRequest(url);
            default:
                 System.err.println("No legal http-verb chosen, use \"get, post or put\"");
                 throw new IllegalArgumentException();
        }}
        catch (UnirestException e){
            e.printStackTrace();
        }
        return null;
    }

    private HttpResponse<JsonNode> prepareGetRequest(String url) throws UnirestException {
         return Unirest.get(url).header("Cookie", this.mobileAppTokenCookie).header("X-CSRF-Token", this.xCsrfToken)
                .header("cache-control", "no-cache").asJson();
    }

    private HttpResponse<JsonNode> preparePostRequest(String url) throws UnirestException{
        return  Unirest.post(url).header("Cookie", this.mobileAppTokenCookie).header("X-CSRF-Token", this.xCsrfToken)
                .header("cache-control", "no-cache").asJson();
    };
    private HttpResponse<JsonNode> preparePutRequest(String url) throws  UnirestException{
        return Unirest.put(url).header("Cookie", this.mobileAppTokenCookie).header("X-CSRF-Token", this.xCsrfToken)
                .header("cache-control", "no-cache").asJson();
    };
}
