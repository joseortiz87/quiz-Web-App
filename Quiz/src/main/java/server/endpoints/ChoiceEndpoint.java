package server.endpoints;
//

import com.google.gson.Gson;
import server.Controllers.ChoiceController;
import server.utility.Log;
import server.models.Choice;
import server.security.XORController;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;



@Path("/Choice")
public class ChoiceEndpoint {

    Log log = new Log();
    ChoiceController cController = new ChoiceController();





    @GET
    @Path("/{id}")
    public Response getChoiceById(@PathParam("id") int questionID) throws IOException{

        log.writeLog(this.getClass().getName(), this, "We are now getting Choice by Id parameter", 0);
        ArrayList<Choice> choices = cController.getChoices(questionID);
        String output = new Gson().toJson(choices);
        String encryptedOutput = XORController.encryptDecryptXOR(output);
        encryptedOutput = new Gson().toJson(encryptedOutput);

        //Choice foundChoice =
        return Response
                .status(200)
                .type("application/json")
                .entity(encryptedOutput)
                .build();
    }


    @POST
    public Response createChoice(String jsonChoice) throws Exception {
        log.writeLog(this.getClass().getName(), this, "We are now creating choice", 2);
        Choice createdChoice = cController.createChoice(new Gson().fromJson(jsonChoice, Choice.class));

        String output = new Gson().toJson(createdChoice);
        String encryptedOutput = XORController.encryptDecryptXOR(output);
        encryptedOutput = new Gson().toJson(encryptedOutput);

        return Response
                .status(200)
                .type("application/json")
                .entity(encryptedOutput)
                .build();


    }
    
    @PUT
    public Response updateChoice(String jsonChoice) throws Exception {
        log.writeLog(this.getClass().getName(), this, "We are now creating choice", 2);
        Choice createdChoice = cController.updateChoice(new Gson().fromJson(jsonChoice, Choice.class));

        String output = new Gson().toJson(createdChoice);
        String encryptedOutput = XORController.encryptDecryptXOR(output);
        encryptedOutput = new Gson().toJson(encryptedOutput);

        return Response
                .status(200)
                .type("application/json")
                .entity(encryptedOutput)
                .build();


    }


}
