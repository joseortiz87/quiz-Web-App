package server.models;

public class Question {

    private int quizID;
    private int questionId;
    private String questionTitle;


    public Question(int quizId, String questionTitle, int questionId) {

        this.quizID = quizId;
        this.questionTitle = questionTitle;
        this.questionId = questionId;

    }

    public Question() {

    }

    public int getQuizID() {
        return quizID;
    }

    public void setQuizID(int quizID) {
        this.quizID = quizID;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

}