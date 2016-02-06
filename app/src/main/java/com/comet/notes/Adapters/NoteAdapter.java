package com.comet.notes.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.comet.notes.R;
import com.comet.notes.models.Note;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by abdul on 09/20/15.
 */


public class NoteAdapter extends ArrayAdapter<Note>{

    ArrayList<Note> arrayList = null;
    CardView cardView = null;
    TextView textContentTextView;
    TextView titleTextView;
    Note singleNoteElement;


    public NoteAdapter(Context context, ArrayList<Note> notes) {
        super(context, R.layout.single_note_ui, notes);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {         //for performance ,, it use convert view , does not create a new view every time
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.single_note_ui, parent, false);
        }

        textContentTextView = (TextView)convertView.findViewById(R.id.textContentTextView);
        titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
        cardView = (CardView) convertView.findViewById(R.id.card);

        singleNoteElement = getItem(position);

        cardView.setCardElevation(10);
        cardView.setShadowPadding(15,0,0,10);
        cardView.setRadius(0);                  //for square card


        textContentTextView.setText(singleNoteElement.get_noteText());
        textContentTextView.setTextSize(singleNoteElement.getNoteTextSize());
        titleTextView.setText(singleNoteElement.get_noteTitle());
        cardView.setCardBackgroundColor(Color.parseColor("#" + singleNoteElement.get_noteColor()));

        return convertView;
    }

    public void updateList(ArrayList<Note> newArrayList){
        Collections.reverse(newArrayList);
        this.clear();
        this.addAll(newArrayList);
        this.notifyDataSetChanged();
    }
}