package com.comet.notes.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.comet.notes.models.Folder;
import com.comet.notes.models.Note;

import java.util.ArrayList;

/**
 * Created by abdul on 09/22/15.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 16;
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

    public void addNote(Note note) {

        SQLiteDatabase db = null;
        ContentValues values = new ContentValues();

        values.put(NOTES_COLUMN_TITLE, note.get_noteTitle());
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

    public void addNoteWithFolder(Note note, int folderId){

        addNote(note);
        //to add this note to folder...add this note id to contains table
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String query = "SELECT * FROM " +TABLE_NOTES;
        try {
            db =getWritableDatabase();

            cursor = db.rawQuery(query, null);
            cursor.moveToLast();

            ContentValues cv = new ContentValues();
            cv.put(CONTAINS_COLUMN_NOTEID, cursor.getInt(cursor.getColumnIndex(NOTES_COLUMN_ID)));
            cv.put(CONTAINS_COLUMN_FOLDERID,folderId);
            db.insert(TABLE_CONTAINS,null,cv);
        }finally {
            if(db!=null)
                db.close();
            if(cursor!=null)
                cursor.close();
        }
    }


    public ArrayList<Note> getAllNotesFromFolder(int folderId) {
        ArrayList<Note> arrayList= new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor c = null;
        try{
            db = getWritableDatabase();
            String query = "SELECT * FROM " + TABLE_NOTES  +","+ TABLE_CONTAINS+ " WHERE " +TABLE_CONTAINS+ "."+CONTAINS_COLUMN_FOLDERID + " = " +folderId
                    +" AND " + TABLE_NOTES+ "."+ NOTES_COLUMN_ID + " IN ( SELECT "+ CONTAINS_COLUMN_NOTEID + " FROM " + TABLE_CONTAINS + " ) " ;
            c = db.rawQuery(query,null);

            c.moveToFirst();

            while(!c.isAfterLast()) {

                Note note = new Note();
                note.set_noteId((c.getInt(c.getColumnIndex(NOTES_COLUMN_ID))));
                note.set_noteTitle((c.getString(c.getColumnIndex(NOTES_COLUMN_TITLE))));
                note.set_noteText((c.getString(c.getColumnIndex(NOTES_COLUMN_TEXT))));
                note.set_noteColor((c.getString(c.getColumnIndex(NOTES_COLUMN_COLOR))));
                note.setNoteTextSize(c.getInt(c.getColumnIndex(NOTES_COLUMN_TEXTSIZE)));
                arrayList.add(note);
                c.moveToNext();

            }
        }catch (Exception e){
            return new ArrayList<>();
        }
        finally {
            if(db!=null)
                db.close();
            if(c!=null)
                c.close();
        }

    return arrayList;
    }

    public int getIdOfFolderByName(String name) {
        SQLiteDatabase db = null;
        Cursor c = null;

        try {
            db = getReadableDatabase();
            String query = " SELECT " + FOLDERS_COLUMN_ID + "  FROM " + TABLE_FOLDERS + " WHERE " + FOLDERS_COLUMN_NAME +" = " + name;

            c = db.rawQuery(query,null);
             return c.getInt(c.getColumnIndex(FOLDERS_COLUMN_ID));

        } finally {
            if(db!=null)
                db.close();
            if(c !=null)
                c.close();
        }
    }


    public void addNoteToManyFolders(int noteId, ArrayList folders ) {
        SQLiteDatabase db =null;
        ContentValues contentValues = new ContentValues();

        try {
            db=getWritableDatabase();

            for (int i = 0; i < folders.size() ; i++) {
                contentValues.put(CONTAINS_COLUMN_NOTEID,noteId);
                contentValues.put(CONTAINS_COLUMN_FOLDERID,getIdOfFolderByName(folders.get(i).toString()));

                db.insert(TABLE_CONTAINS, null, contentValues);
                contentValues.clear();
            }
        } finally {
            if(db!=null)
                db.close();
        }
    }


    public void addFolder(Folder folder) {
        SQLiteDatabase db = null;
        ContentValues contentValues = new ContentValues();

        try {
            db = getWritableDatabase();
            contentValues.put(FOLDERS_COLUMN_NAME,folder.get_folderName());
            contentValues.put(FOLDERS_COLUMN_COLOR, folder.get_folderName());

            db.insert(TABLE_FOLDERS, null, contentValues);
        } finally {
            if(db!=null)
                db.close();
        }
    }

    public String[] getFolderNames() {
        SQLiteDatabase db = null;
        String array[];
        Cursor c = null;
        try{
            db = getWritableDatabase();
            String query = "SELECT " + FOLDERS_COLUMN_NAME +" FROM  " +TABLE_FOLDERS;
            c = db.rawQuery(query,null);
            c.moveToPosition(0);
            int size = c.getCount();
            array = new String[size];

            for (int i = 0; i < size; i++) {
                array[i] = c.getString(c.getColumnIndex(FOLDERS_COLUMN_NAME));
                c.moveToNext();
            }
        } finally {
            if(db!=null)
                db.close();
            if(c!=null)
                c.close();
        }
        return array;
    }

    public ArrayList<Note> getAllNotes() {

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
            //left empty
        }
        finally {
            if(c!=null)
                c.close();
            if(db!=null)
                db.close();
        }

        return list;
    }

    public ArrayList<Folder> getAllFolders(){
        SQLiteDatabase db = null;
        Cursor c = null;
        ArrayList<Folder> folderList = new ArrayList<>();

        try{
            db = getWritableDatabase();
            String query = "SELECT * FROM " + TABLE_FOLDERS;
            c = db.rawQuery(query,null);
            c.moveToFirst();

            while(!c.isAfterLast()) {
                Folder newFolder = new Folder();
                newFolder.set_folderId(c.getInt(c.getColumnIndex(FOLDERS_COLUMN_ID)));
                newFolder.set_folderName(c.getString(c.getColumnIndex(FOLDERS_COLUMN_NAME)));
                newFolder.set_folderColor(c.getString(c.getColumnIndex(FOLDERS_COLUMN_COLOR)));

                folderList.add(newFolder);
                c.moveToNext();
            }
        } finally {
            if(c!=null)
                c.close();
            if(db!=null)
                db.close();
        }

        return folderList;
    }


    public boolean checkIfFolderAlreadyPresent(String name) {
        SQLiteDatabase db = null;
        Cursor c = null;

        try {
            db= getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_FOLDERS +" WHERE " + FOLDERS_COLUMN_NAME +" = " +name;
            c = db.rawQuery(query,null);
            c.moveToFirst();
           //Toast.makeText(DBHandler.this,c.getString(c.getColumnIndex(FOLDERS_COLUMN_NAME)),Toast.LENGTH_LONG).show();

        }catch (Exception e) {
            if(c == null)
                return false;
        }

        finally {
            if(db!= null)
                db.close();
            if(c!=null)
                c.close();
        }

        return true;
    }


    public void deleteNoteById(int id){
        SQLiteDatabase db  = null;
        try{
            db = getWritableDatabase();
            String delete = "DELETE FROM "+ TABLE_NOTES+ " WHERE " + NOTES_COLUMN_ID + " = "+ id;
            db.execSQL(delete);
        } finally {
            if(db != null)
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
            if(db != null)
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
            if(db != null)
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
            if(db != null)
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
            if(db != null)
                db.close();
        }
    }

    public void getNoteTitleById(int noteId){
        SQLiteDatabase db = null;
        try{
            db = getWritableDatabase();
            String query = "SELECT * FROM " + TABLE_NOTES + " WHERE " + NOTES_COLUMN_ID + " = " + noteId;
            db.execSQL(query);
        }finally {
            if(db != null)
                db.close();
        }
    }


}
