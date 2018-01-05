package server.endpoints;
import com.google.gson.Gson;
import server.Controllers.QuizController;
import server.models.Quiz;
import server.security.XORController;
import server.utility.Log;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;




@Path("/quiz")
public class QuizEndpoint {

    Log log = new Log();

    QuizController controller = new QuizController();


    @GET
    @Path("{id}")
    public Response getQuizzes(@PathParam("id") int courseId) throws IOException, ClassNotFoundException {

        log.writeLog(this.getClass().getName(), this, "We are getting quizzes", 2);

        ArrayList<Quiz> allQuizzes = controller.getQuizzes(courseId);
        String output = new Gson().toJson(allQuizzes);


        output = XORController.encryptDecryptXOR(output);

        return Response
                .status(200)
                .type("application/json")
                .entity(new Gson().toJson(output))
                .build();
    }

    @POST
    public Response createQuiz(String quiz) throws Exception {

        log.writeLog(this.getClass().getName(), this, "We are creating a quiz", 0);

        Quiz foundQuiz = controller.createQuiz(new Gson().fromJson(quiz, Quiz.class));

        String output = new Gson().toJson(foundQuiz);
        String encryptedOutput = XORController.encryptDecryptXOR(output);
        encryptedOutput = new Gson().toJson(encryptedOutput);

        return Response
                .status(200)
                .type("application/json")
                .entity(encryptedOutput)
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteQuiz(@PathParam("id") int quizID) throws Exception {

        log.writeLog(this.getClass().getName(), this, "We are now in process of deleting a quiz", 2);

        Boolean deleteQuiz = controller.deleteQuiz(quizID);


        if (deleteQuiz == true) {
            log.writeLog(this.getClass().getName(), this, "user is getting deleted", 2);
            return Response
                    .status(200)
                    .type("application/json")
                    .entity(new Gson().toJson("User should be deleted"))
                    .build();
        } else {
            log.writeLog(this.getClass().getName(), this, "User was not deleted properly", 2);
            return Response
                    .status(200)
                    .type("application/json")
                    .entity(new Gson().toJson("Failed to delete user properly"))
                    .build();
        }

    }

}

