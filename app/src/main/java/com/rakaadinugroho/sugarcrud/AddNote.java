package com.rakaadinugroho.sugarcrud;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.List;

public class AddNote extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    EditText addnote_title;
    EditText addnote_content;
    FloatingActionButton fab;

    boolean isEdit;
    String title, content;
    long time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initViews();
        getSupportActionBar().setTitle("Create Note");

        isEdit  = getIntent().getBooleanExtra("isEdit", false);
        if (isEdit){
            title    = getIntent().getStringExtra("title");
            content  = getIntent().getStringExtra("content");
            time   = getIntent().getLongExtra("time", 0);

            addnote_title.setText(title);
            addnote_content.setText(content);
        }

        fab.setOnClickListener(this);

    }
    public void initViews(){
        addnote_content = (EditText)findViewById(R.id.add_note_content);
        addnote_title   = (EditText)findViewById(R.id.add_note_title);
        fab = (FloatingActionButton)findViewById(R.id.add_note_button);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_note_button: {
                String title = addnote_title.getText().toString();
                String content = addnote_content.getText().toString();
                long time = System.currentTimeMillis();
                datasave(title, content, time);
                finish();
            }
            default:
                break;

        }
    }

    private void datasave(String title, String content, long time) {
        if (isEdit){
            List<Notes> notes   = Notes.find(Notes.class, "title = ?", this.title);
            if (notes.size() > 0){
                Notes note  = notes.get(0);
                note.title  = title;
                note.content    = content;
                note.time   = time;
                note.save();
            }
        }
        else{
            Notes note = new Notes(title, content, time);
            note.save();
        }
    }
}
