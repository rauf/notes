package com.comet.notes.Activity_and_Fragments;


import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.comet.notes.Adapters.FolderAdapter;
import com.comet.notes.Database.DBHandler;
import com.comet.notes.R;
import com.comet.notes.models.Folder;
import com.comet.notes.models.Note;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity   {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ListView leftDrawerListView;
    DBHandler dbHandler;
    TextView allNotesButton;
    NoteFragment noteFragment = null;
    FolderFragment folderFragment = null;
    static ArrayList<Note> noteList = null;
    static ArrayList<Folder> folderList = null;
    //static NoteAdapter noteAdapter = null;
    static FolderAdapter folderAdapter = null;
    public static int  currentlySelectedFolderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
       // toolbar.setNavigationIcon(R.mipmap.back_arrow);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setTitle("Notes");
        toolbar.showOverflowMenu();

        //getting references
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftDrawerListView = (ListView) findViewById(R.id.left_drawer);
        allNotesButton = (TextView) findViewById(R.id.allNotesButton);
        dbHandler = new DBHandler(this, null, null, 0);


        allNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteFragment.getNoteAdapter().updateList(dbHandler.getAllNotes());
            }
        });


/*

        String[] testString = {"abdul","rauf","alisha","naaz"};
        ArrayAdapter<String> adapter= new ArrayAdapter(this,android.R.layout.simple_list_item_1,testString);

        //feeding the left drawer in the drawer layout
        leftDrawerListView.setAdapter(adapter);

*/



        //adding a fake folder
        //dbHandler.addFolder(new Folder("Abdul","16744277"));


        ////////////// adding the note fragment to activity
        noteFragment = new NoteFragment();
        getFragmentManager().beginTransaction().add(R.id.layoutForGridView, noteFragment).commit();

        folderFragment = new FolderFragment();
        getFragmentManager().beginTransaction().add(R.id.layoutForFolderRecyclerView,folderFragment).commit();


       // noteFragment.changeNoteAdapter(noteAdapter);

       // folderFragment.changeFolderAdapter(folderAdapter);
    }



    public void changeNotesOnFolderPress(int folderId){
       // noteAdapter.updateList(dbHandler.getAllNotesFromFolder(folderId));

            noteFragment.getNoteAdapter().updateList(dbHandler.getAllNotesFromFolder(folderId));
            //NoteAdapter na = new NoteAdapter(this,dbHandler.getAllNotesFromFolder(folderId));
            //noteFragment.changeNoteAdapter(na);
    }

    ///////////////getters,setters of variables of this activity to be accessed by fragments


    public static void setCurrentlySelectedFolder(int currentlySelectedFolder) {
        MainActivity.currentlySelectedFolderId = currentlySelectedFolder;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public ListView getLeftDrawerListView() {
        return leftDrawerListView;
    }

    public  DBHandler getDbHandler() {
        return dbHandler;
    }

    public ArrayList<Note> getArrayList() {
        return noteList;
    }

    public NoteFragment getNoteFragment() {
        return noteFragment;
    }

    /////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    public void closeKeyboard(){
        ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }
}




