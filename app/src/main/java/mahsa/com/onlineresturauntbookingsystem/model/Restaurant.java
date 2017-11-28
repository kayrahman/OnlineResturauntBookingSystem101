package mahsa.com.onlineresturauntbookingsystem.model;

/**
 * Created by Mahsa on 06/05/2017.
 */

public class Restaurant {

    private String image;
    private String title;
    private String location;
    private String hours;
    private String cuisine;
    private String description;
    private String dress_code;
    private double latitude;
    private double longtitude;
    private String parking;
    private String payment_options;
    private String phone;
    private String price;



    private String manager_key;

    public Restaurant() {
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDress_code() {
        return dress_code;
    }

    public void setDress_code(String dress_code) {
        this.dress_code = dress_code;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getPayment_options() {
        return payment_options;
    }

    public void setPayment_options(String payment_options) {
        this.payment_options = payment_options;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getManager_key() {
        return manager_key;
    }

    public void setManager_key(String manager_key) {
        this.manager_key = manager_key;
    }
}
