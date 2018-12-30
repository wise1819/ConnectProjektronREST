import bookings.BookingsFetcher;
import com.mashape.unirest.http.exceptions.UnirestException;
import mylin.MylinFetcher;


public class UnirestAuthorization {
    //TODO: 1. Fill account information and base URL for BCS instance here
    public static String username = "Admin";
    public static String password = "Admin";
    public static String bcs_base_url = "http://fuberlinws18.demo.projektron.de";

    public static void main(String[] args) throws UnirestException {


        if (username.equals("") || password.equals("")) {
            System.out.println("Please provide Username and password");
            return;
        }




        /* ### GET BOOKING-DATA */
        var bookingsFetchter = new BookingsFetcher(bcs_base_url,username,password);
        var bookings = bookingsFetchter.fetchAllBookingsForAccount();


        /* ### GET MYLIN-DATA ###*/
        var ownSprints = new MylinFetcher(bcs_base_url);
        // Set RESSOURCES-Parameter to choose REST-Endpoint
        // OWN_TICKETS => get /rest/mylyn/tickets
        // ALL_TICKETS => get /rest/mylyn/tickets/all
        // OWN_SPRINTS => get /rest/mylyn/scrum/sprints/
        var json = ownSprints.fetchMylin(bcs_base_url, username, password, MylinFetcher.RESSOURCES.ALL_TICKETS);

    }
}