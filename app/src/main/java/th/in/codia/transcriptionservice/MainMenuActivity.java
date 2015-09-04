package th.in.codia.transcriptionservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Created by mynameismaxz on 7/13/15 AD.
 */
public class MainMenuActivity extends AppCompatActivity {

    private static int countTopic;
    private ArrayList<String> topics = new ArrayList<>();
    private ArrayList<String> timeStart = new ArrayList<>();
    private ArrayList<String> timeEnd = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        RecyclerView.LayoutManager mLayoutManager;
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        String name = bundle.getString("user");

        final FrameLayout rootLayout = (FrameLayout) findViewById(R.id.rootLayout);
        Snackbar.make(rootLayout, "Welcome user :" + name, Snackbar.LENGTH_LONG).show();

        RecyclerView re = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        re.setLayoutManager(mLayoutManager);
        try {
            re.setAdapter(new TitleAdapter(getMenu(name)));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        re.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Snackbar.make(rootLayout,topics.get(position),Snackbar.LENGTH_SHORT).show();
                Intent i = new Intent(MainMenuActivity.this, StreamActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("topic", topics.get(position));
                i.putExtras(bundle);
                startActivity(i);
            }
        }));
    }

    private ArrayList<MenuTitle> getMenu(String user) throws ExecutionException, InterruptedException {
        ArrayList<MenuTitle> list = new ArrayList<MenuTitle>();
        Topic topic = new Topic("http://" + getResources().getString(R.string.host_web) + "/"
                + getResources().getString(R.string.sub_folder_without_slash)
                + getResources().getString(R.string.gettopic_php));
        String get = topic.execute(user).get();
        int count;
        JSONObject Object;
        try {
            Object = new JSONObject(get);
            count = Object.getInt("count");
            this.countTopic = count;
            if (count > 0) {
                JSONArray jarray = (JSONArray) Object.get("data");
                for (int i = 0; i < count; i++) {
                    JSONObject d = jarray.getJSONObject(i);
                    this.topics.add(d.getString("topic"));
                    this.timeStart.add(d.getString("start"));
                    this.timeEnd.add(d.getString("end"));
                    this.dates.add(d.getString("date"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < this.countTopic; i++) {
            list.add(new MenuTitle(this.topics.get(i), user, this.timeStart.get(i), this.timeEnd.get(i), this.dates.get(i)));
        }
        return list;
    }

}

