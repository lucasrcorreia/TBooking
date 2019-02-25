package rolu18oy.ju.se.layoutapp.Model;

public class User {

    private  String email;
    private  String password;


    public User(){

    }

    public User(String email, String password){
        this.password = password;
        this.email = email;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword() {
        this.password = password;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail() {
        this.email = email;
    }
}