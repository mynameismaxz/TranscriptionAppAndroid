package th.in.codia.transcriptionservice;

import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.nuance.nmdp.speechkit.Prompt;
import com.nuance.nmdp.speechkit.Recognition;
import com.nuance.nmdp.speechkit.Recognizer;
import com.nuance.nmdp.speechkit.SpeechError;
import com.nuance.nmdp.speechkit.SpeechKit;

/**
 * Created by Puri on 8/6/15 AD.
 */

public class ASR  {
    public static SpeechKit _speechKit;


    static SpeechKit getSpeechKit() {
        return _speechKit;
    }


    public static final int LISTENING_DIALOG = 0;
    public static Handler _handler = null;
    public static Recognizer.Listener _listener;
    public static Recognizer _currentRecognizer1 , _currentRecognizer2;
    public static ArrayAdapter<String> _arrayAdapter;
    public static boolean _destroyed;



    public class SavedState{
        boolean DialogRecording;
        Recognizer Recognizer;
        Handler Handler;
    }

}
