package server.endpoints;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import server.Controllers.CourseController;
import server.security.XORController;
import server.utility.Log;
import server.models.Choice;
import server.models.Course;
import server.models.UserCourse;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


@Path("/Courses")
public class CourseEndpoint {

    Log log = new Log();

    String demoJson = new Gson().toJson("Courses");
    @GET
    public Response getCourses() throws IOException, ClassNotFoundException {
        CourseController courseController = new  CourseController();
        log.writeLog(this.getClass().getName(), this, "We are now getting courses", 2);

        ArrayList<Course> courses = courseController.getCourses();
        String output = new Gson().toJson(courses);
        String encryptedOutput = XORController.encryptDecryptXOR(output);
        encryptedOutput = new Gson().toJson(encryptedOutput);

        return Response
                .status(200)
                .type("application/json")
                .entity(encryptedOutput)
                .build();
    }
    
    @GET
    @Path("{id}")
    public Response getCoursesUser(@PathParam("id") int UserId) throws IOException, ClassNotFoundException{
    	CourseController courseController = new  CourseController();
        log.writeLog(this.getClass().getName(), this, "We are now getting Courses by UserId", 2);

        ArrayList<Course> courses = courseController.getCoursesUser(UserId);
        String output = new Gson().toJson(courses);
        String encryptedOutput = XORController.encryptDecryptXOR(output);
        encryptedOutput = new Gson().toJson(encryptedOutput);

        return Response
                .status(200)
                .type("application/json")
                .entity(encryptedOutput)
                .build();
    }
    
    @PUT
    public Response enrollUser2Course(String jsonUserCourse) throws IOException, ClassNotFoundException, JsonSyntaxException, IllegalArgumentException, SQLException{
    	CourseController courseController = new  CourseController();
        log.writeLog(this.getClass().getName(), this, "We are now enrolling User to Courses", 2);

        UserCourse userCourse = courseController.enrollUser2Course(new Gson().fromJson(jsonUserCourse, UserCourse.class));
        String output = new Gson().toJson(userCourse);
        String encryptedOutput = XORController.encryptDecryptXOR(output);
        encryptedOutput = new Gson().toJson(encryptedOutput);

        return Response
                .status(200)
                .type("application/json")
                .entity(encryptedOutput)
                .build();
    }
    
    @DELETE
    public Response deleteEnrollUser2Course(String jsonUserCourse) throws IOException, ClassNotFoundException, JsonSyntaxException, IllegalArgumentException, SQLException{
    	CourseController courseController = new  CourseController();
        log.writeLog(this.getClass().getName(), this, "We are now deleting enrollment of User to Courses", 2);

        UserCourse userCourse = courseController.deleteEnrollUser2Course(new Gson().fromJson(jsonUserCourse, UserCourse.class));
        String output = new Gson().toJson(userCourse);
        String encryptedOutput = XORController.encryptDecryptXOR(output);
        encryptedOutput = new Gson().toJson(encryptedOutput);

        return Response
                .status(200)
                .type("application/json")
                .entity(encryptedOutput)
                .build();
    }
}

