package mahsa.com.onlineresturauntbookingsystem.model;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by mahsa on 03/06/2017.
 */

public class MyApplication extends Application {

    public static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance=this;

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);



        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

    }

    public static synchronized MyApplication getInstance(){
        return mInstance;

    }

    public void setConnectivityListener(ConnectivityReceiever.ConnectivityRecieverListener listener) {
        ConnectivityReceiever.connectivityRecieverListener = listener;
    }


}


























