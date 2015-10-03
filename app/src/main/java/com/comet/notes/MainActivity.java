package com.comet.notes;


import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements MainFragment.MainFragmentInterface {


    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ListView leftDrawerListView;
    DBHandler dbHandler;
    static ArrayList<Note> arrayList = null;
    static NoteAdapter noteAdapter = null;

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
        dbHandler = new DBHandler(this, null, null, 0);


        /////////////////// changing status bar color/////////////
   /*     Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor("#303F9F"));
*/


        ////////////// adding the main fragment to activity


/*

        String[] testString = {"abdul","rauf","alisha","naaz"};
        ArrayAdapter<String> adapter= new ArrayAdapter(this,android.R.layout.simple_list_item_1,testString);

        //feeding the left drawer in the drawer layout
        leftDrawerListView.setAdapter(adapter);

*/

        arrayList = new ArrayList<>(dbHandler.getAllNotes());
        Collections.reverse(arrayList);
        noteAdapter = new NoteAdapter(this, arrayList);

        MainFragment mainFragment = new MainFragment();
        getFragmentManager().beginTransaction().add(R.id.layoutForGridView, mainFragment).commit();

    }

    ////////get data from main fragment (override method)
    @Override
    public void exchangeTextString(String text) {

        try {
            Note newNote = new Note("", text, 30, "7986CB");
            dbHandler.addNote(newNote);
            noteAdapter.insert(newNote, 0);
            closeKeyboard();
        } finally {
            dbHandler.close();
        }
    }

    ///////////////getters of variables of this activity to be accessed by fragments

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
        return arrayList;
    }

    public NoteAdapter getNoteAdapter() {
        return noteAdapter;
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




