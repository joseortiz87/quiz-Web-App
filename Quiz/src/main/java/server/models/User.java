package server.models;


import server.security.Digester;

public class User {

    private int userId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private int type;
    private long createdTime;




    public User(int userId, String username, String password, String firstName, String lastName, int type, Long createdTime) {

        this.userId = userId;
        this.username = username;
        this.createdTime = createdTime;
        this.password = Digester.hashWithSalt(password, username, createdTime);
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
    }

    public User() {

    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int id) {
        this.userId = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = Digester.hash(password);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime() {
        this.createdTime = System.currentTimeMillis() / 1000L;
    }
    public void setCreatedTime(long time) {
        this.createdTime = time;
    }
}

