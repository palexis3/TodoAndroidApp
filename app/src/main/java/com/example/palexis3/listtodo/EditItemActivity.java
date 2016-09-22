package com.example.palexis3.listtodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    EditText editItem;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_edit_item);

        editItem = (EditText) findViewById(R.id.editItem);
        position = getIntent().getIntExtra("position", -1);

        String text = getIntent().getStringExtra("text");
        editItem.append(text);
    }

    public void onEditItem(View view){

        //get text form EditTextLine
        editItem = (EditText) findViewById(R.id.editItem);
        String temp = editItem.getText().toString();

        //creating intent with all info
        Intent intent = new Intent();
        intent.putExtra("resultText", temp);
        intent.putExtra("resultPosition", position);

        //setting result to resend intent and close function
        setResult(RESULT_OK, intent);
        finish();
    }

}
