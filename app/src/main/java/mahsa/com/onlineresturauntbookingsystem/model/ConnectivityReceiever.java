package mahsa.com.onlineresturauntbookingsystem.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by on 03/06/2017.
 */

public class ConnectivityReceiever extends BroadcastReceiver {


    public interface ConnectivityRecieverListener{
        void onNetworkConnectionChanged(boolean isConnected);
    }

    public static ConnectivityRecieverListener connectivityRecieverListener;

    public ConnectivityReceiever(){
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(connectivityRecieverListener!=null){
            connectivityRecieverListener.onNetworkConnectionChanged(isConnected);
        }
    }


    public static boolean isConnected(){

        ConnectivityManager cm =
                (ConnectivityManager)MyApplication.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork= cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }


}



























