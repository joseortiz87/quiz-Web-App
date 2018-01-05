$(document).ready(() => {

    SDK.User.loadNav();

    $("#id_createQuiz_row").hide();
    /*
    LOAD COURSES' COMBO
    */

    SDK.Courses.findCoursesByUserId((err, data) => {
        if (err && err.xhr.status === 401) {
            $(".form-group").addClass("has-error");
        }else if (err){
            console.log("fail");
        } else {
            console.log(data);
            var courses = jQuery.parseJSON(data);
            $.each(courses,function(index,course){
                $("#idquizContainer").append(
                    '<div id="row_' + course.courseID + '" class="row">'
                    + '    <h3>' + course.courseTitel + '</h3>'
                    + '</div>'
                );
                displayQuizzesByCourse(course);
            });
        }
    });

});

function displayQuizzesByCourse(course){
    SDK.Quiz.findQuizByCouse(course.courseID,(err, data) => {
        if (err && err.xhr.status === 401) {
            $(".form-group").addClass("has-error");
        }else if (err){
            console.log("fail");
        } else {
            console.log(data);
            var quizes = jQuery.parseJSON(data);
            $.each(quizes,function(index,quiz){
                $("#row_" + course.courseID).append(
                        '<div class="col-sm-4">'
                        + '    <h3 class="display-3">' + quiz.quizTitle + '</h3>'
                        + '    <a class="btn btn-primary btn-lg" href="#" onclick="startQuiz(\'' + quiz.quizID + '_' + quiz.quizTitle + '\');" role="button">Start</a>'
                        + '</div>'
                );
            });
        }
    });
}

function startQuiz(quizId){
    SDK.Storage.persist("Start Quiz", quizId);
    window.location.href = "Quiz.html";
}