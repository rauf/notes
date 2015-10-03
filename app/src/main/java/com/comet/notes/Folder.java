package com.comet.notes;

/**
 * Created by abdul on 10/02/15.
 */
public class Folder {

    private int _folderId;
    private String _folderName;
    private String _folderColor;

    public Folder(){
        //intentionally left blank
    }

    public Folder(String _folderName, String _folderColor) {
        this._folderName = _folderName;
        this._folderColor = _folderColor;
    }

    public Folder(int _folderId, String _folderName, String _folderColor) {
        this._folderId = _folderId;
        this._folderName = _folderName;
        this._folderColor = _folderColor;
    }



    //////////////////Getters and setters/////////////////

    public int get_folderId() {
        return _folderId;
    }

    public String get_folderName() {
        return _folderName;
    }

    public String get_folderColor() {
        return _folderColor;
    }

    public void set_folderId(int _folderId) {
        this._folderId = _folderId;
    }

    public void set_folderName(String _folderName) {
        this._folderName = _folderName;
    }

    public void set_folderColor(String _folderColor) {
        this._folderColor = _folderColor;
    }

    ///////////////////////////////////////////////////////


}
