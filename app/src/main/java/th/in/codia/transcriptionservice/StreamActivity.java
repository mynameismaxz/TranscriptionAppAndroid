package th.in.codia.transcriptionservice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioRecord;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.nuance.nmdp.speechkit.Recognition;
import com.nuance.nmdp.speechkit.Recognizer;
import com.nuance.nmdp.speechkit.SpeechError;
import com.nuance.nmdp.speechkit.SpeechKit;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by mynameismaxz on 8/5/15 AD.
 */
public class StreamActivity extends AppCompatActivity{

    private String Topic;
    private String lang = "th_TH"; // en_US
    public static final String TAG = "TS";
    public static String send="";
    private boolean isASR = false;
    public int stage = 0;
    public static String finalString;
    private int count = 0;
    public static int isStream =0;
    private Context context;

    private String easr="Please retry your query.\n" +
            "Sorry, speech not recognized. Please try again.";

    private String eoc = "An error occurred.\n";

    public StreamActivity(){
        super();
        ASR._listener = createListener();

        ASR._currentRecognizer1 = null;
        ASR._currentRecognizer2 = null;
        ASR._destroyed = true;
    }
    public class printHandler {
        public void print( String message )
        {
            Log.v(TAG, message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        this.Topic = bundle.getString("topic");

        TextView textTopic = (TextView)findViewById(R.id.textTopic);
        textTopic.setText(this.Topic);
        final EditText StatusMes = (EditText)findViewById(R.id.editStatus);
        StatusMes.setText("");

        final Switch isAD = (Switch)findViewById(R.id.switchASR);
        isAD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isASR = isChecked?true:false;
            }
        });

        ASR._speechKit = (SpeechKit) getLastNonConfigurationInstance();
        if (ASR._speechKit == null) {
            ASR._speechKit = SpeechKit.initialize(this.getApplicationContext(), AppInfo.SpeechKitAppId, AppInfo.SpeechKitServer, AppInfo.SpeechKitPort, AppInfo.SpeechKitSsl, AppInfo.SpeechKitApplicationKey);
            ASR._speechKit.connect();
            ASR._speechKit.setDefaultRecognizerPrompts(/*beep*/null, /*Prompt.vibration(100)*/null, null, null);
        }
        ASR._destroyed = false;

        ASR.SavedState savedState = (ASR.SavedState)getLastNonConfigurationInstance();
        if (savedState == null)
        {
            // Initialize the handler, for access to this application's message queue
            ASR._handler = new Handler();
        } else
        {
            ASR._currentRecognizer1 = savedState.Recognizer;

            ASR._currentRecognizer2 = savedState.Recognizer;
            ASR._handler = savedState.Handler;

            if (savedState.DialogRecording)
            {
                // Simulate onRecordingBegin() to start animation
                ASR._listener.onRecordingBegin(ASR._currentRecognizer1);
                ASR._listener.onRecordingBegin(ASR._currentRecognizer2);
            }

            ASR._currentRecognizer1.setListener(ASR._listener);
            ASR._currentRecognizer2.setListener(ASR._listener);
        }

        Button streamBut = (Button)findViewById(R.id.buttonStream);
        streamBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStream==0){
                    isStream = 1;
                    isAD.setClickable(false);
                    if(isASR){
                        stage=1;
                        count=0;
                        StatusMes.setText("");
                        startASR1();
                    }
                    else{
                        isStream = 0;
//                        Intent i = new Intent(MainMenuActivity.this, StreamActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("topic", topics.get(position));
//                        i.putExtras(bundle);
//                        startActivity(i);
                        Intent i = new Intent(StreamActivity.this,WebRTCActivity.class);
                        Bundle newBundle = new Bundle();
                        newBundle.putString("topic", Topic);
                        i.putExtras(newBundle);
                        startActivity(i);
                    }
                }
                else if(isStream ==1){
                    isStream=0;
                    isAD.setClickable(true);
                    if(isASR){
                        stopRec();
                        send="";
                    }
                }
            }
        });
    }
private static final ScheduledExecutorService a1 = Executors.newSingleThreadScheduledExecutor();

    private void startASR1(){
        ASR._currentRecognizer1 = ASR.getSpeechKit().createRecognizer(Recognizer.RecognizerType.Dictation, Recognizer.EndOfSpeechDetection.None, this.lang, ASR._listener, ASR._handler);
        if(stage!=0){
            stage = 1;
            ASR._currentRecognizer1.start();
            stopASR1();
        }
    }

    private void stopASR1(){
        Runnable asr1 = new Runnable(){
            public void run(){
                if(stage!=0){
                    stage =3;
                    ASR._currentRecognizer1.stopRecording();
                    delayASR2();
                }
            }
        };
        a1.schedule(asr1, 5,TimeUnit.SECONDS);
    }

    private void delayASR2(){
        Runnable dasr2 = new Runnable(){
            public void run(){
                if(stage!=0){
                    startASR2();
                }
            }
        };
        a1.schedule(dasr2, 2, TimeUnit.SECONDS);
    }

    private void startASR2(){
        ASR._currentRecognizer2 = ASR.getSpeechKit().createRecognizer(Recognizer.RecognizerType.Dictation, Recognizer.EndOfSpeechDetection.None, this.lang, ASR._listener, ASR._handler);
        if(stage!=0){
            stage = 2;
            ASR._currentRecognizer2.start();
            stopASR2();
        }
    }

    private void stopASR2(){
        Runnable asr2 = new Runnable(){
            public void run(){
                if(stage!=0){
                    stage = 3;
                    ASR._currentRecognizer2.stopRecording();
                    delayASR1();
                }
            }
        };
        a1.schedule(asr2, 5, TimeUnit.SECONDS);
    }

    private void delayASR1(){
        Runnable dasr1 = new Runnable(){
            public void run(){
                if(stage!=0){
                    startASR1();
                }
            }
        };
        a1.schedule(dasr1, 2, TimeUnit.SECONDS);
    }

    private void stopRec(){
        if (stage==1){
            ASR._currentRecognizer1.stopRecording();
        }
        else if(stage==2){
            ASR._currentRecognizer2.stopRecording();
        }
        stage = 0;
    }



    @Override
    public void onPause() {
        super.onPause();
    }


    /** Called when the activity is resumed. **/
    @Override
    public void onResume() {
        super.onResume();
        Log.v( TAG, "Caption Service resumed" );
    }

    //+++++++++++++++++++++++Nuance ++++++++++++++++++++++++++++++++++
    public Recognizer.Listener createListener()
    {
        return new Recognizer.Listener()
        {
            @Override
            public void onRecordingBegin(Recognizer recognizer)
            {
                Log.d("xxxxx", "Recording.....");
            }

            @Override
            public void onRecordingDone(Recognizer recognizer){}
            @Override
            public void onError(Recognizer recognizer, SpeechError error)

            {
                if (stage==1) {
                    if (recognizer != ASR._currentRecognizer1) return;
                    ASR._currentRecognizer1 = null;
                }
                else if(stage ==2){
                    if (recognizer != ASR._currentRecognizer2) return;
                    ASR._currentRecognizer2 = null;

                }
                String detail = error.getErrorDetail();
                String suggestion = error.getSuggestion();

                if (suggestion == null) suggestion = "";
                setResult(detail + "\n" + suggestion);
                android.util.Log.d("Nuance SampleVoiceApp", "Recognizer.Listener.onError: session id ["
                        + ASR.getSpeechKit().getSessionId() + "]");
            }

            @Override
            public void onResults(Recognizer recognizer, Recognition results) {
                if(stage==1)
                    ASR._currentRecognizer1 = null;
                else if (stage ==2)
                    ASR._currentRecognizer2 = null;
                int count = results.getResultCount();
                Recognition.Result [] rs = new Recognition.Result[count];
                for (int i = 0; i < count; i++)
                {
                    rs[i] = results.getResult(i);
                }
                setResults(rs);
                // for debugging purpose: printing out the speechkit session id
                android.util.Log.d("Nuance SampleVoiceApp", "Recognizer.Listener.onResults: session id ["
                        + ASR.getSpeechKit().getSessionId() + "]");
            }
        };
    }
    private void setResult(String result)
    {
        Log.d(TAG,String.valueOf(result.equals(easr)));
        Log.d(TAG+"c",easr);
        Log.d(TAG+"c",result);

        if(result.equals(easr) || result.equals(eoc)){
            result="";
        }
        finalString=result;
        send = send + result;

        EditText t = (EditText)findViewById(R.id.editStatus);
        if (t != null)
            t.setText(send);
        new postToWeb("http://" + getResources().getString(R.string.host_web) + "/"
                + getResources().getString(R.string.sub_folder_without_slash)
                + getResources().getString(R.string.asrpost_php))
                .execute(Topic, finalString + " ");
        //count to delete in dialog
        if (count==6){
            count=0;
            t.setText("");
            send="";
        }
    }

    private void setResults(Recognition.Result[] results)
    {
        //_arrayAdapter.clear();
        if (results.length > 0)
        {
            setResult(results[0].getText());

        }  else
        {
            setResult("");
        }
    }
}
