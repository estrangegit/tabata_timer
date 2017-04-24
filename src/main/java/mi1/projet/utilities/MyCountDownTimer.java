package mi1.projet.utilities;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MyCountDownTimer {
    private long millisInFuture;
    private long countDownInterval;
    private boolean status;
    private boolean onRun;

    public MyCountDownTimer(long pMillisInFuture, long pCountDownInterval) {
        this.millisInFuture = pMillisInFuture;
        this.countDownInterval = pCountDownInterval;
        status = false;
        onRun = true;
        Initialize();
    }

    public long getCurrentTime() {
        return millisInFuture;
    }

    public void Start() {
        status = true;
        onRun = true;
    }

    public void Stop() {
        status = false;
        onRun = true;
    }

    public void kill(){
        status = true;
        onRun = false;
    }


    public void Initialize()
    {
        final Handler handler = new Handler();
        Log.v("status", "starting");
        final Runnable counter = new Runnable(){

            public void run() {
                long sec = millisInFuture / 1000;
                if (onRun) {
                    if (status) {
                        if (millisInFuture <= 0) {
                            Log.v("status", "done");
                        } else {
                            Log.v("status", Long.toString(sec) + " seconds remain");
                            millisInFuture -= countDownInterval;
                            handler.postDelayed(this, countDownInterval);
                        }
                    } else {
                        Log.v("status", Long.toString(sec) + " seconds remain and timer has stopped!");
                        handler.postDelayed(this, countDownInterval);
                    }
                }
                else{
                    handler.removeCallbacksAndMessages(this);
                }
            }
        };
        handler.postDelayed(counter, countDownInterval);
    }
}