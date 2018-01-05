package server.models;

public class Course {
    int courseID;
    String courseTitel;

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseTitel() {
        return courseTitel;
    }

    public void setCourseTitel(String courseTitel) {
        this.courseTitel = courseTitel;
    }

    public Course(int courseID, String courseTitel) {
        this.courseID = courseID;
        this.courseTitel = courseTitel;
    }

}
