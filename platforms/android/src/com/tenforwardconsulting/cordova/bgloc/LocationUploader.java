package com.tenforwardconsulting.cordova.bgloc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.marianhello.cordova.bgloc.Constant;

import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by dhavalnagar on 01/03/16.
 */
public class LocationUploader extends BroadcastReceiver{

    public static final String TAG = "LocationUploader";

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle results = getResultExtras(true);
        Bundle data = intent.getExtras();
        switch (data.getInt(Constant.ACTION)) {
            case Constant.ACTION_LOCATION_UPDATE:
                Log.d(TAG, "Sending location update to server " + data.toString());
                String jsonString = data.getString(Constant.DATA);
                String url = data.getString("url");
                if(url == null){
                    return;
                }

                String method = data.getString("method");
                HashMap<String, String> headers = (HashMap<String, String>) data.getSerializable("headers");
                HashMap<String, String> params = (HashMap<String, String>) data.getSerializable("params");

                try {
                    JSONObject jsonObj = new JSONObject(jsonString);
                    if(params != null && params.size() > 0){
                        for(Map.Entry<String, String> param:  params.entrySet()){
                            jsonObj.put(param.getKey(), param.getValue());
                        }
                        jsonString = jsonObj.toString();
                    }

                    OkHttpClient client = new OkHttpClient();

                    RequestBody body = RequestBody.create(JSON, jsonString);
                    Request.Builder builder = new Request.Builder().url(url);
                    if(method.equals("POST")){
                        builder = builder.post(body);
                    }else if(method.equals("GET")){
                        builder = builder.get();
                    }
                    if(headers != null && headers.size() > 0){
                        for(Map.Entry<String, String> header:  headers.entrySet()){
                            builder.addHeader(header.getKey(), header.getValue());
                        }
                    }

                    Request request = builder.build();
                    Call call = client.newCall(request);
                    //Log.i(TAG, "Location server response " + response.body().string());
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e(TAG, "Error while uploading location " + e.getMessage());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.i(TAG, "Location server response " + response.body().string());
                        }
                    });
                }catch(Exception e){
                    e.printStackTrace();
                }

                break;
            default:
                break;
        }
    }

//    interface Listener {
//        void onResult(String result);
//    }
//
//    public class AsyncHttpPost extends AsyncTask<String, String, String> {
//
//        private Listener mListener;
//        private HashMap<String, String> mData = null;// post data
//
//        /**
//         * constructor
//         */
//        public AsyncHttpPost(HashMap<String, String> data) {
//            mData = data;
//        }
//        public void setListener(Listener listener) {
//            mListener = listener;
//        }
//
//        /**
//         * background
//         */
//        @Override
//        protected String doInBackground(String... params) {
//            byte[] result = null;
//            String str = "";
//            HttpClient client = new DefaultHttpClient();
//            HttpPost post = new HttpPost(params[0]);// in this case, params[0] is URL
//            try {
//                // set up post data
//                ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
//                Iterator<String> it = mData.keySet().iterator();
//                while (it.hasNext()) {
//                    String key = it.next();
//                    nameValuePair.add(new BasicNameValuePair(key, mData.get(key)));
//                }
//
//                post.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
//                HttpResponse response = client.execute(post);
//                StatusLine statusLine = response.getStatusLine();
//                if(statusLine.getStatusCode() == HttpURLConnection.HTTP_OK){
//                    result = EntityUtils.toByteArray(response.getEntity());
//                    str = new String(result, "UTF-8");
//                }
//            }
//            catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            catch (Exception e) {
//            }
//            return str;
//        }
//
//        /**
//         * on getting result
//         */
//        @Override
//        protected void onPostExecute(String result) {
//            // something...
//            if (mListener != null) {
//                mListener.onResult(result);
//            }
//        }
//    }
}
