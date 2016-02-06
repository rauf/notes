package com.comet.notes.Activity_and_Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;


import com.comet.notes.Database.DBHandler;

import java.util.ArrayList;

/**
 * Created by abdul on 10/11/15.
 */
public class SelectFolderDialog extends DialogFragment{

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final DBHandler dbHandler = new DBHandler(getContext(),null,null,0);
        String arr[] = dbHandler.getFolderNames();
        final ArrayList selectedItems = new ArrayList();

        for (int i = 0; i < arr.length ; i++) {
            Toast.makeText(getActivity(),arr[i],Toast.LENGTH_LONG).show();
        }

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Toast.makeText(getActivity(),"Beginning",Toast.LENGTH_LONG).show();

        builder.setTitle("Add Folder")                                                                 //setMessage and setMultiChoiceItems don not work with each other
                .setMultiChoiceItems(arr, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked)
                            selectedItems.add(which);

                        else if (!isChecked)
                            selectedItems.remove(which);
                    }
                })

                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ////save the note to the folders
                Toast.makeText(getContext(), "Note successfully added", Toast.LENGTH_LONG).show();
                dbHandler.addNoteToManyFolders(((NoteDetail)getContext()).noteId,selectedItems);
            }
        })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Toast.makeText(getContext(), "Operation cancelled", Toast.LENGTH_LONG).show();
                    }
                });

        Toast.makeText(getActivity(), "End", Toast.LENGTH_LONG).show();


        // Create the AlertDialog object and return it
        return builder.create();
    }


}
