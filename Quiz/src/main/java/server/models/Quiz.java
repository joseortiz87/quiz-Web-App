package server.models;

public class Quiz {

    int quizID;
    String quizTitle;
    int courseID;

    // constructor
    public Quiz (int quizID, String quizTitle, int courseID){
        this.quizID = quizID;
        this.quizTitle = quizTitle;
        this.courseID = courseID;
    }

    // tom constructor
    public Quiz() {}

    //get metoder
    public int getQuizID(){
        return quizID;
    }

    public String getQuizTitle(){
        return quizTitle;
    }

    public int getCourseID(){
        return courseID;
    }


    //set metoder
    public void setQuizID(int quizID){
        this.quizID=quizID;
    }

    public void setQuizTitle (String quizTitle){
        this.quizTitle=quizTitle;
    }
    public void setCourseID(int courseID){
        this.courseID=courseID;
    }
}
