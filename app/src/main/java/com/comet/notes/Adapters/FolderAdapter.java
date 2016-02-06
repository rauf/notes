package com.comet.notes.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.comet.notes.Activity_and_Fragments.MainActivity;
import com.comet.notes.R;
import com.comet.notes.models.Folder;

import java.util.List;

/**
 * Created by abdul on 10/03/15.
 */
public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {

    Context context;
    List<Folder> folderList;
    LayoutInflater layoutInflater;

    //self generated
    public FolderAdapter(Context context, List list) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.folderList = list;
    }

    @Override
    public FolderAdapter.FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.single_folder_ui, parent, false);
        FolderViewHolder holder = new FolderViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(FolderAdapter.FolderViewHolder holder, final int position) {

        final Folder currentFolder = folderList.get(position);

        holder.singleFolderTextView.setText(currentFolder.get_folderName());
        // holder.singleFolderCardView.setCardBackgroundColor(16744277);
        holder.singleFolderTextView.setBackgroundColor(16744277);

        holder.singleFolderCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Selected Position: "+position,Toast.LENGTH_LONG).show();
                ((MainActivity)context).setCurrentlySelectedFolder(currentFolder.get_folderId());
                ((MainActivity)context).changeNotesOnFolderPress(currentFolder.get_folderId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }


    public static class FolderViewHolder extends RecyclerView.ViewHolder  {

        TextView singleFolderTextView;
        CardView singleFolderCardView;

        public FolderViewHolder(View itemView) {
            super(itemView);
            singleFolderTextView = (TextView) itemView.findViewById(R.id.singleFolderTextView);
            singleFolderCardView = (CardView) itemView.findViewById(R.id.singleFolderCardView);

        }

    }
}