package com.example.lab4game;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.lab4game.room.AppDatabase;
import com.example.lab4game.room.ResultEntity;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import com.example.lab4game.databinding.ActivityResultBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    LinkedList<ResultEntity> results = new LinkedList<>();
    Button restart;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.getDatabase().getReference().child("scores").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Firebase", "Got data from Firebase");
                task.getResult().getChildren().forEach(child -> {
                    Log.d("Firebase", "Got data from Firebase");
                    ResultEntity a = child.getValue(ResultEntity.class);
                    results.add(a);
                    Log.d("Firebase", "Results len: " + results.size());
                });
            } else {
                Log.d("Firebase", "Failed to get data from Firebase");
            }
            output();
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        output();
        this.restart = (Button) findViewById(R.id.btn_restart);

    }

    private void output() {
        TableLayout table = (TableLayout) findViewById(R.id.tableLayout);
        for (ResultEntity res : results) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
            TextView text_score = new TextView(this);
            TextView text_date = new TextView(this);
            TextView text_gametime = new TextView(this);
            text_score.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            text_date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            text_gametime.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            text_score.setText(Integer.toString(res.points));
            text_date.setText(new Date(res.date).toString());
            text_gametime.setText(Long.toString(res.game_time));

            tableRow.addView(text_score);
            tableRow.addView(text_date);
            tableRow.addView(text_gametime);
            table.addView(tableRow);


        }
    }

    public void onMyButtonClick(View view) {
        finish();
    }

}

