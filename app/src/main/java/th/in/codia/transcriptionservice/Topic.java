package th.in.codia.transcriptionservice;

import android.os.AsyncTask;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Puri on 8/3/15 AD.
 */
public class Topic extends AsyncTask<String,Void,String> {
    String URL;

    public Topic(String url){
        this.URL = url;
    }

    public String getTopic(String user) throws IOException {
        OkHttpClient ok = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("usern", user).build();
        Request request = new Request.Builder().url(this.URL).post(body).build();
        Response response = ok.newCall(request).execute();
        return response.isSuccessful()?response.body().string():"error";
    }

    @Override
    protected String doInBackground(String... params) {
        String get = null;
        try {
             get = this.getTopic(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return get;
    }
}
