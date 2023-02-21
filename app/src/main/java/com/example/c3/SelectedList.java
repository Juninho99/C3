package com.example.c3;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SelectedList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_list);

        String imeListe = getIntent().getStringExtra("listName");

        TextView textView = findViewById(R.id.textView);
        textView.setText(imeListe);
    }
    public void dodajItem(View view) {
    }
}
