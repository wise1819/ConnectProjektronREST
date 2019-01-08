import bookings.BookingsFetcher;
import mylin.MylinFetcher;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class UnirestAuthorization {
    //TODO: 1. Fill account information and base URL for BCS instance here
    private static String username = "Admin";
    private static String password = "Admin";
    //old is: http://fu-projekt.bcs-hosting.de, new is: http://fuberlinws18.demo.projektron.de
    private static String bcs_base_url = "http://fu-projekt.bcs-hosting.de";

    /**
     * Gets all Bookings for the account of the user entered in the static field variables
     *
     * @return A JsonList with only the Bookings, the SyncStateTimestamp is stripped
     */
    public static List<JSONObject> getBookings() {
        checkForPW();

        var bookingsFetchter = new BookingsFetcher(bcs_base_url, username, password);
        return bookingsFetchter.fetchAllBookingsForAccount();


    }

    /**
     * @param res the type of resource you would like to request: OWN_TICKETS, ALL_TICKETS or OWN_SPRINTS
     * @return The full response as specified in the documentation
     */
    public static JSONArray getMylin(MylinFetcher.RESSOURCES res) {

        var ownSprints = new MylinFetcher(bcs_base_url, username, password);
        return ownSprints.fetchMylin(res);
    }


    public static void main(String[] args) {

        getBookings();
        getMylin(MylinFetcher.RESSOURCES.ALL_TICKETS);

    }

    private static void checkForPW() {
        if (username.equals("") || password.equals("")) {
            System.err.println("Please provide Username and password");
        }
    }
}