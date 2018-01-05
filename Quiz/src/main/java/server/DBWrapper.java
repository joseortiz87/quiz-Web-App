package server;

import server.Controllers.Config;
import server.models.*;
import server.security.Digester;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class DBWrapper {
    /*Når i arbejder lokalt, så lav to streger foran DEFAULT_URL og fjerne de to fra den neden under.
    * Derefter skal man erstarte DEFAULT_USERNAME til jeres lokale database navn og DEFAULT_PASSWORD til jeres lokale
    * pass.*/

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    private static Connection connection = null;

    public static Connection getConnection() throws SQLException, IOException, ClassNotFoundException {


        try {
            try {
                Class.forName(JDBC_DRIVER).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            connection = DriverManager.getConnection("jdbc:mysql://" + Config.getDatabaseHost() + ":" + Config.getDatabasePort() + "/" + Config.getDatabaseName(), Config.getDatabaseUsername(), Config.getDatabasePassword());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }


    public static User authorizeUser(String username, String password) throws Exception {
        Connection connection = getConnection();
        User userFound = null;
        User userFound_final = null;


        //Get user by username
        PreparedStatement getUserByUserName = connection.prepareStatement("select * from user where username  = ? ORDER BY createdTime DESC LIMIT 1");
        getUserByUserName.setString(1, username);
        ResultSet resultSet = getUserByUserName.executeQuery();

        String saltet_password = "";
        while (resultSet.next()) {
            userFound = new User();
            userFound.setUsername(resultSet.getString("userName"));
            userFound.setCreatedTime(resultSet.getLong("createdTime"));
            saltet_password = Digester.hashWithSalt(password, resultSet.getString("userName"), resultSet.getLong("createdTime"));
        }

        try {
            PreparedStatement authenticate = connection.prepareStatement("select * from user where username = ? AND password = ?");
            authenticate.setString(1, username);
            authenticate.setString(2, saltet_password);

            ResultSet resultSet2 = authenticate.executeQuery();

            while (resultSet2.next()) {
                try {

                    userFound_final = new User();
                    userFound_final.setUsername(resultSet2.getString("userName"));
                    userFound_final.setPassword(resultSet2.getString("password"));
                    userFound_final.setUserId(resultSet2.getInt("id"));
                    userFound_final.setFirstName(resultSet2.getString("firstName"));
                    userFound_final.setLastName(resultSet2.getString("lastName"));
                    userFound_final.setType(resultSet2.getInt("type"));
                    userFound_final.setCreatedTime(resultSet2.getLong("createdTime"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userFound_final;
    }


    public static User createUser(User createUser) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        //String PS = "INSERT INTO user (firstName, lastName, userName, password, type) VALUES (?,?,?,?,?)";

        //String PS = "INSERT INTO user (firstName) VALUES (" + createUser.getFirstName()+")";
        try {
            conn = DBWrapper.getConnection();
            preparedStatement = conn.prepareStatement("INSERT INTO user (firstName, lastName, userName, password, type, createdTime) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, createUser.getFirstName());
            preparedStatement.setString(2, createUser.getLastName());
            preparedStatement.setString(3, createUser.getUsername());
            preparedStatement.setString(4, Digester.hashWithSalt(createUser.getPassword(), createUser.getUsername(), createUser.getCreatedTime()));
            preparedStatement.setInt(5, createUser.getType());
            preparedStatement.setLong(6, createUser.getCreatedTime());

            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();

            if (rs.next()) {
                createUser.setUserId(rs.getInt(1));
            }

            return createUser;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            close(conn);
            close(preparedStatement);
        }

    }


    public static Quiz createQuiz(Quiz quiz) throws SQLException, IOException, ClassNotFoundException {
        Connection conn = null;
        conn = DBWrapper.getConnection();
        try {
            PreparedStatement createQuiz = conn.prepareStatement("INSERT INTO fmldb.quiz (quizTitle, course_id) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            createQuiz.setString(1, quiz.getQuizTitle());
            createQuiz.setInt(2, quiz.getCourseID());

            int rowsAffected = createQuiz.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet rs = createQuiz.getGeneratedKeys();
                if (rs != null && rs.next()) {
                    int autoIncrementedId = rs.getInt(1);
                    quiz.setQuizID(autoIncrementedId);
                } else {
                    quiz = null;
                }
                return quiz;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);

        }
        return null;
    }


    public static Boolean deleteQuiz(int quizId) throws Exception {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String PS = "DELETE FROM fmldb.quiz WHERE fmldb.quiz.id = ?";
        int check = 0;
        try {
            conn = DBWrapper.getConnection();
            preparedStatement = conn.prepareStatement(PS);
            preparedStatement.setInt(1, quizId);
            check = preparedStatement.executeUpdate();

            if (check == 1) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(conn);
            close(preparedStatement);
        }
        return false;
    }


    public static Question createQuestion(Question question) throws SQLException, IOException, ClassNotFoundException {
        Connection conn = DBWrapper.getConnection();



        try {

            //preparedStatement = conn.prepareStatement("INSERT INTO question (questionTitle, quiz_id) VALUES (?, ?)");
            PreparedStatement createQuestion = conn.prepareStatement("INSERT INTO fmldb.question (questionTitle, quiz_id)\n" +
                    "VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            System.out.println("title: " + question.getQuestionTitle() + " quizId: " + question.getQuizID());
            createQuestion.setString(1, question.getQuestionTitle());
            createQuestion.setInt(2, question.getQuizID());


            int rowsAffected = createQuestion.executeUpdate();
            if (rowsAffected == 1) {
                ResultSet rs = createQuestion.getGeneratedKeys();
                if(rs != null && rs.next()){
                    int id = rs.getInt(1);
                    question.setQuestionId(id);
                } else {
                    question = null;
                }
                return question;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(conn);

        }
        return null;
    }


    public static void deleteQuestion(Question question) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String PS = "DELETE FROM fmldb.question WHERE fmldb.question.id = " + question.getQuestionId();
        try {
            conn = DBWrapper.getConnection();
            conn.prepareStatement(PS);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(conn);
            close(preparedStatement);
        }
    }

    public Choice createChoice(Choice choice) throws IllegalArgumentException, SQLException, IOException, ClassNotFoundException {
             Connection conn = DBWrapper.getConnection();

        try {
            PreparedStatement createChoice = conn.prepareStatement("INSERT INTO fmldb.choice (choiceTitle, answer, question_id) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            createChoice.setString(1, choice.getChoiceTitle());
            createChoice.setInt(2, choice.isAnswer());
            createChoice.setInt(3, choice.getQuestionId());

            int rowsAffected = createChoice.executeUpdate();
            if(rowsAffected == 1) {
                ResultSet rs = createChoice.getGeneratedKeys();
                if(rs != null && rs.next()) {
                    int id = rs.getInt(1);
                    choice.setChoiceId(id);
                } else {
                    choice = null;
                }
                return choice;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            close(conn);
        }
        return null;
    }
    
    public Choice updateChoice(Choice choice) throws IllegalArgumentException, SQLException, IOException, ClassNotFoundException {
        Connection conn = DBWrapper.getConnection();

	   try {
	       PreparedStatement createChoice = conn.prepareStatement("UPDATE fmldb.choice SET answer = ? WHERE id = ?");
	       createChoice.setInt(1, choice.isAnswer());
	       createChoice.setInt(2, choice.getChoiceId());
	
	       int rowsAffected = createChoice.executeUpdate();
	       return choice;
	   } catch (SQLException e) {
	       e.printStackTrace();
	       close(conn);
	   }
	   return null;
	}

    public static Boolean deleteUser(int userId) throws Exception {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String PS = "DELETE FROM fmldb.user WHERE fmldb.user.id = ?";
        int check = 0;
        try {
            conn = DBWrapper.getConnection();
            preparedStatement = conn.prepareStatement(PS);
            preparedStatement.setInt(1, userId);
            check = preparedStatement.executeUpdate();

            if (check == 1) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(conn);
            close(preparedStatement);
        }
        return false;
    }




    public static void deleteChoice(Choice choice) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String PS = "DELETE FROM fmldb.choice WHERE fmldb.choice.id = " + choice.getChoiceId();
        try {
            conn = DBWrapper.getConnection();
            conn.prepareStatement(PS);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(conn);
            close(preparedStatement);
        }
    }
/*  // VALIDERING KAN SKE I CONTROLLER, BRUG getChoices OG TJEK HVILKEN SOM HAR ANSWER = TRUE
    public static Boolean validateChoice(Choice choice) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String PS = "";
        try {
            conn = DBWrapper.getConnection(DEFAULT_URL,DEFAULT_USERNAME,DEFAULT_PASSWORD);
            conn.prepareStatement(PS);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(conn);
            close(preparedStatement);
        }
    }
*/

    public static ArrayList<User> getUsers() {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        ArrayList<User> allUsers = new ArrayList<>();
        try {
            conn = DBWrapper.getConnection();
            preparedStatement = conn.prepareStatement("SELECT * FROM user");
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                User user = new User(rs.getInt("id"), rs.getString("userName"), rs.getString("password"), rs.getString("firstName"), rs.getString("lastName"), rs.getInt("type"), rs.getLong("createdTime"));
                allUsers.add(user);

            }
            return allUsers;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(conn);
            close(rs);
            close(preparedStatement);
        }
        return null;
    }

    public static ArrayList<Course> getCourses() throws IOException, ClassNotFoundException {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        ArrayList<Course> allCourses = new ArrayList<>();
        try {
            conn = DBWrapper.getConnection();
            preparedStatement = conn.prepareStatement("SELECT * FROM fmldb.course");
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Course course = new Course(rs.getInt(1), rs.getString(2));
                allCourses.add(course);
            }
            return allCourses;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
            close(rs);
            close(preparedStatement);
        }
        return null;
    }

    public static ArrayList<Quiz> getQuizzes(int courseId) throws IOException, ClassNotFoundException {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        ArrayList<Quiz> allQuizzes = new ArrayList<>();
        try {
            conn = DBWrapper.getConnection();
            preparedStatement = conn.prepareStatement("SELECT q.* FROM fmldb.quiz q INNER JOIN fmldb.course c ON q.course_id = c.id WHERE q.course_id =" + courseId + ";");
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Quiz quiz = new Quiz(rs.getInt(1), rs.getString(2), rs.getInt(3));
                allQuizzes.add(quiz);
            }
            return allQuizzes;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
            close(rs);
            close(preparedStatement);
        }
        return null;
    }


    public static ArrayList<Question> getQuestions(int quizId) throws IOException, ClassNotFoundException {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        ArrayList<Question> allQuestions = new ArrayList<>();
        try {
            conn = DBWrapper.getConnection();
            preparedStatement = conn.prepareStatement("SELECT q.* FROM fmldb.question q INNER JOIN fmldb.quiz qz ON q.quiz_id = qz.id WHERE q.quiz_id = " + quizId + ";");
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Question question = new Question(rs.getInt(3), rs.getString(2), rs.getInt(1));
                allQuestions.add(question);
            }
            return allQuestions;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
            close(rs);
            close(preparedStatement);
        }
        return null;
    }


    public static ArrayList<Choice> getChoices(int questionID) throws IOException {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        ArrayList<Choice> allChoices = new ArrayList<>();
        try {

            conn = DBWrapper.getConnection();
            preparedStatement = conn.prepareStatement("SELECT c.* FROM fmldb.choice c INNER JOIN fmldb.question q ON c.question_id = q.id WHERE c.question_id =" + questionID + ";");

            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Choice choice = new Choice(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4));
                allChoices.add(choice);
            }
            return allChoices;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(conn);
            close(rs);
            close(preparedStatement);
        }
        return null;
    }

    // Giver alle de fag som en bestemt bruger er tilmeldt
    public static ArrayList<Course> getUsersCourses(User user) throws IOException, ClassNotFoundException {
        Connection conn = null;
        ResultSet rs = null;
        String PS = "SELECT c.* FROM fmldb.user_course uc INNER JOIN fmldb.user u ON u.id = uc.user_id INNER JOIN fmldb.course c ON uc.course_id = c.id WHERE u.id =" + user.getUserId();
        PreparedStatement preparedStatement = null;
        ArrayList<Course> courseArrayList = new ArrayList<>();
        try {
            conn = DBWrapper.getConnection();
            preparedStatement = conn.prepareStatement(PS);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Course tempCourse = new Course(rs.getInt(1), rs.getString(2));
                courseArrayList.add(tempCourse);
            }
            return courseArrayList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
            close(rs);
            close(preparedStatement);
        }
        return null;
    }


    // Giver alle brugere som er tilmeldt et bestemt fag s
    public static ArrayList<User> getCoursesUsers(Course course) throws IOException, ClassNotFoundException {
        Connection conn = null;
        ResultSet rs = null;
        String PS = "SELECT u.* FROM fmldb.user_course uc INNER JOIN fmldb.course c ON uc.course_id = c.id INNER JOIN fmldb.user u ON uc.user_id = u.id WHERE c.id =" + course.getCourseID();
        PreparedStatement preparedStatement = null;
        ArrayList<User> userArrayList = new ArrayList<>();
        try {
            conn = DBWrapper.getConnection();
            preparedStatement = conn.prepareStatement(PS);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                User tempUser = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getLong(7));
                userArrayList.add(tempUser);
            }
            return userArrayList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
            close(rs);
            close(preparedStatement);
        }
        return null;
    }
    
    public UserCourse enrollUser2Course(UserCourse userCourse) throws IllegalArgumentException, SQLException, IOException, ClassNotFoundException {
        Connection conn = DBWrapper.getConnection();
	
	   try {
	       PreparedStatement createUserCourse = conn.prepareStatement("INSERT INTO fmldb.user_course (user_id, course_id) VALUES (?,?)");
	       createUserCourse.setInt(1, userCourse.getUserId());
	       createUserCourse.setInt(2, userCourse.getCourseID());
	
	       int rowsAffected = createUserCourse.executeUpdate();
	       return userCourse;
	   } catch (SQLException e) {
	       e.printStackTrace();
	       close(conn);
	   }
	   return null;
	}
    
    public UserCourse deleteEnrollUser2Course(UserCourse userCourse) throws IllegalArgumentException, SQLException, IOException, ClassNotFoundException {
        Connection conn = DBWrapper.getConnection();
	
	   try {
	       PreparedStatement createUserCourse = conn.prepareStatement("DELETE FROM fmldb.user_course WHERE user_id = ? AND course_id = ?");
	       createUserCourse.setInt(1, userCourse.getUserId());
	       createUserCourse.setInt(2, userCourse.getCourseID());
	
	       int rowsAffected = createUserCourse.executeUpdate();
	       return userCourse;
	   } catch (SQLException e) {
	       e.printStackTrace();
	       close(conn);
	   }
	   return null;
	}

    public static void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
