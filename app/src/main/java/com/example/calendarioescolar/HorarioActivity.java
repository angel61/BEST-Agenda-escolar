package com.example.calendarioescolar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.TimetableView;

import java.util.ArrayList;
import java.util.Calendar;

public class HorarioActivity extends AppCompatActivity {
    private Context context;
    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_EDIT = 2;

    private Toolbar toolbar;
    private TimetableView timetable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.horario_layout);

        init();
    }

    private void init(){
        this.context = this;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        timetable = findViewById(R.id.timetable);
        Calendar cal= Calendar.getInstance();
        if(cal.get(Calendar.DAY_OF_WEEK)!=0&&cal.get(Calendar.DAY_OF_WEEK)!=6)timetable.setHeaderHighlight(cal.get(Calendar.DAY_OF_WEEK)-1);
        initView();
    }

    private void initView(){

        timetable.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {
            @Override
            public void OnStickerSelected(int idx, ArrayList<Schedule> schedules) {
                Intent i = new Intent(context, EditarHorarioActivity.class);
                i.putExtra("mode",REQUEST_EDIT);
                i.putExtra("idx", idx);
                i.putExtra("schedules", schedules);
                startActivityForResult(i,REQUEST_EDIT);
            }
        });

        loadSavedData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
                Intent i = new Intent(this,EditActivity.class);
                i.putExtra("mode",REQUEST_ADD);
                startActivityForResult(i,REQUEST_ADD);
                //timetable.removeAll();
                //saveByPreference(timetable.createSaveData());
                //loadSavedData();
            return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ADD:
                if (resultCode == EditarHorarioActivity.RESULT_OK_ADD) {
                    ArrayList<Schedule> item = (ArrayList<Schedule>) data.getSerializableExtra("schedules");
                    timetable.add(item);
                    saveByPreference(timetable.createSaveData());
                }
                break;
            case REQUEST_EDIT:
                if (resultCode == EditarHorarioActivity.RESULT_OK_EDIT) {
                    int idx = data.getIntExtra("idx", -1);
                    ArrayList<Schedule> item = (ArrayList<Schedule>) data.getSerializableExtra("schedules");
                    timetable.edit(idx, item);
                    saveByPreference(timetable.createSaveData());
                } else if (resultCode == EditarHorarioActivity.RESULT_OK_DELETE) {
                    int idx = data.getIntExtra("idx", -1);
                    timetable.remove(idx);
                }
                break;
        }
    }


    private void saveByPreference(String data){
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("timetable_demo",data);
        editor.commit();
        //Toast.makeText(this,"saved!",Toast.LENGTH_SHORT).show();
    }


    private void loadSavedData(){
        timetable.removeAll();
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this);
        String savedData = mPref.getString("timetable_demo","");
        if(savedData == null && savedData.equals("")) return;
        timetable.load(savedData);
        //Toast.makeText(this,"loaded!",Toast.LENGTH_SHORT).show();
    }
}
