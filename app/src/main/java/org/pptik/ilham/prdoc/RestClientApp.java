package org.pptik.ilham.prdoc;
/**
 * Created by Ilham on 4/11/17.
 * emilhamep@icloud.com
 * PPTIK Intitut Teknologi Bandung
 * Kelas ini digunakan untuk URL statis API yang digunakan dalam aplikasi
 */
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.*;

public class RestClientApp {
    private static final String BASE_URL = "http://10.0.3.2/prd-api/public/";
    //private static final String BASE_URL = "http://192.168.8.100/prd-api/public/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }



    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }


}

