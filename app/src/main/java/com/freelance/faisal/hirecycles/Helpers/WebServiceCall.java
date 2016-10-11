package com.freelance.faisal.hirecycles.Helpers;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Faisal on 8/31/16.
 */
public class WebServiceCall {
    private OnResultReceived mListener;
    private static OkHttpClient client;
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    public WebServiceCall(OnResultReceived listner){
        this.mListener =listner;
        if(client == null)
            client = new OkHttpClient();
    }

    public void asyncGet(String url, String access_token){

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", access_token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(mListener !=null) mListener.onResult(response.body().string());
            }

        });
    }
    public void asyncPost (String url, String postBody) throws Exception{

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
                .build();

//        Response response = client.newCall(request).execute();
//        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//        if(mListener !=null) mListener.onResult(response.body().string());
        //System.out.println(response.body().string());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(mListener !=null) mListener.onResult(response.body().string());
            }

        });

    }
//
//    @Override protected String doInBackground(Void... params) {
//        //DO YOUR STUFF
//        String data="Test";
//        return data;
//    }
//
//    @Override protected void onPostExecute(String result) {
//        if(mListener !=null) mListener.onResult(result);
//    }

    public interface OnResultReceived{
        public void onResult(String result);
    }
}
