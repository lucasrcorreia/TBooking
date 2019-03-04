package rolu18oy.ju.se.layoutapp.Model;

public class Restaurant {
    private  String RestaurantEmail;
    private  String RestaurantName;
    private  String Password;
    private  String Description;

    public Restaurant(){

    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public Restaurant(String restaurantEmail, String restaurantName, String password, String description){
        this.RestaurantEmail = restaurantEmail;
        this.RestaurantName = restaurantName;
        this.Password = password;
        this.Description = description;
    }
    public String getRestaurantEmail() {
        return RestaurantEmail;
    }

    public void setRestaurantEmail(String restaurantEmail) {
        RestaurantEmail = restaurantEmail;
    }

    public String getRestaurantName() {
        return RestaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        RestaurantName = restaurantName;
    }


    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }


}
