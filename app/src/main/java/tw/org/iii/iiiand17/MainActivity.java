package tw.org.iii.iiiand17;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {
    private MyReciver receiver;
    private SeekBar seekBar;//當進度條
//前景處理
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }else{
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        init();
    }

    private void init(){
        seekBar = findViewById(R.id.seekBar);
        receiver = new MyReciver();
        //拉動seekbar,互動
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    Intent intent = new Intent(MainActivity.this, MyService.class);
                    intent.putExtra("cmd","seekto");//傳遞前景至MyService處理
                    intent.putExtra("nowpos",progress);//傳遞前景至MyService處理
                    startService(intent);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    //背景service


    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver, new IntentFilter());
    }

    public void play(View view) {
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("cmd","play");//傳遞前景至MyService處理
        startService(intent);
    }

    public void pause(View view) { //service之間溝通
//        Intent intent = new Intent(this, MyService.class);
//        stopService(intent);
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("cmd","pause");//傳遞前景至MyService處理
        startService(intent);
    }

    public void reset(View view) {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }

    //廣播接收器
    private class MyReciver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int pos = intent.getIntExtra("now",1);
            int len = intent.getIntExtra("len",1);
            if(len>=0){
                seekBar.setMax(len);
            }else if(pos>=0){
                seekBar.setMax(pos);
            }
        }
    }
}
