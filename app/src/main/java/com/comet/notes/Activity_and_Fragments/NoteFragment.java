package com.comet.notes.Activity_and_Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.comet.notes.Adapters.NoteAdapter;
import com.comet.notes.Database.DBHandler;
import com.comet.notes.R;
import com.comet.notes.models.Note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by abdul on 10/01/15.
 */
public class NoteFragment extends Fragment {

    Button addNoteButton;
    EditText writeNoteEditText;
    GridView gridView;
    NoteFragmentInterface noteFragmentInterface;
    NoteAdapter noteAdapter = null;
    DBHandler dbHandler = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            noteFragmentInterface = (NoteFragmentInterface) activity;
        }catch (Exception e){
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notes,container,false);

        //getting references
        addNoteButton = (Button) view.findViewById(R.id.addNoteButton);
        writeNoteEditText = (EditText) view.findViewById(R.id.writeNoteEditText);
        gridView = (GridView) view.findViewById(R.id.gridView);
        dbHandler = new DBHandler(getActivity(),null,null,0);

        ArrayList<Note> list = new ArrayList<>(dbHandler.getAllNotes());
        Collections.reverse(list);
        noteAdapter = new NoteAdapter(getActivity(), list);

        gridView.setAdapter(noteAdapter);

        addNoteButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (writeNoteEditText.getText().toString().trim().equals("") || writeNoteEditText.getText() == null) {
                    Intent i = new Intent(getActivity(), NoteDetail.class);
                    startActivity(i);

                } else {
                    String text = writeNoteEditText.getText().toString();
                    //noteFragmentInterface.exchangeTextString(text);

                    Note newNote = new Note("", text, 30, "7986CB");
                   // dbHandler.addNoteWithFolder(newNote,currentlySelectedFolderId);   //temporarily commented
                    dbHandler.addNote(newNote);
                    noteAdapter.insert(newNote, 0);
                   // closeKeyboard();

                    writeNoteEditText.setText("");
                }
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                Note note = (Note) parent.getItemAtPosition(position);
                                                Intent i = new Intent(getActivity(), NoteDetail.class);
                                                i.putExtra("_noteId", note.get_noteId());
                                                i.putExtra("_noteTitle", note.get_noteTitle());
                                                i.putExtra("_noteText", note.get_noteText());
                                                i.putExtra("_noteColor", note.get_noteColor());
                                                i.putExtra("_noteTextSize", note.getNoteTextSize());
                                                i.putExtra("_positionOfNoteInViewGroup", position);

                                                startActivity(i);
                                            }
                                        }
        );


    return view;
    }


    public NoteAdapter getNoteAdapter() {
        return noteAdapter;
    }

    interface NoteFragmentInterface {
        void exchangeTextString(String text);
    }

}

