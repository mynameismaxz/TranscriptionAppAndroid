package th.in.codia.transcriptionservice;
import android.os.AsyncTask;

import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Puri on 7/12/15 AD.
 */
public class Login extends AsyncTask<String,Void,String> {
    private String URL;

    public Login(String URL){
        this.URL = URL;
    }

    public String getLogin(String user,String pass) throws IOException {
        OkHttpClient ok = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("usern", user).add("pass", pass).build();
        Request request = new Request.Builder().url(this.URL).post(body).build();
        Response response = ok.newCall(request).execute();
        return response.isSuccessful()?response.body().string():"error";
    }

    @Override
    protected String doInBackground(String... params) {
        String re = null;
        try {
            re = getLogin(params[0], params[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return re;
    }
}