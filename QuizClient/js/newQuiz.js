$(document).ready(() => {

    SDK.User.loadNav();

    $("#id_createQuiz_row").hide();
    /*
    LOAD COURSES' COMBO
    */

    SDK.Courses.findAll((err, data) => {
        if (err && err.xhr.status === 401) {
            $(".form-group").addClass("has-error");
        }else if (err){
            console.log("fail");
        } else {
            console.log(data);
            var courses = jQuery.parseJSON(data);
            $.each(courses,function(index,course){
                $("#courseID").append('<option value="' + course.courseID + '">' + course.courseTitel + '</option>');
            });
        }
    });


});

var questionIndex = 0;
var choiceIndex = {};
var questionObject = {};
function addNewQuestion(){
    $("#id_createQuiz_row").show();
    questionIndex++;
    var idQuestionBox = "question_box_" + questionIndex;
    var idQuestionConteiner = "question_conteiner_" + questionIndex;
    var idQuestionTitle = "questionTitle_" + questionIndex;
    var idChoiceRow = "choiceRow_" + questionIndex;
    choiceIndex[questionIndex] = 0;
    $("#questionRows").append(
        '<div id="' + idQuestionBox + '" class="col-sm-4 well">'
        + '  <h3>Question ' + questionIndex + '</h3>'
        + '  <div id="'+ idQuestionConteiner + '">'
        + '      <div class="form-group">'
        + '        <label class="control-label" for="' + idQuestionTitle + '">Question title</label>'
        + '        <input type="text" class="form-control" name="' + idQuestionTitle + '" id="' + idQuestionTitle + '" placeholder="How ...?">'
        + '      </div>'
        + '  </div>'
        + '  <button onclick="addNewChoice(\'' + idChoiceRow + '\')" type="button" class="btn btn-success btn-sm">'
        + '    <span class="glyphicon glyphicon-plus"></span> Choice'
        + '  </button><br/>'
        + '  <div id="'+ idChoiceRow + '" class="row">'
        + '  </div>'
        + '</div>'
    );
}

function addNewChoice(index){
    var idChoiceRow = index;
    index = index.split("_")[1];
    if(choiceIndex[index]){
        choiceIndex[index] = choiceIndex[index] + 1;
    }else{
        choiceIndex[index] = 1;
    }
    var idChoiceTitle = "choiceTitle_" + index + '_' + choiceIndex[index];
    $("#" + idChoiceRow).append(
        '    <div class="col-sm-4">'
        + '        <div class="form-group">'
        + '            <input type="text" class="form-control" name="'+  idChoiceTitle + '" id="'+  idChoiceTitle + '" placeholder="Option' + choiceIndex[index] +'">'
        + '        </div>'
        + '    </div>'
    );
}

function createNewQuiz(){
    var quiz = {
        quizID : 0,
        quizTitle : $("#quizTitle").val(),
        courseID : $("#courseID").val()
    };
    SDK.Quiz.addQuiz(quiz,(err, data) => {
        if (err && err.xhr.status === 401) {
            $(".form-group").addClass("has-error");
        }else if (err){
            console.log("fail");
        } else {
            console.log(data);
            quiz = jQuery.parseJSON(data);
            createNewQuestions(quiz);
        }
    });
}

function createNewQuestions(quiz){
    $.each($("input[id*='questionTitle_']"),function(index,question){
        var question = {
            quizID : quiz.quizID,
            questionId : 0,
            questionTitle : $(question).val()
        };
        
        SDK.Question.createQuestion(question,(err, data) => {
            if (err && err.xhr.status === 401) {
                $(".form-group").addClass("has-error");
            }else if (err){
                console.log("fail");
            } else {
                console.log(data);
                var tmpQuestion = jQuery.parseJSON(data);
                createNewChoices(index+1,tmpQuestion);
            }
        });
    });
}

function createNewChoices(index,question){
    console.log("[Choice] question index: " + index);
    $.each($("input[id*='choiceTitle_" + index + "'"),function(indx,choice){
        var choice = {
            choiceId : 0,
            choiceTitle : $(choice).val(),
            answer : 0,
            questionId : question.questionId
        };

        SDK.Choice.createChoice(choice,(err, data) => {
            if (err && err.xhr.status === 401) {
                $(".form-group").addClass("has-error");
            }else if (err){
                console.log("fail");
            } else {
                console.log(data);
                var tmpChoice = jQuery.parseJSON(data);
            }
        });
    });
}