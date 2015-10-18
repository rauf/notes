package com.comet.notes.models;

/**
 * Created by abdul on 8/11/2015.
 */
public class Note {

    private int _noteId;
    private String _noteTitle;
    private String _noteText;
    private int noteTextSize;
    private String _noteColor;


    public Note(String _noteTitle, String _noteText, int noteTextSize, String _noteColor) {
        this._noteTitle = _noteTitle;
        this._noteText = _noteText;
        this.noteTextSize = noteTextSize;
        this._noteColor = _noteColor;
    }


    public Note(int _noteId,String _noteTitle, String _noteText, int noteTextSize, String _noteColor) {
        this._noteId = _noteId;
        this._noteTitle = _noteTitle;
        this._noteText = _noteText;
        this.noteTextSize = noteTextSize;
        this._noteColor = _noteColor;
    }



    public Note(){

    }

    //////////////////Getters and Setters//////////////////////

    public int get_noteId() {
        return _noteId;
    }

    public void set_noteId(int _noteId) {
        this._noteId = _noteId;
    }

    public String get_noteTitle() {
        return _noteTitle;
    }

    public void set_noteTitle(String _noteTitle) {
        this._noteTitle = _noteTitle;
    }

    public String get_noteText() {
        return _noteText;
    }

    public void set_noteText(String _noteText) {
        this._noteText = _noteText;
    }

    public String get_noteColor() {
        return _noteColor;
    }

    public void set_noteColor(String _noteColor) {
        this._noteColor = _noteColor;
    }

    public int getNoteTextSize() {
        return noteTextSize;
    }

    public void setNoteTextSize(int noteTextSize) {
        this.noteTextSize = noteTextSize;
    }


    ///////////////////////////////////////////////////////////



}
