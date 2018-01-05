package server.Controllers;

import com.google.gson.Gson;
import server.DBWrapper;
import server.models.Course;
import server.models.User;
import server.models.UserCourse;
import server.utility.Log;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class CourseController {
    Gson gson;
    Log log = new Log();


    public ArrayList<Course> getCourses() throws IOException, ClassNotFoundException {

        log.writeLog(this.getClass().getName(), this, "We are now courses", 0);
        DBWrapper db = new DBWrapper();

        ArrayList<Course> c = db.getCourses();
        return c;
    }
    
    public ArrayList<Course> getCoursesUser(int UserId) throws IOException, ClassNotFoundException {

        log.writeLog(this.getClass().getName(), this, "We are now courses", 0);
        DBWrapper db = new DBWrapper();
        User user = new User();
        user.setUserId(UserId);
        ArrayList<Course> c = db.getUsersCourses(user);
        return c;
    }
    
    public UserCourse enrollUser2Course(UserCourse userCourse) throws IllegalArgumentException, SQLException, IOException, ClassNotFoundException{
    	log.writeLog(this.getClass().getName(), this, "We are enrolling User to course", 0);
        DBWrapper db = new DBWrapper();
        UserCourse c = db.enrollUser2Course(userCourse);
        return c;
    }
    
    public UserCourse deleteEnrollUser2Course(UserCourse userCourse) throws IllegalArgumentException, SQLException, IOException, ClassNotFoundException{
    	log.writeLog(this.getClass().getName(), this, "We are deleting enroll of User to course", 0);
        DBWrapper db = new DBWrapper();
        UserCourse c = db.deleteEnrollUser2Course(userCourse);
        return c;
    }
}
