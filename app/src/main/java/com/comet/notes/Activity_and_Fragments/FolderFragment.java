package com.comet.notes.Activity_and_Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.comet.notes.Adapters.FolderAdapter;
import com.comet.notes.Database.DBHandler;
import com.comet.notes.R;

import java.util.ArrayList;

/**
 * Created by abdul on 10/03/15.
 */
public class FolderFragment extends Fragment {

    RecyclerView recyclerView;
    FolderAdapter folderAdapter;
    DBHandler dbHandler = null;
    ArrayList folderList =null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folders, container, false);

        dbHandler = new DBHandler(getActivity(),null,null,0);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        //setting orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);


        folderList = new ArrayList<>(dbHandler.getAllFolders());
        folderAdapter = new FolderAdapter(getActivity(),folderList);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(folderAdapter);
        // cant have onClick listener for recycler view here (recycleView do not have that method)

        return view;
    }

    public void changeFolderAdapter(FolderAdapter folderAdapter) {
        this.folderAdapter = folderAdapter;
        this.folderAdapter.notifyDataSetChanged();
    }

}
