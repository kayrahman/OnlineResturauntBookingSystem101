package mahsa.com.onlineresturauntbookingsystem.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sina on 27/06/2017.
 */

public class RetrofeitService {

    public static UserApi postUserData(){

        return new Retrofit.Builder()
                .baseUrl(UserApi.USER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UserApi.class);
    }


}
