package mahsa.com.onlineresturauntbookingsystem.api;

import mahsa.com.onlineresturauntbookingsystem.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by mahsa on 27/06/2017.
 */

public interface UserApi {

    String USER_BASE_URL = "https://restaurant-booking-system.firebaseio.com/";

    @PUT("/Users/{new}.json")
    Call<User> saveUserData(@Path("new") String key, @Body User user);



}


