package com.android.p05ps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ShowSong extends AppCompatActivity {
    ListView lv;
    SongAdapter adapter;
    ArrayList<Song> al;
    Button btnFilter;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showsong);

        lv = (ListView) this.findViewById(R.id.lv);
        btnFilter = findViewById(R.id.btnFilter);
        spinner = findViewById(R.id.dynamic_spinner);
        DBHelper dbh = new DBHelper(this);
        final ArrayList<Song> songs = dbh.getAllSong();

        adapter = new SongAdapter(this, R.layout.row, songs);
        lv.setAdapter(adapter);
        al = new ArrayList<Song>();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Song data = songs.get(position);
                Intent i = new Intent(ShowSong.this,
                        Modify.class);
                i.putExtra("data", data);
                startActivityForResult(i, 9);
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper dbh = new DBHelper(ShowSong.this);
                al.clear();
                al.addAll(dbh.getAllSong("5"));
                dbh.close();
                adapter.notifyDataSetChanged();
            }
        });

        final ArrayList<String> alYear = new ArrayList<String>();
        for(int i = 0; i<al.size(); i ++){
            alYear.add(al.get(i).getYear()+"");
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,alYear);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spinner.getSelectedItem().toString();
                DBHelper dbh = new DBHelper(ShowSong.this);
                al.clear();
                al.addAll(dbh.getAllSong(selected));
                dbh.close();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 9) {
            DBHelper dbh = new DBHelper(ShowSong.this);
            al.clear();
            al.addAll(dbh.getAllSong());
            dbh.close();
            adapter = new SongAdapter(this, R.layout.row, al);
            lv.setAdapter(adapter);
        }

    }
}