package com.comet.notes;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;



public class NoteDetail extends AppCompatActivity {

    int noteId = -1;
    int positionOfNoteInViewGroup=-1;

    EditText titleEditText;
    EditText textEditText;

    String noteTitle;
    String noteText;
    int noteTextSize;
    String noteColor;

    DBHandler dbHandler = null;
    Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        titleEditText = (EditText) findViewById(R.id.titleEditText);
        textEditText = (EditText) findViewById(R.id.textEditText);
        toolbar =(Toolbar) findViewById(R.id.activity_main_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Note Details");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.showOverflowMenu();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // method call      //not implemented for text size and color
                saveOnPressingBackButton();
                MainActivity.noteAdapter.updateList(dbHandler.getAllNotes());
                closeKeyboard();
                finish();
            }
        });


        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.RelativeLayout);
        dbHandler = new DBHandler(this,null,null,0);

        try {
            Bundle bundle = getIntent().getExtras();

            noteId = bundle.getInt("_noteId");

            noteTitle = bundle.getString("_noteTitle");
            noteText = bundle.getString("_noteText");
            noteTextSize = bundle.getInt("_noteTextSize");
            positionOfNoteInViewGroup = bundle.getInt("_positionOfNoteInViewGroup");

            titleEditText.setText(noteTitle);
            textEditText.setText(noteText);
            textEditText.setTextSize(noteTextSize);
            noteColor = bundle.getString("_noteColor");

            relativeLayout.setBackgroundColor(Color.parseColor("#" + noteColor));
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.deleteButton:
                dbHandler.deleteNoteById(noteId);
                MainActivity.arrayList.remove(positionOfNoteInViewGroup);
                MainActivity.noteAdapter.notifyDataSetChanged();
                closeKeyboard();
                finish();
                break;

            case R.id.colorButton:
                // updateColor();               //not implemented
                break;


            default:
                Toast.makeText(NoteDetail.this, "Please Select a Valid Button", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }


    public void saveOnPressingBackButton(){
        String tempTitle = titleEditText.getText().toString().trim();
        String tempText= textEditText.getText().toString().trim();
        try {
            if (!tempTitle.equals(noteTitle))
                dbHandler.updateNoteTitleById(noteId,tempTitle);

            if(!tempText.equals(noteText))
                dbHandler.updateNoteTextById(noteId,tempText);
        }finally {
            dbHandler.close();
        }
    }

    public void closeKeyboard(){
        ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

}