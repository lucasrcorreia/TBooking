package rolu18oy.ju.se.layoutapp.Model;

public class RestaurantTable {

    RestaurantTable(){
    }

    String tableId;
    int numberOfPeople;
    String restaurantEmail;

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getRestaurantEmail() {
        return restaurantEmail;
    }

    public void setRestaurantEmail(String restaurantEmail) {
        this.restaurantEmail = restaurantEmail;
    }

    public RestaurantTable(String tableid, int numberofpeople, String restaurantemail){
        this.tableId  = tableid;
        this.numberOfPeople = numberofpeople;
        this.restaurantEmail = restaurantemail;
    }
}
