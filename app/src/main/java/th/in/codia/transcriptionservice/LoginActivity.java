package th.in.codia.transcriptionservice;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        String x = null;

        final EditText user = (EditText) findViewById(R.id
                .textUsername);
        final EditText pass = (EditText) findViewById(R.id.textPassword);
        final FrameLayout rootLayout = (FrameLayout) findViewById(R.id.rootLayout);

        Button Login = (Button) findViewById(R.id.buttonLogin);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = user.getText().toString();
                String passs = pass.getText().toString();
                Login loginn = new Login("http://" + getResources().getString(R.string.host_web) + "/"
                        + getResources().getString(R.string.sub_folder_without_slash)
                        + getResources().getString(R.string.chklogin_php));
                String result = null;
                String user = null;
                String status = null;
                try {
                    result = loginn.execute(username, passs).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (Objects.equals(result, "error")) {
                    Snackbar.make(rootLayout, "Something was wrong, Please try again", Snackbar.LENGTH_SHORT).show();
                }
                JSONObject Object;
                try {
                    Object = new JSONObject(result);
                    status = Object.getString("status");
                    user = Object.getString("user");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (Objects.equals(status, "ss")) {
                    // if user can login successful
                    //Snackbar.make(rootLayout,"Log in Success !",Snackbar.LENGTH_LONG).show();
                    Intent i = new Intent(LoginActivity.this, MainMenuActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("user", user);
                    i.putExtras(bundle);
                    startActivity(i);
                } else {
                    Snackbar.make(rootLayout, "Wrong Username or password, Please try again", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}