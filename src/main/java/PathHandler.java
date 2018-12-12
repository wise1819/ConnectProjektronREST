import java.util.HashMap;
import java.util.Map;

public class PathHandler {
    private String urlBCS;
    Map<String, String> bcsPaths;
    public PathHandler(String baseBCSurl){
        this.urlBCS = baseBCSurl;
        // Create Map which contains all REST endpoints of BCS-instance
        this.bcsPaths = new HashMap<>();
        bcsPaths.put("login","/rest/auth/login");
        bcsPaths.put("getBookings", "/app/rest/timerecording/bookings");
        bcsPaths.put("createActivityForUserStory","/rest/mylyn/scrum/sprints/:oid/createActivity");
        bcsPaths.put("modifyActivity","/rest/mylyn/scrum/sprints/modifyActivity/:oid");
        bcsPaths.put("requestOwnSprint","/rest/mylyn/scrum/sprints");
        bcsPaths.put("requestSingleSprint","/rest/mylyn/scrum/sprints/:oid");
        bcsPaths.put("commentTicket","/rest/mylyn/tickets/:ticketOid/createComment");
        bcsPaths.put("getAllTickets","/rest/mylyn/tickets/all");
        bcsPaths.put("getOwnedTickets","/rest/mylyn/tickets");
        bcsPaths.put("getSingleTicket","/rest/mylyn/tickets/:oid");

        //Concatenating baseURL with specific path for endpoint
        for (Map.Entry<String,String> item: bcsPaths.entrySet()){
            String i = item.getValue();
            i = urlBCS + i;
        }
    }


    // Ugly Solution for getting whole Paths on actuall BCS-System
    public String getPathForBCSServer(String activity){

        return this.bcsPaths.get(activity);
    }
}
