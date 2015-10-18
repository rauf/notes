package com.comet.notes.Activity_and_Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
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

import com.comet.notes.Database.DBHandler;
import com.comet.notes.R;
import com.comet.notes.models.Note;


public class NoteDetail extends AppCompatActivity {

    int noteId = -1;
    int positionOfNoteInViewGroup=-1;

    boolean intentRecieved;

    EditText titleEditText;
    EditText textEditText;

    String noteTitle;
    String noteText;
    int noteTextSize;
    String noteColor;


    FloatingActionButton floatingActionButton;
    DBHandler dbHandler = null;
    Toolbar toolbar = null;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        intentRecieved = true;
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        textEditText = (EditText) findViewById(R.id.textEditText);
        toolbar =(Toolbar) findViewById(R.id.activity_main_toolbar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Note Details");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.showOverflowMenu();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // method call      //not implemented for text size and color
                calledOnGoingBack();
            }
        });



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SelectFolderDialog selectFolderDialog = new SelectFolderDialog();
                new SelectFolderDialog().show(getSupportFragmentManager(),"Abdul");
            }
        });




        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.RelativeLayout);
        dbHandler = new DBHandler(this,null,null,0);

        try {
            bundle = getIntent().getExtras();

            if(bundle == null)
                intentRecieved = false;

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
    public void onBackPressed() {
        super.onBackPressed();
        calledOnGoingBack();
    }


    public void calledOnGoingBack(){
        saveOnPressingBackButton();
       // MainActivity.noteAdapter.updateList(dbHandler.getAllNotes());

        closeKeyboard();
        finish();
    }


    public void saveOnPressingBackButton() {
        String tempTitle = titleEditText.getText().toString().trim();
        String tempText = textEditText.getText().toString().trim();
        try {
            if (intentRecieved) {
                if (!tempTitle.equals(noteTitle))
                    dbHandler.updateNoteTitleById(noteId, tempTitle);

                if (!tempText.equals(noteText) && (!tempText.equals("")))
                    dbHandler.updateNoteTextById(noteId, tempText);

                Toast.makeText(NoteDetail.this, "Note update", Toast.LENGTH_LONG).show();
            } else if (!intentRecieved) {

                if (!tempText.equals(""))
                    dbHandler.addNote(new Note(tempTitle, tempText, 30, "7FAAFF"));
            }
        } finally {
            dbHandler.close();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

                case R.id.deleteButton:
                    if(intentRecieved) {
                        try {
                            dbHandler.deleteNoteById(noteId);
                            MainActivity.noteList.remove(positionOfNoteInViewGroup);
                            //MainActivity.noteAdapter.notifyDataSetChanged();
                        } catch (Exception e){
                            Toast.makeText(NoteDetail.this,"Cannot delete note. It is not saved",Toast.LENGTH_LONG).show();
                        }
                    }
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



    public void closeKeyboard(){
        ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

}