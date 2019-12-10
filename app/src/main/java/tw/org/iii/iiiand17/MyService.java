package tw.org.iii.iiiand17;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
//播放音樂檔處理
public class MyService extends Service {
    private MediaPlayer mediaPlayer;
    private File sdroot;

    //綁定型服務, 必須override; 但我們這邊不做綁定型的
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    //啟動後 onCreate只會啟動一次
    @Override
    public void onCreate() {
        super.onCreate();
        try{
            sdroot = Environment.getDataDirectory();//取得路徑
            //https://developer.android.com/reference/android/media/MediaPlayer
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(sdroot.getAbsolutePath() + "/s1.mp3");
            mediaPlayer.prepare();
        }catch (Exception e){
            Log.v("brad",e.toString());
        }
//        Log.v("brad", "onCreate");
    }

    //啟動後都在此運動,結束後才會走onCreate
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.v("brad", "onStartCommand");

        String cmd = intent.getStringExtra("cmd");
        if(mediaPlayer.isPlaying() &&  (cmd.equals("pause"))){
            mediaPlayer.pause();
        }else if(!mediaPlayer.isPlaying() && (cmd.equals("play"))) {
            mediaPlayer.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null){
            if(mediaPlayer.isPlaying()) mediaPlayer.stop();
            mediaPlayer.release();
        }
//        Log.v("brad", "onDestroy");
    }
}
