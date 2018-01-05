$(document).ready(() => {

    var username = SDK.Storage.load("username");
    var id = SDK.Storage.load("user ID");


$('#usernameInput').text(username);
$('#idInput').text(id);


    $('#sletBrugerKnap').click((e) => {
        e.preventDefault();

        var userID = SDK.Storage.load("user ID");
        console.log("Button works!");

        SDK.User.delete(userID);
         alert("User was deleted!");
           window.location.href = "login.html";
           SDK.Storage.remove("user ID");
           SDK.Storage.remove("username");

    });

});