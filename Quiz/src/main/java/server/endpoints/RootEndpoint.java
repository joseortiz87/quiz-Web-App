package server.endpoints;

import server.utility.Log;

import javax.ws.rs.GET;
import javax.ws.rs.core.Response;

public class RootEndpoint {
    Log log = new Log();

    @GET
    public Response defaultGetMethod(){
        log.writeLog(this.getClass().getName(), this, "We are now getting the default method for rootEndPoint", 0);
        return Response.status(200).type("application/json").entity("{\"default\":\"true\"}").build();
    }
}
