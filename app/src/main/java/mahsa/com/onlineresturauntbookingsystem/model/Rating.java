package mahsa.com.onlineresturauntbookingsystem.model;

/**
 * Created by rating on 06/06/2017.
 */

public class Rating {

    String username;
    String rating;
    String description;
    String image;



    public Rating(String username, String rating, String description,String image) {

        this.username = username;
        this.rating = rating;
        this.description = description;
        this.image = image;
    }


    public Rating() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Rating(String rating, String desc) {
        this.rating = rating;
        this.description = desc;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;

    }

    public void setRating(String rating) {
        this.rating = rating;
    }


}
