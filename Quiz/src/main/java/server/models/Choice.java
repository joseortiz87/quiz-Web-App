package server.models;

import server.utility.Log;

public class Choice {
    Log log = new Log();
    private int choiceId;
    private String choiceTitle;
    private int answer;
    private int questionId;


    //
    public Choice (int choiceId, String choiceTitle, int answer, int questionId) {
        this.choiceId = choiceId;
        this.choiceTitle = choiceTitle;
        this.answer = answer;
        this.questionId = questionId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(int choiceId) {
        this.choiceId = choiceId;
    }

    public String getChoiceTitle() {
        return choiceTitle;
    }

    public void setChoiceTitle(String choiceTitle) {
        this.choiceTitle = choiceTitle;
    }

    public int isAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }
}
