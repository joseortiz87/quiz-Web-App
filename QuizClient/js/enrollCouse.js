$(document).ready(() => {

    SDK.User.loadNav();

    /*
    LOAD COURSE
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
                $( "#id_enroll_tbody" ).append(
                    '<tr id="row_' + course.courseID + '" >'
                    + "<td>" + course.courseTitel + "</td>"
                    + "<td>" 
                    + '<div class="checkbox">'
                    + '   <label><input id="checkbox_' + course.courseID + '" onchange="updateenrollCouse(' 
                    + course.courseID + ');" type="checkbox" value="" >'
                    + '</label>'
                    + ' </div>'
                    + "</td></tr>"                                            
                );
            });
            checkedAlreadyEnrollCourses();
        }
    });


});

function checkedAlreadyEnrollCourses(){
    SDK.Courses.findCoursesByUserId((err, data) => {
        if (err && err.xhr.status === 401) {
            $(".form-group").addClass("has-error");
        }else if (err){
            console.log("fail");
        } else {
            console.log(data);
            var selectedCourses = jQuery.parseJSON(data);
            $.each(selectedCourses,function(index,selectedCourse){
                    $("#checkbox_" + selectedCourse.courseID).attr("checked","checked");
            });
        }
    });
}


function updateenrollCouse(courseId){
    var userCourse = {
        userId : SDK.Storage.load("User ID"),
        courseID : courseId
    };
    if($("#checkbox_" + courseId).is(":checked")){
        SDK.Courses.enrollUser2Course(userCourse,(err, data) => {
            if (err && err.xhr.status === 401) {
                $(".form-group").addClass("has-error");
            }else if (err){
                console.log("fail");
            } else {
                console.log(data);
                userCourse = jQuery.parseJSON(data);
            }
        });
    }else{
        SDK.Courses.deleteEnrollUser2Course(userCourse,(err, data) => {
            if (err && err.xhr.status === 401) {
                $(".form-group").addClass("has-error");
            }else if (err){
                console.log("fail");
            } else {
                console.log(data);
                userCourse = jQuery.parseJSON(data);
            }
        });
    }
}