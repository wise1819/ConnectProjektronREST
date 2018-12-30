import bookings.BookingsFetcher;
import com.mashape.unirest.http.exceptions.UnirestException;
import mylin.MylinFetcher;

//TODO: Currently only tested with newer Demo-Server, not older server
public class UnirestAuthorization {
    //TODO: 1. Fill account information and base URL for BCS instance here
    private static String username = "Admin";
    private static String password = "Admin";
    private static String bcs_base_url = "http://fuberlinws18.demo.projektron.de";

    public static void main(String[] args) {
        if (username.equals("") || password.equals("")) {
            System.out.println("Please provide Username and password");
            return;
        }


        //TODO: 2. To fetch Booking-Data use Code as seen below

        /* ### GET BOOKING-DATA */
        var bookingsFetchter = new BookingsFetcher(bcs_base_url, username, password);
        var bookings = bookingsFetchter.fetchAllBookingsForAccount();

        //TODO: 3. Since Mylin has different Endpoints use the ENUM-Types to specify which endpoint to use

        /* ### GET MYLIN-DATA ###*/
        // OWN_TICKETS => get /rest/mylyn/tickets
        // ALL_TICKETS => get /rest/mylyn/tickets/all
        // OWN_SPRINTS => get /rest/mylyn/scrum/sprints/

        var ownSprints = new MylinFetcher(bcs_base_url, username, password);
        var json = ownSprints.fetchMylin(MylinFetcher.RESSOURCES.OWN_SPRINTS);

    }
}