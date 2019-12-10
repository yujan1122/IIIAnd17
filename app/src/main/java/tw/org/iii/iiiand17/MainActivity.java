package tw.org.iii.iiiand17;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
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

    }

    //背景service

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
}
