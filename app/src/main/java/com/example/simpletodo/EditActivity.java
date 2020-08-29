package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    Button btnSave;
    EditText etItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        btnSave = findViewById(R.id.btnSave);
        etItem = findViewById(R.id.etItem);

        getSupportActionBar().setTitle("Edit Item");
        etItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));
        // when users are done editing, they click the button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create an intent which contain the result
                Intent intent = new Intent();
                //pass the result
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, etItem.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
                //set the result
                setResult(RESULT_OK, intent);
                // finish the activity, and close the screen and go back
                finish();

            }
        });
    }
}