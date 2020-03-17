package com.devinmartinolich.basemvvm.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.devinmartinolich.basemvvm.R;

/**
 * Name: NetworkUtils
 * Created by Devin Martinolich on 1/24/18.
 * Modified by
 * Purpose: Contains utility methods related to network connections
 */
public class NetworkUtils
{
    private static final String TAG = "NetworkUtils";

    /**
     * Name: isNetworkAvailable
     * Created by Devin Martinolich on 1/24/18.
     * Modified by
     * Purpose: This method is used to check internet connectivity of device.
     *          This method returns true if connectivity exist else returns
     *          false
     */
    public static boolean isNetworkAvailable(Context context)
    {
        AppLog.d(TAG, "-> isNetworkAvailable()");
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected())
            return true;

        return false;
    }

    /**
     * Name: getErrorMessageByHttpCode
     * Created by Devin Martinolich on 1/24/18.
     * Modified by
     * Purpose: To get error message based on http response code
     *
     * @param aContext  app context.
     * @param aHttpCode Http code to get relevant error message
     */
    public static String getErrorMessageByHttpCode(Context aContext, int aHttpCode)
    {
        AppLog.d(TAG, "-> getErrorMessageByHttpCode()");
        if (aHttpCode == 408)
            return aContext.getString(R.string.error_network_connection_timeout);

        else if (aHttpCode == 401 || aHttpCode == 407)
            return aContext.getString(R.string.error_network_unauthorized);

        else if (aHttpCode == 440)
            return aContext.getString(R.string.error_network_session_expire);

            // client error
        else if (aHttpCode > 200 && aHttpCode < 500)
            return aContext.getString(R.string.error_network_client_error);

        else if (aHttpCode == 504 || aHttpCode == 598 || aHttpCode == 599)
            return aContext.getString(R.string.error_network_server_timeout);

            // server error
        else if (aHttpCode >= 500)
            return aContext.getString(R.string.error_network_server_errors);

        else
            return aContext.getString(R.string.error_network_client_error);
    }
}
