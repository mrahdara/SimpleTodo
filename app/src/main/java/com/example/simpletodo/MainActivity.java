package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.apache.commons.io.FileUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;
    List<String> items;
    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);


        loadItems();
        ItemsAdapter.OnClickListener onClickListener =  new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("ManActivity", "single clock on position: " + position);

                // create new axtivity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                // pass the data being edited
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                // display the activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };
        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){

            @Override
            public void onItemLongClicked(int position) {
                // delete the item from the model
                items.remove(position);
                // notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();

            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todoItem = etItem.getText().toString();

                // add item to the model
                items.add(todoItem);
                // notify adapter that an item is inserted
                itemsAdapter.notifyItemInserted(items.size()-1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    // this function will load items by reading every line of data file
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivitiy", "Error writing items", e);
            items = new ArrayList<>();
        }

    }



    // handle the result of EditActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE ){
            // retrieve updated value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            // extract position
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            Log.d("MainActivity", itemText);
            //update the model
            items.set(position, itemText);
            // notify adapter
            itemsAdapter.notifyItemChanged(position);
            // persist the data
            saveItems();
            Toast.makeText(getApplicationContext(), "the item at poistion " + position + " is updated", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.w("MainActivity", " the result of change in EditActivity has problem");

        }
    }

    // this function saves items by writing them into the data file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        }
        catch (IOException e){
            Log.e("MainActivitiy", "Error writing items", e);
        }
    }
}

