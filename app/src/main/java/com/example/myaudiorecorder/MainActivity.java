package com.example.myaudiorecorder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.myaudiorecorder.preferences.PreferencesActivity;
import com.example.myaudiorecorder.preferences.PreferencesFragment;
import com.example.myaudiorecorder.presenter.MyPresenter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyPresenter.MyView {

    public static String FOLDER_NAME = "";
    private boolean flag_perm = false;
    private Button rec_button;
    private String[] perm_arr;
    private Button save_button;
    private MyPresenter mPresenter;
    private LinearLayout mLayout;
    private String color;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        check_flag();
        if (flag_perm && isExternalStorageWritable()) {
            FOLDER_NAME = getPackageName();
            setContentView(R.layout.activity_main);
            mPresenter = new MyPresenter(this);
            rec_button = findViewById(R.id.record_button);
            save_button = findViewById(R.id.save_button);
            rec_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    switch (v.getId())
//                    {
//                        case (R.id.record_button):

                    if (rec_button.getTag() == getString(R.string.rec_stop))//запись не осуществляется
                    {
                        rec_button.setTag(getString(R.string.rec_start));//запись началась
                        rec_button.setBackground(getDrawable(R.drawable.ic_crop_square));
                        startService();
                    }//сервис стартует
                    else if (rec_button.getTag() == getString(R.string.rec_start)) {
                        rec_button.setTag(getString(R.string.rec_resume));//запись приостановлена
                        rec_button.setBackground(getDrawable(R.drawable.ic_play_arrow));
                        pauseService();//запись приостанавливается
                    } else if (rec_button.getTag() == getString(R.string.rec_resume)) {// обновить фрагмент
                        rec_button.setTag(getString(R.string.rec_start));
                        rec_button.setBackground(getDrawable(R.drawable.ic_crop_square));
                        resumeService();//сервис возобновляется
                    }
//                        break;

                }
//                }
            });
            save_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    case (R.id.save_button):
                    stopService();
                    mPresenter.updateList();

//                            .beginTransaction()
//                            .replace(R.id.listFragment, new SimpleListFragment())
//                            .commit();


                    rec_button.setTag(getString(R.string.rec_stop));
                    rec_button.setBackground(getDrawable(R.drawable.ic_mic_none_black));
//                    break;
                }
            });


        } else
            exit();

        mLayout = findViewById(R.id.main_layout);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        color = preferences.getString(
                getString(R.string.background_key),
                getString(R.string.pref_colors_default_value)
        );
        switch (color) {
            case "0":
                mLayout.setBackgroundColor(getColor(R.color.Purple));
                break;
            case "1":
                mLayout.setBackgroundColor(getColor(R.color.Yellow));
                break;
            case "2":
                mLayout.setBackgroundColor(getColor(R.color.Green));
                break;
        }
//        getResources(getColor(R.color.Purple))
    }

    private void exit() {
        finish();
        System.exit(0);
    }

    private void checkPermissions(int permissionIndex) {
//        String[] array_perm=_requestPermissions.toArray();
        if (permissionIndex >= perm_arr.length) {
            flag_perm = true;
        } else {
            this.askForPermission(perm_arr[permissionIndex], permissionIndex);
        }
    }


    private void askForPermission(String permission, int permissionIndex) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{permission},
                    permissionIndex //permissionIndex will become the requestCode on callback
            );
        } else {
            this.checkPermissions(permissionIndex + 1); //check the next permission
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        this.checkPermissions(requestCode + 1); //check the next permission
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//    private void onClick(View v) {
//        switch (v.getId()) {
//            case (R.id.record_button):
//
//                if (rec_button.getTag() == getString(R.string.rec_stop))//запись не осуществляется
//                {
//                    rec_button.setTag(getString(R.string.rec_start));//запись началась
//                    rec_button.setBackground(getDrawable(R.drawable.ic_crop_square));
//                    startService();
//                }//сервис стартует
//                else if (rec_button.getTag() == getString(R.string.rec_start)) {
//                    rec_button.setTag(getString(R.string.rec_resume));//запись приостановлена
//                    rec_button.setBackground(getDrawable(R.drawable.ic_play_arrow));
//                    pauseService();//запись приостанавливается
//                } else if (rec_button.getTag() == getString(R.string.rec_resume)) {// обновить фрагмент
//                    rec_button.setTag(getString(R.string.rec_start));
//                    rec_button.setBackground(getDrawable(R.drawable.ic_crop_square));
//                    resumeService();//сервис возобновляется
//                }
//                break;
//            case (R.id.save_button):
//                stopService();
//                mPresenter.updateList();
//                rec_button.setTag(getString(R.string.rec_stop));
//                rec_button.setBackground(getDrawable(R.drawable.ic_mic_none_black));
//                break;
//        }
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_settings):
                Intent intent = new Intent(this, PreferencesActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startService() {
        Intent intent = new Intent(this, AudioRecorder.class);
        intent.setAction(AudioRecorder.ACTION_START);
        startService(intent);
    }

    private void stopService() {
        Intent intent = new Intent(this, AudioRecorder.class);
        intent.setAction(AudioRecorder.ACTION_STOP);
        startService(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void pauseService() {
        Intent intent = new Intent(this, AudioRecorder.class);
        intent.setAction(AudioRecorder.ACTION_PAUSE);
        startForegroundService(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void resumeService() {
        Intent intent = new Intent(this, AudioRecorder.class);
        intent.setAction(AudioRecorder.ACTION_RESUME);
        startForegroundService(intent);
    }

    private void check_flag() {
        List<String> _requestPermissions = new ArrayList<>();
        _requestPermissions.add(Manifest.permission.RECORD_AUDIO);
        _requestPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        perm_arr = _requestPermissions.toArray(new String[0]);
        checkPermissions(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

//    @Override
//    public void onAttachFragment(Fragment fragment) {
//        super.onAttachFragment(fragment);
//    }

//    @Override
//    public void onFragmentInteraction(String link) {
//        SimpleListFragment simpleListFragment = (SimpleListFragment) (getSupportFragmentManager().findFragmentById(R.id.listFragment));
//        if (simpleListFragment != null && simpleListFragment.isInLayout()) {
//            simpleListFragment.setListAdapter(null);//okpjojoj
//        }
//    }

    @Override
    public void onUpdate() {
        getSupportFragmentManager().findFragmentById(R.id.listFragment).onAttach(getApplicationContext());
    }

//    @Override
//    public void onUpdate() {
//        String[] newArray = new String[]{"1"};
////        (SimpleListFragment)(getSupportFragmentManager().findFragmentById(R.id.listFragment))
//
//    }

}
