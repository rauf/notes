package com.comet.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by abdul on 09/22/15.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 10;
    private static final String DATABASE_NAME = "notes.db";


    //table and columns for notes
    private static final String TABLE_NOTES = "notes";
    private static final String NOTES_COLUMN_ID="_noteId";
    private static final String NOTES_COLUMN_TITLE="_noteTitle";
    private static final String NOTES_COLUMN_TEXT="_noteText";
    private static final String NOTES_COLUMN_TEXTSIZE = "_noteTextSize";
    private static final String NOTES_COLUMN_COLOR = "_noteColor";


    //table and columns for folders
    private static final String TABLE_FOLDERS = "folders";
    private static final String FOLDERS_COLUMN_ID ="_folderId";
    private static final String FOLDERS_COLUMN_NAME = "_folderName";
    private static final String FOLDERS_COLUMN_COLOR = "_folderColor";

    //table and columns for relationship between notes and folders...many to many
    private static final String TABLE_CONTAINS = "contains";
    private static final String CONTAINS_COLUMN_NOTEID = "_noteId";
    private static final String CONTAINS_COLUMN_FOLDERID = "folderId";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        context.openOrCreateDatabase(DATABASE_NAME, context.MODE_PRIVATE, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createNoteTable = "CREATE TABLE " + TABLE_NOTES + " ( "+
                NOTES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                NOTES_COLUMN_TITLE + " TEXT, "+
                NOTES_COLUMN_TEXT + " TEXT NOT NULL, "+
                NOTES_COLUMN_TEXTSIZE + " INTEGER DEFAULT 20," +
                NOTES_COLUMN_COLOR + " TEXT " +
                ")";

        String createFolderTable = "CREATE TABLE " + TABLE_FOLDERS + " ( " +
                FOLDERS_COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FOLDERS_COLUMN_NAME + " TEXT NOT NULL, "+
                FOLDERS_COLUMN_COLOR + " TEXT "+
                ")";

        String createContainsTable = "CREATE TABLE " + TABLE_CONTAINS + "(" +
                CONTAINS_COLUMN_NOTEID + " INTEGER NOT NULL, " +
                CONTAINS_COLUMN_FOLDERID + " INTEGER NOT NULL, " +
                "FOREIGN KEY " + "(" + CONTAINS_COLUMN_NOTEID + ")" + " REFERENCES " +  TABLE_NOTES + "(" + NOTES_COLUMN_ID + ")" + "," +
                "FOREIGN KEY " + "(" + CONTAINS_COLUMN_FOLDERID + ")" + " REFERENCES " +  TABLE_FOLDERS + "(" + FOLDERS_COLUMN_ID + ")" +
                ")";

        db.execSQL(createNoteTable);
        db.execSQL(createFolderTable);
        db.execSQL(createContainsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTAINS);
        onCreate(db);
    }

    public void addNote(Note note){

        SQLiteDatabase db=null;
        ContentValues values = new ContentValues();

        values.put(NOTES_COLUMN_TITLE,note.get_noteTitle());
        values.put(NOTES_COLUMN_TEXT, note.get_noteText());
        values.put(NOTES_COLUMN_TEXTSIZE, note.getNoteTextSize());
        values.put(NOTES_COLUMN_COLOR, note.get_noteColor());

        try {
            db = getWritableDatabase();
            db.insert(TABLE_NOTES, null, values);
        } finally {
            if(db!=null)
                db.close();
        }

    }


    public ArrayList<Note> getAllNotes(){

        ArrayList<Note> list  = new ArrayList<>();
        SQLiteDatabase db=null;
        Cursor c=null;

        try {
            db = getReadableDatabase();
            String query = "SELECT * FROM "+ TABLE_NOTES;

            c = db.rawQuery(query, null);
            c.moveToFirst();

            while(!c.isAfterLast()){

                Note note = new Note();
                note.set_noteId(c.getInt(c.getColumnIndex(NOTES_COLUMN_ID)));
                note.set_noteTitle(c.getString(c.getColumnIndex(NOTES_COLUMN_TITLE)));
                note.set_noteText(c.getString(c.getColumnIndex(NOTES_COLUMN_TEXT)));
                note.setNoteTextSize(c.getInt(c.getColumnIndex(NOTES_COLUMN_TEXTSIZE)));
                note.set_noteColor(c.getString(c.getColumnIndex(NOTES_COLUMN_COLOR)));

                list.add(note);
                c.moveToNext();
            }
        } catch (Exception e) {

        }
        finally {
            if(c!=null)
                c.close();
            if(db!=null)
                db.close();
        }

        return list;
    }


    public void deleteNoteById(int id){
        SQLiteDatabase db  = null;
        try{
            db = getWritableDatabase();
            String delete = "DELETE FROM "+ TABLE_NOTES+ " WHERE " + NOTES_COLUMN_ID + " = "+ id;
            db.execSQL(delete);
        } finally {
            db.close();
        }
    }

    public void updateNoteTitleById(int id,String newTitle){
        SQLiteDatabase db = null;
        try{
            db=getWritableDatabase();
            String update = "UPDATE "+ TABLE_NOTES + " SET " + NOTES_COLUMN_TITLE + " = " + "\"" + newTitle + "\"" + " WHERE "+ NOTES_COLUMN_ID + " = " + id;
            db.execSQL(update);
        }
        finally {
            db.close();
        }
    }

    public void updateNoteTextById(int id,String newText){
        SQLiteDatabase db = null;
        try{
            db=getWritableDatabase();
            String update = "UPDATE "+ TABLE_NOTES + " SET " + NOTES_COLUMN_TEXT + " = " + "\"" + newText + "\"" +" WHERE "+ NOTES_COLUMN_ID + " = " + id;
            db.execSQL(update);
        }
        finally {
            db.close();
        }
    }

    public void updateNoteTextSizeById(int id,int newTextSize){
        SQLiteDatabase db = null;
        try{
            db=getWritableDatabase();
            String update = "UPDATE "+ TABLE_NOTES + " SET " + NOTES_COLUMN_TEXTSIZE + " = " + newTextSize + " WHERE "+ NOTES_COLUMN_ID + " = " + id;
            db.execSQL(update);
        }
        finally {
            db.close();
        }
    }

    public void updateColorOfNoteById(int id,String newColor){
        SQLiteDatabase db = null;
        try{
            db=getWritableDatabase();
            String update = "UPDATE "+ TABLE_NOTES + " SET " + NOTES_COLUMN_COLOR + " = " + "\"" + newColor + "\"" + " WHERE "+ NOTES_COLUMN_ID + " = " + id;
            db.execSQL(update);
        }
        finally {
            db.close();
        }
    }

    public void getNoteTitleById(int noteId){
        SQLiteDatabase db = null;
        try{
            String query = "SELECT * FROM " + TABLE_NOTES + " WHERE " + NOTES_COLUMN_ID + " = " + noteId;
            db.execSQL(query);
        }finally {
            db.close();
        }
    }


}
