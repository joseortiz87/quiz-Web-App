package server.endpoints;
import com.google.gson.Gson;
import server.utility.Log;
import server.Controllers.QuestionController;
import server.models.Question;
import server.security.XORController;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;


@Path("/Question")
public class QuestionEndpoint {
    Log log = new Log();
    QuestionController controller = new QuestionController();

    @GET
    @Path ("{quizId}")
    public Response getQuestions(@PathParam("quizId") int quizId) throws IOException, ClassNotFoundException {
        log.writeLog(this.getClass().getName(), this, "We are now getting questions", 2);
        ArrayList<Question> question = controller.getQuestions(quizId);
        String output = new Gson().toJson(question);
        String encryptedOutput = XORController.encryptDecryptXOR(output);
        encryptedOutput = new Gson().toJson(encryptedOutput);


//
            /*try {
                controller.getQuestions(new Gson().fromJson(jsonQuestion, Question.class));
            } catch (SQLException e) {
                e.printStackTrace();
            }*/


        return Response
                .status(200)
                .type("application/json")
                .entity(encryptedOutput)
                .build();

    }
    /*
        @GET
        @Path("{questionId}")
        public Response getQuestionById(@PathParam("questionId") int questionId) {


            return Response
                    .status(200)
                    .type("application/json")
                    .entity(new Gson().toJson("foundQuestion"))
                    .build();

        }
    */
    @POST
    public Response createQuestion(String jsonQuestion) throws Exception {

        log.writeLog(this.getClass().getName(), this, "We are now creating a question", 2);

        Question isCreated = controller.createQuestion(new Gson().fromJson(jsonQuestion, Question.class));
        String output = new Gson().toJson(isCreated);
        String encryptedOutput = XORController.encryptDecryptXOR(output);
        encryptedOutput = new Gson().toJson(encryptedOutput);

        return Response
                .status(200)
                .type("application/json")
                .entity(encryptedOutput)
                .build();

    }
}
