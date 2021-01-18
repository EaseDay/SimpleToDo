package com.example.yizeprework;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> item;

    Button btnAdd;
    EditText edBar;
    RecyclerView rvItems;
    ItemAdapter itemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        edBar = findViewById(R.id.edBar);
        rvItems = findViewById(R.id.rvItems);

        loadItems();

        ItemAdapter.OnLongClickListener onLongClickListener = new ItemAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                //Delete the item.
                item.remove(position);
                //Notify the adapter in which position we deleted the item.
                itemAdapter.notifyItemRemoved(position);

                //Save the items.
                saveItems();
                //Notify the user.
                Toast.makeText(getApplicationContext(), "Item is removed", Toast.LENGTH_SHORT).show();
            }
        };

        ItemAdapter.OnClickListener onClickListener = new ItemAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single click at postion " + position);

                //Create the new activity.
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                //Pass the data being edited to the activity.
                i.putExtra(KEY_ITEM_TEXT, item.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                //Display the activity.
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };
        itemAdapter = new ItemAdapter(item, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Grab item from edit bar.
                String todoItem = edBar.getText().toString();

                //Add item to the model.
                item.add(todoItem);

                //Notify adapter that an item is inserted.
                itemAdapter.notifyItemInserted(item.size() - 1);

                //Erase the edit bar.
                edBar.setText("");

                //Save the items.
                saveItems();

                //Notify the user.
                Toast.makeText(getApplicationContext(), "Item is added", Toast.LENGTH_SHORT).show();
            }

        });

    }

    //Handle the result of the edit activity.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            //Retrieve the updated text value.
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            //Extract the original position of the edited item from the position key.
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            //Update the model at the right position with the new item.
            item.set(position, itemText);
            //Notify the adapter.
            itemAdapter.notifyItemChanged(position);
            //Persist the changes.
            saveItems();
            //Notify the user.
            Toast.makeText(getApplicationContext(), "Item updated", Toast.LENGTH_SHORT).show();
        }else{Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    //Load items by read every line of the data file.
    private void loadItems(){
        try {
            //Read every line of the file and contain into the array list.
            item = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            item = new ArrayList<>();
        }
    }

    //Save items by writing items into the data file.
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(),item);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}