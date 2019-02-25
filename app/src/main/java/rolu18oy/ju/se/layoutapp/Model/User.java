package rolu18oy.ju.se.layoutapp.Model;

public class User {

    private  String email;
    private  String password;
    private  String FirstName;
    private  String LastName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public User(){
    }

    public User(String firstname,String lastname, String email, String password){
        this.password = password;
        this.email = email;
        this.FirstName = firstname;
        this.LastName = lastname;
    }

}