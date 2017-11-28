package mahsa.com.onlineresturauntbookingsystem.model;

/**
 * Created by mahsa on 22/06/2017.
 */

public class User {


    private String name;
    private String address;
    private String email;
    private String phone;
    private String image;

    public User(String name, String address, String email, String phone, String image ) {
        this.image = image;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }

    public User() {

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}












