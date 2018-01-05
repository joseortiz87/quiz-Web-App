const SDK = {
  serverURL: "http://localhost:8081/simple-service-webapp/api",
  request: (options, cb) => {

    let headers = {};
    if (options.headers) {
      Object.keys(options.headers).forEach((h) => {
        headers[h] = (typeof options.headers[h] === 'object') ? JSON.stringify(options.headers[h]) : options.headers[h];
      });
    }

    $.ajax({
      url: SDK.serverURL + options.url,
      method: options.method,
      headers: headers,
      contentType: "application/json",
      dataType: "json",
      data: JSON.stringify(options.data),
      success: (data, status, xhr) => {
        cb(null, data, status, xhr);
      },
      error: (xhr, status, errorThrown) => {
        cb({xhr: xhr, status: status, error: errorThrown});
      }
    });

  },

  User: {
      findAll: (cb) => {
          SDK.request({method: "GET", url: "/staffs"}, cb);
      },

      current: () => {
          return SDK.Storage.load("user");
      },
      logOut: () => {

          SDK.Storage.remove("userId");
          SDK.Storage.remove("user");
          window.location.href = "login.html";
      },

      create: (username, password, firstName, lastName, type, cb) => {
          SDK.request({
              method: "POST",
              url: "/user",
              data: {
                  username: username,
                  password: password,
                  firstName: firstName,
                  lastName: lastName,
                  type: type
              },
          }, (err, data) => {
              //On login-error
              if (err) return cb(err);

              cb(null, data);

          });
      },

      delete: (id) => {
          SDK.request({
                  method: "DELETE",
                  url: "/user/" + id
              },
              (err) => {
                  if (err) return err;
              });
     },

    login: (username, password, cb) => {
      SDK.request({
        data: {
          username: username,
          password: password
        },
        url: "/user/login",
        method: "POST"
      }, (err, data) => {

        //On login-error
        if (err) return cb(err);

        data = JSON.parse(data);

        SDK.Storage.persist("username", data.username);
        SDK.Storage.persist("firstName", data.firstName);
        SDK.Storage.persist("lastName", data.lastName);
        SDK.Storage.persist("User ID", data.userId);
        SDK.Storage.persist("User type", data.type);

        cb(null, data);

      });
    },

    loadNav: (cb) => {
      $("#nav-container").load("nav.html", () => {
        const currentUser = SDK.User.current();
        if (currentUser) {
          $(".navbar-right").html(`
            
            <li><a href="#" id="logout-link">Logout</a></li>
          `);
        } else {
          $(".navbar-right").html(`
            <li><a href="login.html">Logout <span class="sr-only">(current)</span></a></li>
          `);
        }
        $("#logout-link").click(() => SDK.User.logOut());
        if(SDK.Storage.load("User type") == 1){
          //USER
          $("#quizManager_menu").remove();
          $("#createAdmin").remove();
        }else if(SDK.Storage.load("User type") == 2){
          //ADMIN
          $("#choiceQuiz_menu").remove();
        }else{
          $("#choiceQuiz_menu").remove();
          $("#quizManager_menu").remove();
          $("#createAdmin").remove();
          $("#enrollCouse").remove();
        }
        cb && cb();
      });
    }
  },


Storage: {
    prefix: "Quiz",
    persist: (key, value) => {
        window.localStorage.setItem(SDK.Storage.prefix + key, (typeof value === 'object') ? JSON.stringify(value) : value)
    },
    load: (key) => {
        const val = window.localStorage.getItem(SDK.Storage.prefix + key);
        try {
            return JSON.parse(val);
        }
        catch (e) {
            return val;
        }
    },
    remove: (key) => {
        window.localStorage.removeItem(SDK.Storage.prefix + key);
    }
},

Courses:{
    findAll: (cb) => {
      SDK.request({method: "GET", 
                   url: "/Courses/" }, cb);
    },
    findCoursesByUserId: (cb) => {
      SDK.request({method: "GET", 
                   url: "/Courses/" + SDK.Storage.load("User ID") }, cb);
    },
    enrollUser2Course: (userCourse,cb) => {
      SDK.request({method: "PUT", 
                   url: "/Courses/", data : userCourse}, cb);
    },
    deleteEnrollUser2Course: (userCourse,cb) => {
      SDK.request({method: "DELETE", 
                   url: "/Courses/" , data : userCourse }, cb);
    }
},

Quiz:{
    findQuizByCouse: (courseId,cb) => {
      SDK.request({method: "GET", 
                   url: "/quiz/" + courseId }, cb);
    },
    deleteQuizById: (quizID,cb) => {
      SDK.request({method: "DELETE", 
                   url: "/quiz/" + quizID }, cb);
    },
    addQuiz: (quiz,cb) => {
      SDK.request({method: "POST", 
                   url: "/quiz", data: quiz }, cb);
    }
},

Question:{
    getQuestions: (quizId,cb) => {
      SDK.request({method: "GET", 
                   url: "/Question/" + quizId }, cb);
    },
    createQuestion: (question,cb) => {
      SDK.request({method: "POST", 
                 url: "/Question", data: question }, cb);
    }
},

Choice:{
    getChoiceById: (questionID,cb) => {
      SDK.request({method: "GET", 
                   url: "/Choice/" + questionID }, cb);
    },
    createChoice: (choice,cb) => {
      SDK.request({method: "POST", 
                 url: "/Choice", data: choice }, cb);
    },
    updateChoice: (choice,cb) => {
      SDK.request({method: "PUT", 
                 url: "/Choice", data: choice }, cb);
    }
},

};