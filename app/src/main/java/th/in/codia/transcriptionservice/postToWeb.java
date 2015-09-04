package th.in.codia.transcriptionservice;

import android.os.AsyncTask;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by mynameismaxz on 8/6/15 AD.
 */
public class postToWeb extends AsyncTask<String,Void,Void> {

    private String postURL;

    // constructor
    public postToWeb(String Post){
        this.postURL = Post;
    }

    public void postWeb(String Mountpoint,String result) throws IOException {
        OkHttpClient ok = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("mountpoint", Mountpoint).add("text", result).build();
        Request request = new Request.Builder().url(this.postURL).post(body).build();
        Response response = ok.newCall(request).execute();
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            postWeb(params[0],params[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
