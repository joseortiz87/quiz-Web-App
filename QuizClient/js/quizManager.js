$(document).ready(() => {

    SDK.User.loadNav();

    /*
    LOAD QUIZZES FOR EACH COURSE
    */

    SDK.Courses.findAll((err, data) => {
        if (err && err.xhr.status === 401) {
            $(".form-group").addClass("has-error");
        }else if (err){
            console.log("fail")
        } else {
            console.log(data);
            var courses = jQuery.parseJSON(data);
            $.each(courses,function(index,course){
                SDK.Quiz.findQuizByCouse(course.courseID,(err, data2) => {
                    console.log(data2);
                    var quizzes = jQuery.parseJSON(data2);
                    $.each(quizzes,function(index2,quiz){
                        $( "#id_quiz_tbody" ).append(
                                                '<tr id="row_' + quiz.quizID + '" >'
                                                + "<td>" + course.courseTitel + "</td>"
                                                + "<td>" + quiz.quizTitle + "</td>"
                                                + '<td><button onclick="deleteQuizById('+ quiz.quizID + ')" type="button" class="btn btn-danger">'
                                                + '<span class="glyphicon glyphicon-remove"></span></button></td>'
                                                + "</tr>"
                                            );
                    });
                });
            });
        }
    });


});

function deleteQuizById(quizId){
    SDK.Quiz.deleteQuizById(quizId,(err,data) =>{
        console.log(data);
        $("#row_"+quizId).remove();
    });
}

function addNewQuiz(){
    window.location.href = "newQuiz.html";
}