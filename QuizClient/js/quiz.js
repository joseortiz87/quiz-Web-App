$(document).ready(() => {

    SDK.User.loadNav();
    var quiz = SDK.Storage.load("Start Quiz");
    var quizId = quiz.split('_')[0];
    $("#QuizName").text("Quiz " + quiz);

    /*
    LOAD COURSES' COMBO
    */
    SDK.Question.getQuestions(quizId,(err, data) => {
        if (err && err.xhr.status === 401) {
            $(".form-group").addClass("has-error");
        }else if (err){
            console.log("fail");
        } else {
            console.log(data);
            var questions = jQuery.parseJSON(data);
            $.each(questions,function(index,question){
                $("#idquizContainer").append(
                        '<div id="question_' + question.questionId + '" class="col-sm-4 well">'
                        + '    <h3 class="display-3">' + question.questionTitle + '</h3>'
                        + '</div>'
                );
                getChoicesByQuestion(question);
            });
        }
    });

});

function getChoicesByQuestion(question){
    SDK.Choice.getChoiceById(question.questionId,(err, data) => {
        if (err && err.xhr.status === 401) {
            $(".form-group").addClass("has-error");
        }else if (err){
            console.log("fail");
        } else {
            console.log(data);
            var choices = jQuery.parseJSON(data);
            $.each(choices,function(index,choice){
                var ischecked = "";
                if(choice.answer == 1){
                    ischecked = "checked";
                }
                $("#question_" + question.questionId).append(
                    '<div class="checkbox">'
                    + '   <label><input id="checkbox_' + choice.choiceId + '" onchange="updateChoice(' + choice.choiceId + ');" type="checkbox" value="" '
                    + ischecked + '>'
                    + choice.choiceTitle + '</label>'
                    + ' </div>'
                );
            });
        }
    });
}

function updateChoice(choiceId){
    var checked = 0;
    if($("#checkbox_" + choiceId).is(":checked")){
        checked = 1;
    }
    var choice = {
        choiceId : choiceId,
        choiceTitle : "",
        answer : checked,
        questionId : 0
    };
    SDK.Choice.updateChoice(choice,(err, data) => {
        if (err && err.xhr.status === 401) {
            $(".form-group").addClass("has-error");
        }else if (err){
            console.log("fail");
        } else {
            console.log(data);
            choice = jQuery.parseJSON(data);
        }
    });
}

function endQuiz(){
    window.location.href = "QuizChoice.html";
}