

import authentication.Authenticator;

import bookings.BookingsFetcher;
import com.mashape.unirest.http.exceptions.UnirestException;
import mylin.MylinFetcher;


public class UnirestAuthorization {
    //TODO: Make userdata changable from the outside
    private static String username = "Admin";
    private static String password = "Admin";
    //TODO: make this URL changable from the outsite (via cli, enviroment, config-file,...)
    private static Authenticator authenticator;
    public static void main(String[] args) throws UnirestException {


        if (username.equals("") || password.equals("")) {
            System.out.println("Please provide Username and password");
            return;
        }

        authenticator = new Authenticator();
        var authobject = authenticator.authenticateToDemoServer(username,password);

        /* ### GET BOOKING-DATA */
        var bookingsFetchter = new BookingsFetcher();
        var bookings = bookingsFetchter.fetchAllBookingsForAccount(authobject);
        bookings.forEach(System.out::println);


        /* ### GET MYLIN-DATA ###*/


        var ownSprints = new MylinFetcher();
        //Set RESSOURCES-Parameter to choose REST-Endpoint
        // OWN_TICKETS => get /rest/mylyn/tickets
        // ALL_TICKETS => get /rest/mylyn/tickets/all
        // OWN_SPRINTS => get /rest/mylyn/scrum/sprints/
        var json = ownSprints.fetchMylin(authobject, MylinFetcher.RESSOURCES.OWN_SPRINTS);
        System.out.println(json.toString());
    }
}
