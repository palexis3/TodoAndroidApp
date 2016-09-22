package com.example.palexis3.listtodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ListTodoActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 200;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_todo);
        lvItems = (ListView) findViewById(R.id.lvItems);
        readItems(); //load items during onCreate()
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupEditListener();
        setupListViewListener();
    }

    //removes items from list
    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems(); //save items when a list item is removed
                        return true;
                    }
                }
        );
    }

    //edits a listview element using onClickListener
    private void setupEditListener() {
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(ListTodoActivity.this, EditItemActivity.class);
                        i.putExtra("text", items.get(position));
                        i.putExtra("position", position);
                        startActivityForResult(i, REQUEST_CODE);
                    }
                }
        );
    }

    //adding items to listView
    public void onAddItem(View v){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems(); //save items when a new list item is added
    }

    //create method to open a file and read a newline-delimited list of items
    private void readItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try{
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        }catch (IOException e){
            items = new ArrayList<>();
        }
    }

    //write elements to a file called todo.txt
    private void writeItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try{
            FileUtils.writeLines(todoFile, items);
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    //processes intent from editing activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){

                String editText = data.getStringExtra("resultText");
                int pos = data.getIntExtra("resultPosition", -1);

                //there is a legit text and position
                if(pos != -1 && editText.length() > 0){
                    items.set(pos, editText);
                    itemsAdapter.notifyDataSetChanged();
                    writeItems();
                }
                //text is empty so it is removed from listview
                else if(pos != -1 && editText.equalsIgnoreCase("")){
                    items.remove(pos);
                    itemsAdapter.notifyDataSetChanged();
                    writeItems();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
