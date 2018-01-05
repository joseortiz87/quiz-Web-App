$(document).ready(() => {

    SDK.User.loadNav();

    $("#opretBtn").click(() => {

        const username = $("#inputUsername").val();
        const password = $("#inputPassword").val();
        const firstName = $("#inputFirstname").val();
        const lastName = $("#inputLastname").val();
        const type = 2;
        console.log("success");

        SDK.User.create(username, password, firstName, lastName, type, (err, data) => {
            if (err) {
                alert("it didn't work");
                return console.log("fail", err)
            }
            console.log(data);

            window.location.href = "login.html";

        });

    });

});